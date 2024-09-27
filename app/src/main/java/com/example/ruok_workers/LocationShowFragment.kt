package com.example.ruok_workers

import android.Manifest
import android.content.pm.PackageManager
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.ruok_workers.databinding.FragmentLocationShowBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.MarkerIcons

class LocationShowFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentLocationShowBinding
    private lateinit var naverMap: NaverMap
    private var selectedMarker: Marker? = null  // 현재 선택된 마커

    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    // 권한 설정
    private val LOCATION_PERMISSION_REQUEST_CODE = 1000
    private val PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLocationShowBinding.inflate(inflater, container, false)

        if (!hasLocationPermission()) {
            requestPermissions(PERMISSIONS, LOCATION_PERMISSION_REQUEST_CODE)
        } else {
            initMapView()
        }

        return binding.root
    }

    // 위치 권한 확인
    private fun hasLocationPermission(): Boolean {
        for (permission in PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(requireContext(), permission)
                != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    // 지도 초기화
    private fun initMapView() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapLocationShow) as MapFragment?
            ?: MapFragment.newInstance().also {
                childFragmentManager.beginTransaction().add(R.id.mapLocationShow, it).commit()
            }
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        setUpMap()

        // 데이터베이스에서 위치 정보 가져오기
        val locations = getLatestLocations()

        // 가장 최근 상담자의 위치가 있는 경우
        if (locations.isNotEmpty()) {
            // 첫 번째 위치를 카메라의 초기 위치로 설정
            val latestLocation = locations[0]
            val cameraUpdate = CameraUpdate.scrollTo(LatLng(latestLocation.first, latestLocation.second))
            naverMap.moveCamera(cameraUpdate)
        }

        // 마커를 지도에 추가하고 클릭 이벤트 처리
        addMarkersOnMap(naverMap, locations)
    }

    // 지도 설정
    private fun setUpMap() {
        naverMap.uiSettings.isLocationButtonEnabled = true
        naverMap.locationTrackingMode = LocationTrackingMode.Follow
    }

    // 최신 상담 내역의 위치 정보를 가져오는 함수
    private fun getLatestLocations(): List<Pair<Double, Double>> {
        val locations = mutableListOf<Pair<Double, Double>>()

        dbManager = DBManager(requireContext(), "RUOKsample", null, 1)
        sqlitedb = dbManager.readableDatabase

        val consultationCursor: Cursor = sqlitedb.rawQuery(
            """
            SELECT h_num, MAX(c_time) AS latest_time
            FROM consultation
            GROUP BY h_num
            """, null
        )

        while (consultationCursor.moveToNext()) {
            val hNum = consultationCursor.getInt(consultationCursor.getColumnIndexOrThrow("h_num"))
            val latestTime = consultationCursor.getString(consultationCursor.getColumnIndexOrThrow("latest_time"))

            // 최신 상담 시간에 해당하는 c_num 조회
            val cNumCursor = sqlitedb.rawQuery(
                """
                SELECT c_num FROM consultation 
                WHERE h_num = ? AND c_time = ?
                """, arrayOf(hNum.toString(), latestTime)
            )

            if (cNumCursor.moveToFirst()) {
                val cNum = cNumCursor.getInt(cNumCursor.getColumnIndexOrThrow("c_num"))

                // 해당 c_num으로 location 테이블에서 l_lat, l_lon 조회
                val locationCursor = sqlitedb.rawQuery(
                    """
                    SELECT l_lat, l_lon FROM location 
                    WHERE c_num = ?
                    """, arrayOf(cNum.toString())
                )

                if (locationCursor.moveToFirst()) {
                    val lLat = locationCursor.getDouble(locationCursor.getColumnIndexOrThrow("l_lat"))
                    val lLon = locationCursor.getDouble(locationCursor.getColumnIndexOrThrow("l_lon"))
                    locations.add(Pair(lLat, lLon))
                }
                locationCursor.close()
            }
            cNumCursor.close()
        }

        consultationCursor.close()
        sqlitedb.close()
        dbManager.close()

        return locations
    }

    // 위치 리스트를 받아서 지도에 마커를 추가하는 함수
    private fun addMarkersOnMap(naverMap: NaverMap, locations: List<Pair<Double, Double>>) {
        locations.forEach { location ->
            val marker = Marker().apply {
                position = LatLng(location.first, location.second)  // 위도, 경도 설정
                map = naverMap
                icon = MarkerIcons.BLACK  // 마커 스타일
                iconTintColor = Color.RED  // 초기 마커 색상은 빨간색
            }

            // 마커 클릭 이벤트 처리
            marker.setOnClickListener {
                // 이전 선택된 마커가 있으면 색상을 다시 빨간색으로 변경
                selectedMarker?.iconTintColor = Color.RED

                // 선택된 마커를 초록색으로 변경
                marker.iconTintColor = Color.GREEN
                selectedMarker = marker  // 현재 선택된 마커 업데이트

                // 마커의 위치를 통해 c_num으로 h_num 조회 후 h_name, h_birth 가져오기
                val hInfo = getHomelessInfoByMarker(marker.position)

                // UI 업데이트: 이름과 생년월일 표시
                if (hInfo != null) {
                    binding.tvNameLocationShow.text = hInfo.first
                    binding.tvBirthLocationShow.text = hInfo.second
                }

                true
            }

            marker.map = naverMap
        }
    }

    // c_num을 통해 h_num을 찾고, h_num을 통해 h_name과 h_birth를 조회하는 함수
    private fun getHomelessInfoByMarker(position: LatLng): Pair<String, String>? {
        dbManager = DBManager(requireContext(), "RUOKsample", null, 1)
        sqlitedb = dbManager.readableDatabase

        // 1. 위치 정보로 c_num 가져오기
        val locationCursor = sqlitedb.rawQuery(
            """
            SELECT c_num FROM location 
            WHERE l_lat = ? AND l_lon = ?
            """, arrayOf(position.latitude.toString(), position.longitude.toString())
        )

        if (locationCursor.moveToFirst()) {
            val cNum = locationCursor.getInt(locationCursor.getColumnIndexOrThrow("c_num"))

            // 2. c_num으로 consultation 테이블에서 h_num 가져오기
            val consultationCursor = sqlitedb.rawQuery(
                """
                SELECT h_num FROM consultation 
                WHERE c_num = ?
                """, arrayOf(cNum.toString())
            )

            if (consultationCursor.moveToFirst()) {
                val hNum = consultationCursor.getInt(consultationCursor.getColumnIndexOrThrow("h_num"))

                // 3. h_num으로 homeless 테이블에서 h_name과 h_birth 가져오기
                val homelessCursor = sqlitedb.rawQuery(
                    """
                    SELECT h_name, h_birth FROM homeless 
                    WHERE h_num = ?
                    """, arrayOf(hNum.toString())
                )

                if (homelessCursor.moveToFirst()) {
                    val hName = homelessCursor.getString(homelessCursor.getColumnIndexOrThrow("h_name"))
                    val hBirth = homelessCursor.getString(homelessCursor.getColumnIndexOrThrow("h_birth"))
                    homelessCursor.close()
                    consultationCursor.close()
                    locationCursor.close()
                    sqlitedb.close()
                    dbManager.close()

                    return Pair(hName, hBirth)  // 이름과 생년월일 반환
                }
                homelessCursor.close()
            }
            consultationCursor.close()
        }
        locationCursor.close()
        sqlitedb.close()
        dbManager.close()

        return null
    }
}
