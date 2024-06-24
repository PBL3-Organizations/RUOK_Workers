import android.annotation.SuppressLint
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.example.ruok_workers.BriefingDetailFragment
import com.example.ruok_workers.DBManager
import com.example.ruok_workers.DashboardActivity
import com.example.ruok_workers.R

class BriefingBeforeFragment : Fragment() {

    private lateinit var dbManager: DBManager
    private lateinit var sqlitedb: SQLiteDatabase
    private lateinit var listView: ListView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_briefing_before, container, false)
        listView = view.findViewById(R.id.listView_briefing_before)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dbManager = DBManager(requireContext(), "RUOKsample", null, 1)
        sqlitedb = dbManager.readableDatabase
        displayBriefings()

    }

    @SuppressLint("Range")
    private fun displayBriefings() {
        val briefingsBefore = mutableListOf<String>()

        //여기서 b_type 번호(0 - 후 / 1 - 전 / 2 - 중)
        val cursor: Cursor = sqlitedb.rawQuery("SELECT * FROM briefing WHERE b_type = 1 ORDER BY b_notice DESC, b_time DESC", null)

        while (cursor.moveToNext()) {
            val title = cursor.getString(cursor.getColumnIndex("b_title"))
            val bTime = cursor.getString(cursor.getColumnIndex("b_time"))
            val isNotice = cursor.getInt(cursor.getColumnIndex("b_notice"))

            val displayTitle = if (isNotice == 1) {
                "\uD83D\uDD34 $title"  // Red circle emoji
            } else {
                title
            }
            briefingsBefore.add("$displayTitle - $bTime")
        }

        cursor.close()
        dbManager.close()

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, briefingsBefore)
        listView.adapter = adapter
        //listView선택시 BriefingDetailFragment로 이동
        listView.setOnItemClickListener { parent, view, position, id ->
            val parentActivity = activity as DashboardActivity
            parentActivity.setFragment(BriefingDetailFragment())
        }
    }
}
