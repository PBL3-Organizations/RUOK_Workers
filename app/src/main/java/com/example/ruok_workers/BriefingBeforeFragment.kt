import android.annotation.SuppressLint
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ruok_workers.BriefingBeforeAdapter
import com.example.ruok_workers.BriefingBeforeCard
import com.example.ruok_workers.BriefingDetailFragment
import com.example.ruok_workers.CountingDetailFragment
import com.example.ruok_workers.CountingListAdapter
import com.example.ruok_workers.CountingListFragment
import com.example.ruok_workers.DBManager
import com.example.ruok_workers.DashboardActivity
import com.example.ruok_workers.R
import com.example.ruok_workers.databinding.FragmentBriefingBeforeBinding
import java.util.Vector

class BriefingBeforeFragment : Fragment() {

    private lateinit var dbManager: DBManager
    private lateinit var sqlitedb: SQLiteDatabase
    private lateinit var listView: ListView
    private lateinit var binding: FragmentBriefingBeforeBinding
    private lateinit var adapter: BriefingBeforeAdapter
    private lateinit var BriefingDetailFragment:BriefingDetailFragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBriefingBeforeBinding.inflate(inflater,container,false)
        dbManager = DBManager(requireContext(), "RUOKsample", null, 1)
        sqlitedb = dbManager.readableDatabase
//        displayBriefings()
        var briefingsBefore = Vector<BriefingBeforeCard>()

        //여기서 b_type 번호(0 - 후 / 1 - 전 / 2 - 중)
        val cursor: Cursor = sqlitedb.rawQuery("SELECT * FROM briefing WHERE b_type = 1 ORDER BY b_notice DESC, b_time DESC", null)
        lateinit var title:String
        lateinit var bTime:String
        while (cursor.moveToNext()) {
            title = cursor.getString(cursor.getColumnIndexOrThrow("b_title"))
            bTime = cursor.getString(cursor.getColumnIndexOrThrow("b_time"))
            val isNotice = cursor.getInt(cursor.getColumnIndexOrThrow("b_notice"))

            val displayTitle = if (isNotice == 1) {
                "\uD83D\uDD34 $title"  // Red circle emoji
            } else {
                title
            }
            briefingsBefore.add(BriefingBeforeCard(displayTitle,bTime))
            Log.i("DB", "briefingsBefore.add")

        }
        cursor.close()

        val layoutManager = LinearLayoutManager(context)
        binding!!.listViewBriefingBefore.layoutManager = layoutManager

        adapter = BriefingBeforeAdapter(requireContext(), briefingsBefore)
        binding!!.listViewBriefingBefore.adapter = adapter

        sqlitedb.close()
        dbManager.close()

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    @SuppressLint("Range")
    private fun displayBriefings() {


//        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, briefingsBefore)
//        listView.adapter = adapter
//        List.setOnItemClickListener { parent, view, position, id ->
//            Log.i("DB", "setOnItemClickListener")
//            com.example.ruok_workers.BriefingDetailFragment = BriefingDetailFragment()
//
//            var bundle = Bundle()
//            bundle.putString("b_title","$title")
//            bundle.putString("b_time", bTime)
//
//            com.example.ruok_workers.BriefingDetailFragment.arguments = bundle
//            (context as AppCompatActivity).supportFragmentManager.beginTransaction().replace(R.id.rootLayout,
//                com.example.ruok_workers.BriefingDetailFragment
//            ).commit()
//        }
    }
    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CountingListFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}
