package tw.edu.ntub.imd.birc.rxandmvvm.view.fragement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import tw.edu.ntub.imd.birc.rxandmvvm.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RecordFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RecordFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

//    private lateinit var dateEdit: EditText
//    private lateinit var spinner : Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        //val adapter = ArrayAdapter.createFromResource(this, R.array.lunch, android.R.layout.simple_spinner_dropdown_item)
        //spinner.adapter = adapter

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_record, container, false)
//        val root = inflater.inflate(R.layout.fragment_record, container, false)
//        dateEdit = root.findViewById(R.id.date_edit)
//        dateEdit.setOnClickListener(dlistener)
//
//        spinner = root.findViewById(R.id.spinner)
//        // Create an ArrayAdapter using the string array and a default spinner layout
//        ArrayAdapter.createFromResource(
//            this.requireContext(),
//            R.array.lunch, android.R.layout.simple_spinner_dropdown_item
//        ).also { adapter ->
//            // Specify the layout to use when the list of choices appears
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//            // Apply the adapter to the spinner
//            spinner.adapter = adapter
//        }
//
//        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
//                //return Toast.makeText(this, "你選的是" + lunch[pos], Toast.LENGTH_SHORT).show()
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>) {}
//        }
//
//        return root
    }


//    private val calender: Calendar = Calendar.getInstance()
//    private val dlistener = View.OnClickListener {
//
//        when (it) {
//
//            dateEdit -> datePicker()
//
//        }
//    }
//
//    private fun datePicker() {
//        DatePickerDialog(
//            this.requireContext(),
//            dateListener,
//            calender.get(Calendar.YEAR),
//            calender.get(Calendar.MONTH),
//            calender.get(Calendar.DAY_OF_MONTH)).show()
//    }
//
//    private val dateListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
//        calender.set(year, month, day)
//        format("yyyy / MM / dd", dateEdit)
//    }
//
//    private fun format(format: String, view: View) {
//        val time = SimpleDateFormat(format, Locale.TAIWAN)
//        (view as EditText).setText(time.format(calender.time))
//    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RecordFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RecordFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}