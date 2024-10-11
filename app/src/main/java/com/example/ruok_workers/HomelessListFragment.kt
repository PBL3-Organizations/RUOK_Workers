package com.example.ruok_workers

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ruok_workers.databinding.FragmentHomelessListBinding
import java.io.File


class HomelessListFragment : Fragment() {
    lateinit var binding: FragmentHomelessListBinding
    lateinit var adapter: HomelessListAdapter

    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    var loginNum: Int = -1

    lateinit var item: ConsultationItem

    var list = ArrayList<FaviconItem>()
    var name: String = ""
    var birth: String = ""
    var bookmark: Int = -1
    var num: Int = -1
    var h_num = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    @SuppressLint("Range")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomelessListBinding.inflate(inflater, container, false)

        //기존 로그인 정보 가져오기
        loginNum = arguments?.getInt("m_num")!!

        //데이터베이스 연동
        dbManager = DBManager(requireContext(), "RUOKsample", null, 1)

        // EditText에서 엔터(완료) 버튼을 눌렀을 때 키보드를 숨김
        binding.searchEditText2.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard()
                true
            } else {
                false
            }
        }

        binding.searchButton2.setOnClickListener {

            list.clear()
            val filter = binding.searchEditText2.text.toString().trim()

            sqlitedb = dbManager.readableDatabase

            binding.searchEditText2.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    hideKeyboard()
                    true
                } else {
                    false
                }
            }

            if (filter.isNotEmpty()) {
                val cursor: Cursor
                cursor = sqlitedb.rawQuery(
                    "SELECT h.*, b.m_num IS NOT NULL AS is_bookmarked FROM homeless h LEFT JOIN bookmark b ON h.h_num = b.h_num AND b.m_num = ? WHERE h.h_name LIKE ? ORDER BY is_bookmarked DESC, h.h_name ASC",
                    arrayOf(loginNum.toString(), "%$filter%")
                )
                if (cursor != null && cursor.moveToFirst()) {
                    do {
                        name = cursor.getString(cursor.getColumnIndexOrThrow("h_name")).toString()
                        birth = cursor.getString(cursor.getColumnIndexOrThrow("h_birth")).toString()
                        num = cursor.getInt(cursor.getColumnIndex("h_num"))
                        bookmark = if (cursor.getInt(cursor.getColumnIndex("is_bookmarked")) == 1) 1 else 0

//                        var photoFilename: String = cursor.getString(cursor.getColumnIndex("h_photo"))
//                        var resId = resources.getIdentifier(photoFilename.substringBefore('.'), "drawable", requireContext().packageName)

//                        list.add(FaviconItem(name, birth, num, bookmark, resId, loginNum))

//                        val photoFilename = cursor.getString(cursor.getColumnIndex("h_photo"))
//                        var resId: Int? = null
//                        var photoPath: String? = null
//
//                        // 이미지가 URI인지 drawable인지 확인
//                        if (photoFilename.startsWith("content://") || photoFilename.startsWith("file://")) {
//                            photoPath = photoFilename  // URI 경우
//                        } else {
//                            resId = resources.getIdentifier(photoFilename.substringBefore('.'), "drawable", requireContext().packageName)
//                            if (resId != 0) {
//                                photoPath = photoFilename // Drawable 경우
//                            }
//                        }
//
//                        // FaviconItem에 photoPath (URI 또는 drawable 이름) 추가
//                        list.add(FaviconItem(name, birth, num, bookmark, photoPath, loginNum))

                        val photoFilename = cursor.getString(cursor.getColumnIndex("h_photo"))
//                        list.add(FaviconItem(name, birth, num, bookmark, photoFilename, loginNum))

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

//                        var resId = resources.getIdentifier(photoFilename.substringBefore('.'), "drawable", requireContext().packageName)
//                        if (resId != 0) {
//                            list.add(FaviconItem(name, birth, num, bookmark, resId, null, loginNum))
//                        } else {
//                            // 내부 저장소 이미지를 처리
//                            val filePath = requireContext().filesDir.absolutePath + "/" + photoFilename
//                            val imgFile = File(filePath)
//                            if (imgFile.exists()) {
//                                val bitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
//                                list.add(FaviconItem(name, birth, num, bookmark, null, bitmap, loginNum))
//                            } else {
//                                // 기본 이미지 추가
//                                list.add(FaviconItem(name, birth, num, bookmark, R.drawable.dflt, null, loginNum))
//                            }
//                        }


                    } while (cursor.moveToNext())
                }
                cursor?.close()

            } else {
                val cursor: Cursor
                cursor = sqlitedb.rawQuery(
                    "SELECT h.*, b.m_num IS NOT NULL AS is_bookmarked FROM homeless h LEFT JOIN bookmark b ON h.h_num = b.h_num AND b.m_num = ? ORDER BY is_bookmarked DESC, h.h_name ASC",
                    arrayOf(loginNum.toString())
                )
                if (cursor != null && cursor.moveToFirst()) {
                    do {
                        name = cursor.getString(cursor.getColumnIndexOrThrow("h_name")).toString()
                        birth = cursor.getString(cursor.getColumnIndexOrThrow("h_birth")).toString()
                        num = cursor.getInt(cursor.getColumnIndex("h_num"))
                        bookmark = if (cursor.getInt(cursor.getColumnIndex("is_bookmarked")) == 1) 1 else 0
//
//                        // FaviconItem에 photoPath (URI 또는 drawable 이름) 추가
//                        list.add(FaviconItem(name, birth, num, bookmark, photoPath, loginNum))

                        val photoFilename = cursor.getString(cursor.getColumnIndex("h_photo"))
//                        list.add(FaviconItem(name, birth, num, bookmark, photoFilename, loginNum))

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

//            adapter = HomelessListAdapter(requireContext(), list)

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

        //btnNextHomelessList 클릭시 HomelessListFragment에서 LocationAddFragment로 이동
        binding.btnNextHomelessList.setOnClickListener{
            val m_num = loginNum
            if (::adapter.isInitialized) h_num = adapter.h_num
            else h_num = 0

            val item = ConsultationItem(m_num, h_num, "", 0, "", "", "", "", 0.0, 0.0, arrayOf(""))

            val parentActivity = activity as DashboardActivity
            val onRecording = arguments?.getInt("onRecording", 0)!!
            val bundle = Bundle()
            bundle.putInt("onRecording", onRecording)
            bundle.putInt("hasConsultation", 1)
            bundle.putParcelable("consultation_item", item)
            val QuestionnaireFragment = QuestionnaireFragment()
            QuestionnaireFragment.arguments = bundle
            parentActivity.setFragment(QuestionnaireFragment)
        }

        //btnNoName 클릭시 h_num을 0으로 설정하고 아이템들의 배경색을 default_card로 변경
        binding.btnNoName.setOnClickListener{
            h_num = 0

            // 어댑터가 초기화되었는지 그리고 리스트에 아이템이 있는지 확인
            if (::adapter.isInitialized && adapter.itemCount > 0) {
                adapter.resetItemBackgrounds()
                binding.btnNoName.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.selected_card))
            } else {
                // 아이템이 없을 경우의 처리 로직
                binding.btnNoName.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.selected_card))
            }
        }

        //btnNewHomeless 클릭시 HomelessListFragment에서 ProfileAddFragment로 이동
        binding.btnNewHomeless.setOnClickListener{
            val parentActivity = activity as DashboardActivity
            parentActivity.setFragment(ProfileAddFragment())
        }

        return binding!!.root
    }

    // 키보드를 숨기는 함수
    private fun hideKeyboard() {
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomelessListFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}