package com.example.ruok_workers

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ruok_workers.databinding.FragmentSearchBinding
import java.io.File

class SearchFragment : Fragment() {

    lateinit var binding: FragmentSearchBinding
    lateinit var faviconAdapter: FaviconAdapter

    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    var loginNum: Int = -1

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

        binding = FragmentSearchBinding.inflate(inflater, container, false)

        //기존 로그인 정보 가져오기
        loginNum = arguments?.getInt("m_num")!!

        dbManager = DBManager(requireContext(), "RUOKsample", null, 1)

        var itemList = ArrayList<FaviconItem>()
        var name: String
        var birth: String
        var bookmark: Int
        var num: Int

        binding.searchButton.setOnClickListener {

            itemList.clear()
            val filter = binding.searchEditText.text.toString().trim()

            sqlitedb = dbManager.readableDatabase

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

                        var photoFilename: String = cursor.getString(cursor.getColumnIndex("h_photo"))

                        if (photoFilename.startsWith("/")) {
                            // 내부 저장소 경로에서 이미지 불러오기
                            val file = File(photoFilename)
                            if (file.exists()) {
                                // Bitmap으로 변환하여 ImageView에 설정
                                val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                                itemList.add(FaviconItem(name, birth, num, bookmark, null, bitmap, loginNum))
                            } else {
                                // 파일이 없을 경우 기본 이미지 설정
                                itemList.add(FaviconItem(name, birth, num, bookmark, R.drawable.dflt, null, loginNum))
                            }
                        } else {
                            // drawable 이미지 불러오기
                            val resId = resources.getIdentifier(photoFilename.substringBefore('.'), "drawable", requireContext().packageName)
                            if (resId != 0) {
                                itemList.add(FaviconItem(name, birth, num, bookmark, resId, null, loginNum))
                            } else {
                                // 이미지가 없는 경우 기본 이미지 표시
                                itemList.add(FaviconItem(name, birth, num, bookmark, R.drawable.dflt, null, loginNum))
                            }
                        }

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

                        // 사진 파일명 가져오기
                        val photoFilename = cursor.getString(cursor.getColumnIndex("h_photo"))

                        if (photoFilename.startsWith("/")) {
                            // 내부 저장소 경로에서 이미지 불러오기
                            val file = File(photoFilename)
                            if (file.exists()) {
                                // Bitmap으로 변환하여 ImageView에 설정
                                val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                                itemList.add(FaviconItem(name, birth, num, bookmark, null, bitmap, loginNum))
                            } else {
                                // 파일이 없을 경우 기본 이미지 설정
                                itemList.add(FaviconItem(name, birth, num, bookmark, R.drawable.dflt, null, loginNum))
                            }
                        } else {
                            // drawable 이미지 불러오기
                            val resId = resources.getIdentifier(photoFilename.substringBefore('.'), "drawable", requireContext().packageName)
                            if (resId != 0) {
                                itemList.add(FaviconItem(name, birth, num, bookmark, resId, null, loginNum))
                            } else {
                                // 이미지가 없는 경우 기본 이미지 표시
                                itemList.add(FaviconItem(name, birth, num, bookmark, R.drawable.dflt, null, loginNum))
                            }
                        }
                    } while (cursor.moveToNext())
                }
                cursor?.close()
            }


            binding.centerTextView.text = if (itemList.isEmpty()) "검색 결과가 없습니다." else "검색 결과: ${itemList.size}개"
            binding.centerTextView.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.VISIBLE

            binding.recyclerView.layoutManager = LinearLayoutManager(context)

            faviconAdapter = FaviconAdapter(requireContext(), itemList)

            binding.recyclerView.adapter = faviconAdapter

            sqlitedb.close()
        }

        binding.searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard()
                true
            } else {
                false
            }
        }

        dbManager.close()

        // 노숙인 추가 버튼 클릭 시 개인정보 수집 동의로 이동
        binding.addNewProfile.setOnClickListener {
            val agreeFragment = AgreeFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.rootLayout, agreeFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        return binding!!.root
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    // 키보드를 숨기는 함수
    private fun hideKeyboard() {
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 뒤로가기 버튼을 눌렀을 때 백스택을 비우고 DashboardFragment로 이동
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // 백스택을 전부 비움
                    parentFragmentManager.popBackStack(
                        null,
                        FragmentManager.POP_BACK_STACK_INCLUSIVE
                    )
                    // DashboardFragment로 이동
                    val parentActivity = activity as DashboardActivity
                    parentActivity.setFragment(DashboardFragment())
                }
            }
        )
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}
