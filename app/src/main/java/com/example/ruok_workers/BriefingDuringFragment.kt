import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.ruok_workers.R
import com.example.ruok_workers.BriefingDetailFragment
import com.example.ruok_workers.DBManager
import java.text.SimpleDateFormat
import java.util.*

class BriefingDuringFragment : Fragment() {

    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Fragment의 레이아웃을 인플레이트
        val view = inflater.inflate(R.layout.fragment_briefing_during, container, false)

        //데이터베이스 연동
        dbManager = DBManager(requireContext(), "RUOKsample", null, 1)
        dbManager.close()

        // 더미 데이터
        val dummyData = arrayOf("During 1", "아이템 2", "아이템 3", "아이템 4", "아이템 5")

        // ListView를 찾기
        val listView: ListView = view.findViewById(R.id.listView_briefing_during)

        // ArrayAdapter를 생성하여 ListView를 더미 데이터로 채우기
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, dummyData)

        // ListView에 어댑터를 설정
        listView.adapter = adapter

        // ListView의 항목을 클릭할 때 실행할 동작을 정의
        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            // 클릭된 항목의 위치(position)에 따라 Fragment 전환 코드를 작성
            when (position) {
                0 -> {
                    // BriefingDetailFragment와 연결
                    val fragment = BriefingDetailFragment()
                    val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
                    val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.rootLayout, fragment)
                    fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.commit()
                }
                // 다른 항목을 클릭했을 때 필요한 작업을 추가
            }
        }

        return view
    }
}
