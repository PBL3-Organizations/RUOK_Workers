package com.example.ruok_workers

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.ruok_workers.databinding.FragmentLocationTrackingBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.Locale


@Suppress("DEPRECATION")
class LocationTrackingFragment : Fragment(), OnMapReadyCallback {

    lateinit var binding: FragmentLocationTrackingBinding

    private lateinit var locationSource: FusedLocationSource
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var naverMap: NaverMap

    private val LOCATION_PERMISSION_REQUEST_CODE = 1000
    private var cameraMoved = false  // 카메라가 이동되었는지 여부를 나타내는 플래그

    //위치 정보 동의
    private val PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true) {
                initMapView()
            }
            else {
                // 권한이 거부된 경우 사용자에게 알림
                requestPermissions(PERMISSIONS, LOCATION_PERMISSION_REQUEST_CODE)
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        if (!hasPermission()) {
            requestMultiplePermissions.launch(PERMISSIONS)
        } else {
            locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
            initMapView()
        }
    }

    // 권한 확인
    private fun hasPermission(): Boolean {
        for (permission in PERMISSIONS) {
            if (activity?.let { ContextCompat.checkSelfPermission(it, permission) }
                != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    private fun initMapView() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as MapFragment?
            ?: MapFragment.newInstance().also {
                childFragmentManager.beginTransaction().add(R.id.map, it).commit()
            }
        // fragment의 getMapAsync() 메서드로 OnMapReadyCallBack 콜백을 등록하면, 비동기로 NaverMap 객체를 얻을 수 있음
        mapFragment.getMapAsync(this)
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        setUpMap()

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                updateMarkerLocation(it)
                updateAddress(it)
            }
        }

        naverMap.addOnLocationChangeListener { location ->
            updateMarkerLocation(location)
            updateAddress(location)
        }
    }

    private fun setUpMap() {
        if (::naverMap.isInitialized) {
            naverMap.locationSource = locationSource //현 위치
            naverMap.uiSettings.isLocationButtonEnabled = true //현 위치 버튼 기능
            naverMap.locationTrackingMode = LocationTrackingMode.Follow //위치를 추적하면서 카메라도 같이 움직임
        }
    }

    private fun updateMarkerLocation(location: Location): Marker {

        val marker = Marker()
        marker.setMap(null)

        Marker().apply{
            marker.position = LatLng(location.latitude, location.longitude) //마커 위치
//          marker.zIndex = 10 //마커 우선순위
            marker.map = naverMap //마커 표시
            marker.isIconPerspectiveEnabled = true //원근감 표시
            marker.width = Marker.SIZE_AUTO
            marker.height = Marker.SIZE_AUTO
        }

        if (!cameraMoved) {  // 카메라가 아직 이동되지 않은 경우에만 이동
            val cameraUpdate = CameraUpdate.scrollTo(marker.position)
            naverMap.moveCamera(cameraUpdate)
            cameraMoved = true  // 플래그를 true로 설정하여 이후에는 카메라를 이동하지 않음
        }

        return marker
    }

    @Suppress("DEPRECATION")
    private fun updateAddress(location: Location) {
        val geocoder = context?.let { Geocoder(it, Locale.KOREA) }  // 한국어로 설정
        lifecycleScope.launch {
            try {
                val addresses: List<Address>? =
                    geocoder?.getFromLocation(location.latitude, location.longitude, 1)
                if (!addresses.isNullOrEmpty()) {
                    val address = addresses[0].getAddressLine(0)
                    val placeName = addresses[0].featureName
                }
            } catch (e: IOException) {
                Log.e(TAG, "Geocoder failed", e)
            }
        }
    }

    enum class State {
        START, STOP, OTHER
    }

    private var state: State? = State.START

    companion object {
        const val ARG_STATE = "State"
        const val TAG = "LocationTrackingFragment"

        @JvmStatic
        fun newInstance(state: State) =
            LocationTrackingFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_STATE, state)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        state = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getSerializable(ARG_STATE, State::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getSerializable(ARG_STATE) as? State
        }
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentLocationTrackingBinding.inflate(inflater, container, false)

        state?.let { updateButtonVisibility(it) }

        //활성화 버튼 교체
        binding.btnStartOutreach.setOnClickListener {
            updateButtonVisibility(State.STOP)
        }

        binding.btnStopOutreach.setOnClickListener {
            updateButtonVisibility(State.OTHER)
        }

        binding.btnContinueOutreach.setOnClickListener {
            updateButtonVisibility(State.STOP)
        }

        //btnRecordOutreach 클릭시 LocationTrackingFragment에서 QuestionnaireFragment로 이동
        binding.btnRecordOutreach.setOnClickListener{
            val parentActivity = activity as DashboardActivity
            parentActivity.setFragment(QuestionnaireFragment())
        }

        //btnEndOutreach 클릭시 LocationTrackingFragment에서 ListFragment로 이동
        binding.btnEndOutreach.setOnClickListener{
            val parentActivity = activity as DashboardActivity
            parentActivity.setFragment(ListFragment())
        }

        return binding.root
    }

    private fun updateButtonVisibility(state: State) {
        when (state) {
            State.START -> {
                binding.btnStartOutreach.visibility = View.VISIBLE
                binding.btnStopOutreach.visibility = View.GONE
                binding.btnContinueOutreach.visibility = View.GONE
                binding.btnRecordOutreach.visibility = View.GONE
                binding.btnEndOutreach.visibility = View.GONE
            }
            State.STOP -> {
                binding.btnStartOutreach.visibility = View.GONE
                binding.btnStopOutreach.visibility = View.VISIBLE
                binding.btnContinueOutreach.visibility = View.GONE
                binding.btnRecordOutreach.visibility = View.GONE
                binding.btnEndOutreach.visibility = View.GONE
            }
            State.OTHER -> {
                binding.btnStartOutreach.visibility = View.GONE
                binding.btnStopOutreach.visibility = View.GONE
                binding.btnContinueOutreach.visibility = View.VISIBLE
                binding.btnRecordOutreach.visibility = View.VISIBLE
                binding.btnEndOutreach.visibility = View.VISIBLE
            }
        }
    }

}