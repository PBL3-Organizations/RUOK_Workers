package com.example.ruok_workers

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.ruok_workers.databinding.FragmentLocationTrackingBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource


class LocationTrackingFragment : Fragment(), OnMapReadyCallback {

    private lateinit var locationSource: FusedLocationSource
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var naverMap: NaverMap

    private val LOCATION_PERMISSION_REQUEST_CODE = 1000

    //위치 정보 동의
    private val PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] == true) {
                locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
                setUpMap()
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!hasPermission()) {
            requestMultiplePermissions.launch(PERMISSIONS)
        } else {
            locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
            setUpMap()
        }
    }

    // hasPermission()에서는 위치 권한 있 -> true , 없 -> false
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

    private fun updateMarkerLocation(location: Location) {
        val marker = Marker()
        marker.position = LatLng(location.latitude, location.longitude)
        marker.zIndex = 10
        marker.map = naverMap
        marker.isIconPerspectiveEnabled = true
        marker.width = Marker.SIZE_AUTO
        marker.height = Marker.SIZE_AUTO
    }

    private fun setUpMap() {
        if (::naverMap.isInitialized) {
            naverMap.locationSource = locationSource
            naverMap.uiSettings.isLocationButtonEnabled = true
            naverMap.locationTrackingMode = LocationTrackingMode.Follow
        }
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        setUpMap()

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                updateMarkerLocation(it)
            }
        }
    }


    enum class State {
        START, STOP, OTHER
    }

    lateinit var binding: FragmentLocationTrackingBinding
    //private var fstate: State = State.START
    private var fstate: State? = null

    companion object {
        const val ARG_STATE = "state"

        @JvmStatic
        fun newInstance(state: State) =
            LocationTrackingFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_STATE, state)
                }
            }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            fstate = it.getSerializable(ARG_STATE, State::class.java)
        }
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentLocationTrackingBinding.inflate(inflater, container, false)

        fstate?.let { updateButtonVisibility(it) }

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

    fun updateButtonVisibility(state: State) {
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