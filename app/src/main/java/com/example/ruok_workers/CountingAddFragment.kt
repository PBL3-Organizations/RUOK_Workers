package com.example.ruok_workers

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ruok_workers.databinding.FragmentCountingAddBinding
import com.example.ruok_workers.databinding.FragmentDashboardBinding
import com.example.ruok_workers.databinding.FragmentLocationAddBinding


class CountingAddFragment : Fragment() {
    lateinit var binding: FragmentCountingAddBinding

    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    var loginNum: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCountingAddBinding.inflate(inflater, container, false)

        //기존 로그인 정보 가져오기
        loginNum = arguments?.getInt("m_num")!!

        //데이터베이스 연동
        dbManager = DBManager(requireContext(), "RUOKsample", null, 1)
        dbManager.close()

        // 라디오 버튼 클릭 리스너 설정
        binding.radioButton1.setOnClickListener { onRadioButtonClicked(it) }
        binding.radioButton2.setOnClickListener { onRadioButtonClicked(it) }
        binding.radioButton3.setOnClickListener { onRadioButtonClicked(it) }
        binding.radioButton4.setOnClickListener { onRadioButtonClicked(it) }

        // btnCompleteCountingAdd 클릭시 CountingAddFragment에서 CountingTableFragment로 이동
        binding.btnCountingAdd.setOnClickListener {
            val selectedCourse = when {
                binding.radioButton1.isChecked -> binding.radioButton1.text.toString()
                binding.radioButton2.isChecked -> binding.radioButton2.text.toString()
                binding.radioButton3.isChecked -> binding.radioButton3.text.toString()
                binding.radioButton4.isChecked -> binding.radioButton4.text.toString()
                else -> ""
            }

            val selectedLocations = mutableListOf<String>()
            if (binding.checkBox11.isChecked) selectedLocations.add(binding.checkBox11.text.toString())
            if (binding.checkBox12.isChecked) selectedLocations.add(binding.checkBox12.text.toString())
            if (binding.checkBox13.isChecked) selectedLocations.add(binding.checkBox13.text.toString())
            if (binding.checkBox14.isChecked) selectedLocations.add(binding.checkBox14.text.toString())
            if (binding.checkBox15.isChecked) selectedLocations.add(binding.checkBox15.text.toString())
            if (binding.checkBox21.isChecked) selectedLocations.add(binding.checkBox21.text.toString())
            if (binding.checkBox22.isChecked) selectedLocations.add(binding.checkBox22.text.toString())
            if (binding.checkBox23.isChecked) selectedLocations.add(binding.checkBox23.text.toString())
            if (binding.checkBox24.isChecked) selectedLocations.add(binding.checkBox24.text.toString())
            if (binding.checkBox25.isChecked) selectedLocations.add(binding.checkBox25.text.toString())
            if (binding.checkBox26.isChecked) selectedLocations.add(binding.checkBox26.text.toString())
            if (binding.checkBox31.isChecked) selectedLocations.add(binding.checkBox31.text.toString())
            if (binding.checkBox32.isChecked) selectedLocations.add(binding.checkBox32.text.toString())
            if (binding.checkBox33.isChecked) selectedLocations.add(binding.checkBox33.text.toString())
            if (binding.checkBox34.isChecked) selectedLocations.add(binding.checkBox34.text.toString())
            if (binding.checkBox35.isChecked) selectedLocations.add(binding.checkBox35.text.toString())
            if (binding.checkBox36.isChecked) selectedLocations.add(binding.checkBox36.text.toString())
            if (binding.checkBox37.isChecked) selectedLocations.add(binding.checkBox37.text.toString())
            if (binding.checkBox38.isChecked) selectedLocations.add(binding.checkBox38.text.toString())
            if (binding.checkBox41.isChecked) selectedLocations.add(binding.checkBox41.text.toString())
            if (binding.checkBox42.isChecked) selectedLocations.add(binding.checkBox42.text.toString())
            if (binding.checkBox43.isChecked) selectedLocations.add(binding.checkBox43.text.toString())
            if (binding.checkBox44.isChecked) selectedLocations.add(binding.checkBox44.text.toString())
            if (binding.checkBox45.isChecked) selectedLocations.add(binding.checkBox45.text.toString())

            val bundle = Bundle()
            bundle.putString("course", selectedCourse)
            bundle.putStringArrayList("locations", ArrayList(selectedLocations))

            val countingTableFragment = CountingTableFragment()
            countingTableFragment.arguments = bundle
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.rootLayout, countingTableFragment).commit()
        }

        return binding.root
    }

    private fun onRadioButtonClicked(view: View) {
        binding.checkBox11.visibility = View.GONE
        binding.checkBox12.visibility = View.GONE
        binding.checkBox13.visibility = View.GONE
        binding.checkBox14.visibility = View.GONE
        binding.checkBox15.visibility = View.GONE
        binding.checkBox21.visibility = View.GONE
        binding.checkBox22.visibility = View.GONE
        binding.checkBox23.visibility = View.GONE
        binding.checkBox24.visibility = View.GONE
        binding.checkBox25.visibility = View.GONE
        binding.checkBox26.visibility = View.GONE
        binding.checkBox31.visibility = View.GONE
        binding.checkBox32.visibility = View.GONE
        binding.checkBox33.visibility = View.GONE
        binding.checkBox34.visibility = View.GONE
        binding.checkBox35.visibility = View.GONE
        binding.checkBox36.visibility = View.GONE
        binding.checkBox37.visibility = View.GONE
        binding.checkBox38.visibility = View.GONE
        binding.checkBox41.visibility = View.GONE
        binding.checkBox42.visibility = View.GONE
        binding.checkBox43.visibility = View.GONE
        binding.checkBox44.visibility = View.GONE
        binding.checkBox45.visibility = View.GONE

        when (view.id) {
            R.id.radioButton1 -> {
                binding.checkBox11.visibility = View.VISIBLE
                binding.checkBox12.visibility = View.VISIBLE
                binding.checkBox13.visibility = View.VISIBLE
                binding.checkBox14.visibility = View.VISIBLE
                binding.checkBox15.visibility = View.VISIBLE
                binding.checkBox21.visibility = View.GONE
                binding.checkBox22.visibility = View.GONE
                binding.checkBox23.visibility = View.GONE
                binding.checkBox24.visibility = View.GONE
                binding.checkBox25.visibility = View.GONE
                binding.checkBox26.visibility = View.GONE
                binding.checkBox31.visibility = View.GONE
                binding.checkBox32.visibility = View.GONE
                binding.checkBox33.visibility = View.GONE
                binding.checkBox34.visibility = View.GONE
                binding.checkBox35.visibility = View.GONE
                binding.checkBox36.visibility = View.GONE
                binding.checkBox37.visibility = View.GONE
                binding.checkBox38.visibility = View.GONE
                binding.checkBox41.visibility = View.GONE
                binding.checkBox42.visibility = View.GONE
                binding.checkBox43.visibility = View.GONE
                binding.checkBox44.visibility = View.GONE
                binding.checkBox45.visibility = View.GONE
            }
            R.id.radioButton2 -> {
                binding.checkBox11.visibility = View.GONE
                binding.checkBox12.visibility = View.GONE
                binding.checkBox13.visibility = View.GONE
                binding.checkBox14.visibility = View.GONE
                binding.checkBox15.visibility = View.GONE
                binding.checkBox21.visibility = View.VISIBLE
                binding.checkBox22.visibility = View.VISIBLE
                binding.checkBox23.visibility = View.VISIBLE
                binding.checkBox24.visibility = View.VISIBLE
                binding.checkBox25.visibility = View.VISIBLE
                binding.checkBox26.visibility = View.VISIBLE
                binding.checkBox31.visibility = View.GONE
                binding.checkBox32.visibility = View.GONE
                binding.checkBox33.visibility = View.GONE
                binding.checkBox34.visibility = View.GONE
                binding.checkBox35.visibility = View.GONE
                binding.checkBox36.visibility = View.GONE
                binding.checkBox37.visibility = View.GONE
                binding.checkBox38.visibility = View.GONE
                binding.checkBox41.visibility = View.GONE
                binding.checkBox42.visibility = View.GONE
                binding.checkBox43.visibility = View.GONE
                binding.checkBox44.visibility = View.GONE
                binding.checkBox45.visibility = View.GONE
            }
            R.id.radioButton3 -> {
                binding.checkBox11.visibility = View.GONE
                binding.checkBox12.visibility = View.GONE
                binding.checkBox13.visibility = View.GONE
                binding.checkBox14.visibility = View.GONE
                binding.checkBox15.visibility = View.GONE
                binding.checkBox21.visibility = View.GONE
                binding.checkBox22.visibility = View.GONE
                binding.checkBox23.visibility = View.GONE
                binding.checkBox24.visibility = View.GONE
                binding.checkBox25.visibility = View.GONE
                binding.checkBox26.visibility = View.GONE
                binding.checkBox31.visibility = View.VISIBLE
                binding.checkBox32.visibility = View.VISIBLE
                binding.checkBox33.visibility = View.VISIBLE
                binding.checkBox34.visibility = View.VISIBLE
                binding.checkBox35.visibility = View.VISIBLE
                binding.checkBox36.visibility = View.VISIBLE
                binding.checkBox37.visibility = View.VISIBLE
                binding.checkBox38.visibility = View.VISIBLE
                binding.checkBox41.visibility = View.GONE
                binding.checkBox42.visibility = View.GONE
                binding.checkBox43.visibility = View.GONE
                binding.checkBox44.visibility = View.GONE
                binding.checkBox45.visibility = View.GONE
            }
            R.id.radioButton4 -> {
                binding.checkBox11.visibility = View.GONE
                binding.checkBox12.visibility = View.GONE
                binding.checkBox13.visibility = View.GONE
                binding.checkBox14.visibility = View.GONE
                binding.checkBox15.visibility = View.GONE
                binding.checkBox21.visibility = View.GONE
                binding.checkBox22.visibility = View.GONE
                binding.checkBox23.visibility = View.GONE
                binding.checkBox24.visibility = View.GONE
                binding.checkBox25.visibility = View.GONE
                binding.checkBox26.visibility = View.GONE
                binding.checkBox31.visibility = View.GONE
                binding.checkBox32.visibility = View.GONE
                binding.checkBox33.visibility = View.GONE
                binding.checkBox34.visibility = View.GONE
                binding.checkBox35.visibility = View.GONE
                binding.checkBox36.visibility = View.GONE
                binding.checkBox37.visibility = View.GONE
                binding.checkBox38.visibility = View.GONE
                binding.checkBox41.visibility = View.VISIBLE
                binding.checkBox42.visibility = View.VISIBLE
                binding.checkBox43.visibility = View.VISIBLE
                binding.checkBox44.visibility = View.VISIBLE
                binding.checkBox45.visibility = View.VISIBLE
            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CountingAddFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}