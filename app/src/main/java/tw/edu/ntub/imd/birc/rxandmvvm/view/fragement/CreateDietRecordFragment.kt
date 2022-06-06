package tw.edu.ntub.imd.birc.rxandmvvm.view.fragement

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tw.edu.ntub.imd.birc.rxandmvvm.R
import tw.edu.ntub.imd.birc.rxandmvvm.data.DietRecord
import tw.edu.ntub.imd.birc.rxandmvvm.view.activity.MainActivity
import tw.edu.ntub.imd.birc.rxandmvvm.viewmodel.DietRecordViewModel
import java.lang.Double.parseDouble
import java.lang.Integer.parseInt
import java.text.SimpleDateFormat
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CreateDietRecordFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreateDietRecordFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var editName: EditText
    private lateinit var editDateTime: EditText
    private lateinit var spinnerPortionSize: Spinner
    private lateinit var editNote: EditText
    private lateinit var checkBoxGrains: CheckBox
    private lateinit var checkBoxMeatsAndProtein: CheckBox
    private lateinit var checkBoxMilkAndDairy: CheckBox
    private lateinit var checkBoxFats: CheckBox
    private lateinit var checkBoxVegetables: CheckBox
    private lateinit var checkBoxFruits: CheckBox
    private lateinit var editEnergy: EditText
    private lateinit var editCarbohydrate: EditText
    private lateinit var editProtein: EditText
    private lateinit var editSaturatedFat: EditText
    private lateinit var editFat: EditText
    private lateinit var createDietRecordArrow: ImageButton
    private lateinit var createDietRecordFinish: ImageButton
    private val viewModel: DietRecordViewModel by viewModel()
    private val calender: Calendar = Calendar.getInstance()


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
        val view = inflater.inflate(R.layout.fragment_create_diet_record, container, false)
        editName = view.findViewById(R.id.edit_create_record_name)
        editDateTime = view.findViewById(R.id.edit_create_record_dateTime)
        spinnerPortionSize = view.findViewById(R.id.create_record_portionSize_spinner)
        editNote = view.findViewById(R.id.edit_create_record_note)
        checkBoxGrains = view.findViewById(R.id.checkBox_create_record_grains)
        checkBoxMeatsAndProtein = view.findViewById(R.id.checkBox_create_record_meatsAndProtein)
        checkBoxMilkAndDairy = view.findViewById(R.id.checkBox_create_record_milkAndDairy)
        checkBoxFats = view.findViewById(R.id.checkBox_create_record_fats)
        checkBoxVegetables = view.findViewById(R.id.checkBox_create_record_vegetables)
        checkBoxFruits = view.findViewById(R.id.checkBox_create_record_fruits)
        editEnergy = view.findViewById(R.id.edit_create_record_energy)
        editCarbohydrate = view.findViewById(R.id.edit_create_record_carbohydrate)
        editProtein = view.findViewById(R.id.edit_create_record_protein)
        editSaturatedFat = view.findViewById(R.id.edit_create_record_saturatedFat)
        editFat = view.findViewById(R.id.edit_create_record_fat)
        createDietRecordArrow = view.findViewById(R.id.create_diet_record_arrow)
        createDietRecordFinish = view.findViewById(R.id.create_diet_record_finish)
        this.getNowDateTime()
        editDateTime.setOnClickListener {
            this.datePicker()
        }

        val adapter = activity?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.create_record_portionSize_spinner_list,
                android.R.layout.simple_spinner_item
            )
        }
        adapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPortionSize.adapter = adapter
        spinnerPortionSize.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                println("error")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val type = parent?.getItemAtPosition(position).toString()
                println(type)
            }

        }

        createDietRecordArrow.setOnClickListener {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.container_activity_main, MainActivity.addFragment)
                ?.commit()
        }
        createDietRecordFinish.setOnClickListener {
            this.creatDietRecord()
        }

        return view
    }


    private fun creatDietRecord() {
        val dateTime = editDateTime.text.toString()
        val seconds = calender.get(Calendar.SECOND)
        val foodName = editName.text.toString()
        val portionSize = (spinnerPortionSize.selectedItemPosition - 1).toString()
        val mealTime = "$dateTime:$seconds"
        val note = editNote.text.toString()
        val energy = parseInt(editEnergy.text.toString())
        val fat = parseDouble(editFat.text.toString())
        val saturatedFat = parseDouble(editSaturatedFat.text.toString())
        val carbohydrate = parseDouble(editCarbohydrate.text.toString())
        val protein = parseDouble(editProtein.text.toString())
        val grains = if (checkBoxGrains.isChecked) "1" else "0"
        val vegetables = if (checkBoxVegetables.isChecked) "1" else "0"
        val meatsAndProtein = if (checkBoxMeatsAndProtein.isChecked) "1" else "0"
        val milkAndDairy = if (checkBoxMilkAndDairy.isChecked) "1" else "0"
        val fruits = if (checkBoxFruits.isChecked) "1" else "0"
        val fats = if (checkBoxFats.isChecked) "1" else "0"
        val data = DietRecord(
            foodName, portionSize, mealTime, note, energy, fat, saturatedFat, carbohydrate, protein,
            grains, vegetables, meatsAndProtein, milkAndDairy, fruits, fats,
        )
        Log.d("DateTime", "ddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd" + data)


        val jsonObject: JsonObject = Json.encodeToJsonElement(data).jsonObject
        viewModel.createDietRecord(jsonObject).enqueue(object : Callback<DietRecord> {
            override fun onResponse(call: Call<DietRecord>, response: Response<DietRecord>) {
                if (response.isSuccessful) {
                    Log.d("Retrofit", "ssssssssssssssssssssssssssssssssssssssssssssssssssssss")

                }
            }

            override fun onFailure(call: Call<DietRecord>, t: Throwable) {
                Log.d("Retrofit", "eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee")
                val transaction = activity?.supportFragmentManager?.beginTransaction()
                transaction?.replace(R.id.container_activity_main, MainActivity.addFragment)
                    ?.commit()
            }
        })
//                .mapSourceState { it.data.map { user -> UserItem(user) } }

        Log.d("Retrofit", "tttttttttttttttttttttttttttttttttttttttttttttt")
    }


    private fun datePicker() {
        val picker = context?.let {
            DatePickerDialog(
                it,
                dateListener,
                calender.get(Calendar.YEAR),
                calender.get(Calendar.MONTH),
                calender.get(Calendar.DATE)
            )
        }
        picker?.setCancelable(true)
        picker?.setCanceledOnTouchOutside(true)
        picker?.setButton(
            DialogInterface.BUTTON_POSITIVE, "確定"
        ) { date, which ->
            this.timePicker()
        }
        picker?.setButton(
            DialogInterface.BUTTON_NEGATIVE, "取消"
        ) { _, _ -> Log.d("Picker", "Cancel!") }
        picker?.show()
    }


    private fun timePicker() {
        val timePicker = context?.let {
            TimePickerDialog(
                it,
                timeListener,  // instead of a listener
                calender.get(Calendar.HOUR_OF_DAY), calender.get(Calendar.MINUTE), true
            )
        }
        timePicker?.setCancelable(true)
        timePicker?.setCanceledOnTouchOutside(true)
        timePicker?.show()
    }

    private val dateListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
        calender.set(year, month, day)
    }

    private val timeListener = TimePickerDialog.OnTimeSetListener { _, hour, min ->
        calender.set(Calendar.HOUR_OF_DAY, hour)
        calender.set(Calendar.MINUTE, min)
        format("yyyy/MM/dd HH:mm", editDateTime)
    }

    private fun format(format: String, view: View) {
        val time = SimpleDateFormat(format, Locale.TAIWAN)
        (view as EditText).setText(time.format(calender.time))
    }

    private fun getNowDateTime() {
        val time = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.TAIWAN)
        editDateTime.setText(time.format(Date()))
    }
    
}




