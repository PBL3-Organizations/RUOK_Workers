import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.example.ruok_workers.R

class BriefingDuringFragment : Fragment() {

    private val posts = ArrayList<String>()
    private lateinit var listView: ListView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_briefing_during, container, false)

        // 게시글 목록을 나타내는 ListView 찾기
        listView = view.findViewById<ListView>(R.id.list_view_posts)

        // 더미 데이터 생성
        posts.addAll(listOf(
            "게시글 1",
            "게시글 2",
            "게시글 3"
            // 더 많은 게시글을 추가
        ))

        // ArrayAdapter를 사용하여 ListView에 데이터 설정
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, posts)
        listView.adapter = adapter


        return view
    }

    // 새 게시글을 추가하는 메서드
    fun addPost(newPost: String) {
        posts.add(newPost)
        // Adapter에 데이터 변경을 알림
        (listView.adapter as ArrayAdapter<*>).notifyDataSetChanged()
    }

    fun addPostBelowThird(newPost: String) {
        // 가장 최신 게시물 아래에 새 게시물을 추가
        posts.add(posts.size - 1, newPost)
        // 데이터 변경을 어댑터에 알림
        (listView.adapter as ArrayAdapter<*>).notifyDataSetChanged()
    }


}
