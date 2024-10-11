package com.example.ruok_workers

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ruok_workers.databinding.FragmentHomelessRevisionBinding
import java.io.File

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomelessRevisionFragment : Fragment() {
    lateinit var binding: FragmentHomelessRevisionBinding
    lateinit var adapter: HomelessListAdapter

    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    lateinit var item: ConsultationItem
    var c_num = -1
    var loginNum = -1

    var list = ArrayList<FaviconItem>()
    var name: String = ""
    var birth: String = ""
    var bookmark: Int = -1
    var num: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomelessRevisionBinding.inflate(inflater, container, false)

        //로그인 정보 가져오기
        loginNum = arguments?.getInt("m_num", 0)!!

        //전달받은 데이터 가져오기 및 보내기
        val bundle = Bundle()
        item = arguments?.getParcelable<ConsultationItem>("consultation_item")!!
        val hasConsultation = arguments?.getInt("hasConsultation")!!
        c_num = arguments?.getInt("c_num", 0)!!
        num = arguments?.getInt("h_num", 0)!!

        if (num == 0) {
            list.clear()
        } else {
            //기존 데이터 가져와서 적용
            dbManager = DBManager(requireContext(), "RUOKsample", null, 1)
            sqlitedb = dbManager.readableDatabase
            var cursor: Cursor
            val sql = "SELECT h.*, b.m_num IS NOT NULL AS is_bookmarked FROM homeless h LEFT JOIN bookmark b ON h.h_num = b.h_num AND b.m_num = ? WHERE h.h_num = ?;"
            cursor = sqlitedb.rawQuery(sql, arrayOf(loginNum.toString(), item.h_num.toString()))
            cursor.moveToNext()
            name = cursor.getString(cursor.getColumnIndexOrThrow("h.h_name")).toString()
            birth = cursor.getString(cursor.getColumnIndexOrThrow("h.h_birth")).toString()
            num = cursor.getInt(cursor.getColumnIndexOrThrow("h.h_num"))
            bookmark = if (cursor.getInt(cursor.getColumnIndexOrThrow("is_bookmarked")) == 1) 1 else 0

            var photoFilename: String = cursor.getString(cursor.getColumnIndexOrThrow("h_photo"))
//            var resId = resources.getIdentifier(photoFilename.substringBefore('.'), "drawable", requireContext().packageName)

//            var photoUri: String = cursor.getString(cursor.getColumnIndexOrThrow("h_photo"))
//            val resId = resources.getIdentifier(photoUri.substringBefore('.'), "drawable", requireContext().packageName)

            cursor.close()
            sqlitedb.close()
            dbManager.close()

            list.clear()
//            list.add(FaviconItem(name, birth, num, bookmark, photoFilename, loginNum))  // photoFilename을 사용
//            list.add(FaviconItem(name, birth, num, bookmark, resId, loginNum))

            if (photoFilename.startsWith("/")) {
                // 내부 저장소 경로에서 이미지 불러오기
                val file = File(photoFilename)
                if (file.exists()) {
                    // Bitmap으로 변환하여 ImageView에 설정
                    val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                    list.add(FaviconItem(name, birth, num, bookmark, null, bitmap, loginNum))
                } else {
                    // 파일이 없을 경우 기본 이미지 설정
                    list.add(FaviconItem(name, birth, num, bookmark, R.drawable.dflt, null, loginNum))
                }
            } else {
                // drawable 이미지 불러오기
                val resId = resources.getIdentifier(photoFilename.substringBefore('.'), "drawable", requireContext().packageName)
                if (resId != 0) {
                    list.add(FaviconItem(name, birth, num, bookmark, resId, null, loginNum))
                } else {
                    // 이미지가 없는 경우 기본 이미지 표시
                    list.add(FaviconItem(name, birth, num, bookmark, R.drawable.dflt, null, loginNum))
                }
            }

        }

        binding.centerTextView2.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.VISIBLE
        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        // Adapter에 OnItemClickListener를 전달
        adapter = HomelessListAdapter(requireContext(), list, object : HomelessListAdapter.OnItemClickListener {
            override fun onItemClicked() {
                // 아이템이 클릭될 때 btnNoName의 배경색을 default_card로 변경
                binding.btnNoName.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.default_card))
            }
        })

        binding.recyclerView.adapter = adapter

        //데이터베이스 연동
        dbManager = DBManager(requireContext(), "RUOKsample", null, 1)

        binding.searchButton2.setOnClickListener {

            list.clear()
            val filter = binding.searchEditText2.text.toString().trim()

            sqlitedb = dbManager.readableDatabase

            if (filter.isNotEmpty()) {
                val cursor: Cursor
                cursor = sqlitedb.rawQuery(
                    "SELECT h.*, b.m_num IS NOT NULL AS is_bookmarked FROM homeless h LEFT JOIN bookmark b ON h.h_num = b.h_num AND b.m_num = ? WHERE h.h_name LIKE ? ORDER BY is_bookmarked DESC",
                    arrayOf(loginNum.toString(), "%$filter%")
                )
                if (cursor != null && cursor.moveToFirst()) {
                    do {
                        name = cursor.getString(cursor.getColumnIndexOrThrow("h.h_name")).toString()
                        birth = cursor.getString(cursor.getColumnIndexOrThrow("h.h_birth")).toString()
                        num = cursor.getInt(cursor.getColumnIndexOrThrow("h.h_num"))
                        bookmark = if (cursor.getInt(cursor.getColumnIndexOrThrow("is_bookmarked")) == 1) 1 else 0

                        var photoFilename: String = cursor.getString(cursor.getColumnIndexOrThrow("h_photo"))
//                        var resId = resources.getIdentifier(photoFilename.substringBefore('.'), "drawable", requireContext().packageName)

                        if (photoFilename.startsWith("/")) {
                            // 내부 저장소 경로에서 이미지 불러오기
                            val file = File(photoFilename)
                            if (file.exists()) {
                                // Bitmap으로 변환하여 ImageView에 설정
                                val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                                list.add(FaviconItem(name, birth, num, bookmark, null, bitmap, loginNum))
                            } else {
                                // 파일이 없을 경우 기본 이미지 설정
                                list.add(FaviconItem(name, birth, num, bookmark, R.drawable.dflt, null, loginNum))
                            }
                        } else {
                            // drawable 이미지 불러오기
                            val resId = resources.getIdentifier(photoFilename.substringBefore('.'), "drawable", requireContext().packageName)
                            if (resId != 0) {
                                list.add(FaviconItem(name, birth, num, bookmark, resId, null, loginNum))
                            } else {
                                // 이미지가 없는 경우 기본 이미지 표시
                                list.add(FaviconItem(name, birth, num, bookmark, R.drawable.dflt, null, loginNum))
                            }
                        }

                    } while (cursor.moveToNext())
                }
                cursor?.close()

            } else {
                val cursor: Cursor
                cursor = sqlitedb.rawQuery(
                    "SELECT h.*, b.m_num IS NOT NULL AS is_bookmarked FROM homeless h LEFT JOIN bookmark b ON h.h_num = b.h_num AND b.m_num = ? ORDER BY is_bookmarked DESC",
                    arrayOf(loginNum.toString())
                )
                if (cursor != null && cursor.moveToFirst()) {
                    do {
                        name = cursor.getString(cursor.getColumnIndexOrThrow("h.h_name")).toString()
                        birth = cursor.getString(cursor.getColumnIndexOrThrow("h.h_birth")).toString()
                        num = cursor.getInt(cursor.getColumnIndexOrThrow("h.h_num"))
                        bookmark = if (cursor.getInt(cursor.getColumnIndexOrThrow("is_bookmarked")) == 1) 1 else 0

                        val photoFilename: String = cursor.getString(cursor.getColumnIndexOrThrow("h_photo"))

                        if (photoFilename.startsWith("/")) {
                            // 내부 저장소 경로에서 이미지 불러오기
                            val file = File(photoFilename)
                            if (file.exists()) {
                                // Bitmap으로 변환하여 ImageView에 설정
                                val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                                list.add(FaviconItem(name, birth, num, bookmark, null, bitmap, loginNum))
                            } else {
                                // 파일이 없을 경우 기본 이미지 설정
                                list.add(FaviconItem(name, birth, num, bookmark, R.drawable.dflt, null, loginNum))
                            }
                        } else {
                            // drawable 이미지 불러오기
                            val resId = resources.getIdentifier(photoFilename.substringBefore('.'), "drawable", requireContext().packageName)
                            if (resId != 0) {
                                list.add(FaviconItem(name, birth, num, bookmark, resId, null, loginNum))
                            } else {
                                // 이미지가 없는 경우 기본 이미지 표시
                                list.add(FaviconItem(name, birth, num, bookmark, R.drawable.dflt, null, loginNum))
                            }
                        }

                    } while (cursor.moveToNext())
                }
                cursor?.close()
            }

            binding.centerTextView2.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.VISIBLE

            binding.recyclerView.layoutManager = LinearLayoutManager(context)

            // Adapter에 OnItemClickListener를 전달
            adapter = HomelessListAdapter(requireContext(), list, object : HomelessListAdapter.OnItemClickListener {
                override fun onItemClicked() {
                    // 아이템이 클릭될 때 btnNoName의 배경색을 default_card로 변경
                    binding.btnNoName.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.default_card))
                }
            })

            binding.recyclerView.adapter = adapter

            sqlitedb.close()
        }

        dbManager.close()

        //btnBeforeHomelessList 클릭시 HomelessRevisionFragment에서 PhotoRevisionFragment로 이동
        binding.btnBeforeHomelessList.setOnClickListener{
            val parentActivity = activity as DashboardActivity
            val photoRevisionFragment = PhotoRevisionFragment()
            bundle.putInt("hasConsultation", hasConsultation)
            bundle.putInt("c_num", c_num)
            bundle.putInt("h_num", num)
            bundle.putParcelable("consultation_item", item)
            photoRevisionFragment.arguments = bundle
            parentActivity.setFragment(photoRevisionFragment)
        }

        //btnNextHomelessList 클릭시 HomelessRevisionFragment에서 LocationRevisionFragment로 이동
        binding.btnNextHomelessList.setOnClickListener{
            Log.d("HomelessRevisionFragment", "Current item.h_num: ${item.h_num}, adapter.h_num: ${adapter.h_num}")
            item.h_num = adapter.h_num
            Log.d("HomelessRevisionFragment", "After assigning adapter.h_num to item.h_num: item.h_num: ${item.h_num}, num: $num")
            if (num != 0) num = item.h_num
            Log.d("HomelessRevisionFragment", "After checking num != 0: item.h_num: ${item.h_num}, num: $num")
            val parentActivity = activity as DashboardActivity
            val locationRevisionFragment = LocationRevisionFragment()
            bundle.putInt("hasConsultation", hasConsultation)
            bundle.putInt("c_num", c_num)
            bundle.putInt("h_num", num)
            Log.d("HomelessRevisionFragment", "Final values before transition: hasConsultation: $hasConsultation, c_num: $c_num, h_num: ${item.h_num}, num: $num")
            bundle.putParcelable("consultation_item", item)
            locationRevisionFragment.arguments = bundle
            parentActivity.setFragment(locationRevisionFragment)
        }

        //btnNoName 클릭시 num을 0으로 설정하고 아이템들의 배경색을 default_card로 변경
        binding.btnNoName.setOnClickListener{
            num = 0

            // 어댑터가 초기화되었는지 그리고 리스트에 아이템이 있는지 확인
            if (::adapter.isInitialized && adapter.itemCount > 0) {
                adapter.resetItemBackgrounds()
                binding.btnNoName.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.selected_card))
            } else {
                // 아이템이 없을 경우의 처리 로직
                binding.btnNoName.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.selected_card))
            }
        }

        //btnNewHomeless 클릭시 HomelessRevisionFragment에서 ProfileAddFragment로 이동
        binding.btnNewHomeless.setOnClickListener{
            val parentActivity = activity as DashboardActivity
            parentActivity.setFragment(ProfileAddFragment())
        }

        return binding!!.root
    }

    private fun goToLocationRevisionFragment() {
        val fragment = LocationRevisionFragment()
        parentFragmentManager.commit {
            replace(R.id.rootLayout, fragment)
            addToBackStack(null)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomelessRevisionFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
