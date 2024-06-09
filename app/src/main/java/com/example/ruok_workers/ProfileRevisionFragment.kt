package com.example.ruok_workers

import ProfileDetailFragment
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileRevisionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileRevisionFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_revision, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // '수정하기' 버튼 찾기
        val editButton = view.findViewById<Button>(R.id.profile_revieion_ok)

        // OnClickListener 설정
        editButton.setOnClickListener {
            // ProfileDetailFragment.kt로 이동
            val fragment = ProfileDetailFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.rootLayout, fragment)
                .addToBackStack(null)
                .commit()
        }
    }


    fun onEditProfileClicked(view: View) {
        // ProfileDetailFragment.kt로 이동
        val fragment = ProfileDetailFragment()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.rootLayout, fragment)
            .addToBackStack(null)
            .commit()
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileRevisionFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileRevisionFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}