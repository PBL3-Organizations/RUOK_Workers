package com.example.ruok_workers

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.ruok_workers.databinding.FragmentLocationAddBinding
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
import com.naver.maps.map.util.MarkerIcons
import kotlinx.coroutines.launch
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class LocationAddFragment : Fragment(), OnMapReadyCallback {
    lateinit var binding: FragmentLocationAddBinding

    private lateinit var locationSource: FusedLocationSource
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var naverMap: NaverMap

    private val LOCATION_PERMISSION_REQUEST_CODE = 1000
    private var cameraMoved = false  // 카메라가 이동되었는지 여부를 나타내는 플래그
    private var isManualLocation = false // 위치 업데이트를 수동으로 설정하는 플래그

    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    lateinit var item: ConsultationItem
    var l_addr: String = ""
    var l_lat: Double = 0.0
    var l_lon: Double = 0.0

    private var currentMarker: Marker? = null

    //위치 정보 동의
    private val PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true) {
                locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
                initMapView()
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
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapLocationShow) as MapFragment?
            ?: MapFragment.newInstance().also {
                childFragmentManager.beginTransaction().add(R.id.mapLocationShow, it).commit()
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
            if (!isManualLocation) {
                updateMarkerLocation(location)
                updateAddress(location)
            }

        }

        // 클릭한 곳으로 마커와 주소 업데이트
        naverMap.setOnMapClickListener { _, latLng ->
            val location = Location("").apply {
                latitude = latLng.latitude
                longitude = latLng.longitude
            }

            updateMarkerLocation(location)
            updateAddress(location)
            isManualLocation = true
        }
    }

    private fun setUpMap() {
        if (::naverMap.isInitialized) {
            naverMap.locationSource = locationSource //현 위치
            naverMap.uiSettings.isLocationButtonEnabled = true //현 위치 버튼 기능
            naverMap.locationTrackingMode = LocationTrackingMode.Follow //위치를 추적하면서 카메라도 같이 움직임
            naverMap.isIndoorEnabled = true //실내지도 활성화

            //더블 클릭하면 현 위치 정보로 업데이트
            naverMap.setOnMapDoubleTapListener { pointF, latLng ->
                isManualLocation = false // 현 위치 버튼 클릭 시 자동 위치 업데이트 재개
                cameraMoved = false //카메라 현 위치로 이동
                false // 기본 동작 유지
            }
        }
    }

    private fun updateMarkerLocation(location: Location): Marker {
        // 기존 마커가 있으면 제거
        currentMarker?.map = null

        currentMarker = Marker().apply {
            position = LatLng(location.latitude, location.longitude) //마커 위치
            map = naverMap //마커 표시
            isIconPerspectiveEnabled = true //원근감 표시
            width = Marker.SIZE_AUTO
            height = Marker.SIZE_AUTO
            icon = MarkerIcons.BLACK
            iconTintColor = Color.RED
        }

        if (!cameraMoved) {  // 카메라가 아직 이동되지 않은 경우에만 이동
            val cameraUpdate = CameraUpdate.scrollTo(currentMarker!!.position)
            naverMap.moveCamera(cameraUpdate)
            cameraMoved = true  // 플래그를 true로 설정하여 이후에는 카메라를 이동하지 않음
        }

        return currentMarker as Marker
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

                    // "대한민국" 제거
                    val addressParts = address.split(" ")
                    val filteredAddress = addressParts.drop(1).joinToString(" ")

                    //데이터 가져오기
                    item.addr = filteredAddress
                    item.latitude = location.latitude
                    item.longitude = location.longitude

                    // TextView에 동적으로 값 설정
                    binding.tvAddressLocationAdd.text = filteredAddress
                    binding.tvAddressPopLocationAdd.text = filteredAddress
                }
            } catch (e: IOException) {
                Log.e(LocationTrackingFragment.TAG, "Geocoder failed", e)
            }
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLocationAddBinding.inflate(inflater, container, false)

        //btnSetLocationAdd 클릭시 만난 위치 레이아웃 나타남
        binding.btnSetLocationAdd.setOnClickListener {
            binding.llPopLocationAdd.visibility = View.VISIBLE
        }

        val onRecording = arguments?.getInt("onRecording", 0)!!
        val bundle = Bundle()
        bundle.putInt("onRecording", onRecording)

        item = arguments?.getParcelable<ConsultationItem>("consultation_item")!!

        val hasConsultation = arguments?.getInt("hasConsultation")!!
        bundle.putInt("hasConsultation", hasConsultation)
        bundle.putParcelable("consultation_item", item)

        //btnBeforeLocationAdd 클릭시 LocationAddFragment에서 HomelessListFragment로 이동
        binding.btnBeforeLocationAdd.setOnClickListener{
            val parentActivity = activity as DashboardActivity
            val homelessListFragment = HomelessListFragment()
            homelessListFragment.arguments = bundle
            parentActivity.setFragment(homelessListFragment)
        }

        //btnCompleteLocationAdd 클릭시 LocationAddFragment에서 LocationTrackingFragment로 이동
        binding.btnCompleteLocationAdd.setOnClickListener{
            val parentActivity = activity as DashboardActivity

            val currentTime = System.currentTimeMillis()
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date(currentTime))

            dbManager = DBManager(requireContext(), "RUOKsample", null, 1)
            sqlitedb = dbManager.writableDatabase
            var cursor: Cursor
            var sql = "BEGIN TRANSACTION;"
            sqlitedb.execSQL(sql)
            sql = "INSERT INTO consultation(m_num, h_num, c_time, c_health, c_unusual, c_measure, c_content) VALUES (?, ?, ?, ?, ?, ?, ?);"
            sqlitedb.execSQL(sql, arrayOf(item.m_num.toString(), item.h_num.toString(), sdf, item.health.toString(), item.unusual, item.measure, item.content))
            sql = "SELECT last_insert_rowid() as c_num;"
            cursor = sqlitedb.rawQuery(sql, arrayOf())
            sql = "COMMIT;"
            sqlitedb.execSQL(sql)

            cursor.moveToNext()
            val c_num = cursor.getInt(cursor.getColumnIndexOrThrow("c_num"))

            for (i in 0 until item.filename.size) {
                sql = "INSERT INTO photo(p_filename, c_num) VALUES(?, ?);"
                sqlitedb.execSQL(sql, arrayOf(item.filename.get(i), c_num.toString()))
            }

            sql = "INSERT INTO location(c_num, l_addr, l_lat, l_lon)  VALUES(?, ?, ?, ?);"
            sqlitedb.execSQL(sql, arrayOf(c_num.toString(), item.addr, item.latitude, item.longitude))

            cursor.close()
            sqlitedb.close()
            dbManager.close()

            if (onRecording == 1) {
                val locationTrackingFragment = LocationTrackingFragment()
                locationTrackingFragment.arguments = bundle
                parentActivity.setFragment(locationTrackingFragment)
            } else {
                val listFragment = ListFragment()
                listFragment.arguments = bundle
                parentActivity.setFragment(listFragment)
            }

            Toast.makeText(context, "상담내역 저장!", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LocationAddFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}