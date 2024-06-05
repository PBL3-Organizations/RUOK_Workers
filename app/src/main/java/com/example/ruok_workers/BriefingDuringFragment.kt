// BriefingDuringFragment.kt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.example.ruok_workers.R
import java.text.SimpleDateFormat
import java.util.*

class BriefingDuringFragment : Fragment() {

    private val posts = ArrayList<String>()
    private lateinit var listView: ListView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_briefing_during, container, false)

        listView = view.findViewById<ListView>(R.id.list_view_posts)

        // 기존 게시글 목록
        posts.addAll(listOf(
            "게시글 1",
            "게시글 2",
            "게시글 3"
            // 더 많은 게시글을 추가
        ))

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, posts)
        listView.adapter = adapter

        val addPostButton = view.findViewById<Button>(R.id.btn_add_post2)
        addPostButton.setOnClickListener {
            // 새 글 작성 화면으로 이동
            val addPostFragment = BriefingAddFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, addPostFragment, "briefing_add")
                .addToBackStack(null)
                .commit()
        }

        return view
    }

    // 작성한 글을 목록에 추가하는 메서드
    fun addPost(newPost: String, belowThird: Boolean = false) {
        val currentTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
        val formattedPost = "$newPost - $currentTime"
        if (belowThird) {
            posts.add(posts.size - 1, formattedPost)
        } else {
            posts.add(formattedPost)
        }
        (listView.adapter as ArrayAdapter<*>).notifyDataSetChanged()
    }


}
