import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.ruok_workers.R

class BriefingAddFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_briefing_add, container, false)

        val submitButton = view.findViewById<Button>(R.id.button_submit_briefing)
        val setAsNoticeCheckbox = view.findViewById<CheckBox>(R.id.checkbox_set_as_notice)
        val titleEditText = view.findViewById<EditText>(R.id.editText_briefing_title)
        val contentEditText = view.findViewById<EditText>(R.id.editText_briefing_content)

        submitButton.setOnClickListener {
            // Check if the checkbox is checked
            val isSetAsNotice = setAsNoticeCheckbox.isChecked

            // Get title and content from EditText fields
            val title = titleEditText.text.toString()
            val content = contentEditText.text.toString()

            // Save or handle the data according to the checkbox state and other conditions

            // For now, just hide the fragment
            parentFragmentManager.popBackStack()
        }

        return view
    }
}
