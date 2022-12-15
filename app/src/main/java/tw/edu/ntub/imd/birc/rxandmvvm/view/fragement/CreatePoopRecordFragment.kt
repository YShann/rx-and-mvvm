package tw.edu.ntub.imd.birc.rxandmvvm.view.fragement

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tw.edu.ntub.imd.birc.rxandmvvm.R
import tw.edu.ntub.imd.birc.rxandmvvm.data.PoopRecord
import tw.edu.ntub.imd.birc.rxandmvvm.data.WaterRecord
import tw.edu.ntub.imd.birc.rxandmvvm.view.activity.HomeActivity
import tw.edu.ntub.imd.birc.rxandmvvm.viewmodel.PoopRecordViewModel
import tw.edu.ntub.imd.birc.rxandmvvm.viewmodel.WaterRecordViewModel
import java.text.SimpleDateFormat
import java.util.*

class CreatePoopRecordFragment : Fragment() {
    private val viewModel: PoopRecordViewModel by viewModel()
    private lateinit var createPoopRecordDate: EditText
    private lateinit var createPoopRecordCount: EditText
    private lateinit var createPoopReturnBtn: ImageButton
    private lateinit var createPoopReturnCheck: ImageButton
    private lateinit var createPoopRecordState: Spinner
    private val calender: Calendar = Calendar.getInstance()
    private var poopstatus: String = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create_poop_record, container, false)
        createPoopReturnBtn = view.findViewById(R.id.create_poop_record_return)
        createPoopRecordDate = view.findViewById(R.id.create_poop_record_date)
        createPoopReturnCheck = view.findViewById(R.id.create_poop_record_check)
        createPoopRecordState = view.findViewById(R.id.create_poop_record_state)
        createPoopRecordCount = view.findViewById(R.id.create_poop_record_volume)
        this.getNowDateTime()

        createPoopRecordDate.setOnClickListener {
            this.datePicker()
        }
        createPoopReturnBtn.setOnClickListener {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.container_activity_main, HomeActivity.poopRecordFragment)
                ?.commit()
        }
        createPoopReturnCheck.setOnClickListener {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.container_activity_main, HomeActivity.poopRecordFragment)
                ?.commit()
        }


        val adapter = activity?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.poop_state,
                android.R.layout.simple_spinner_item
            )
        }
        adapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        createPoopRecordState.adapter = adapter
        createPoopRecordState.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                println("error")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                poopstatus=position.toString()
            }

        }

        createPoopReturnCheck.setOnClickListener {
            val poopCount: Int = createPoopRecordCount.text.toString().toInt()
            val poopTime: String = createPoopRecordDate.text.toString()
            val jsonObject = JSONObject()
            jsonObject.put("poopCount", poopCount)
            jsonObject.put("poopTime", poopTime)
            jsonObject.put("poopStatus", poopstatus)
            val jsonObjectString = jsonObject.toString()
            val requestBody =
                jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())
            viewModel.createPoopRecord(requestBody)
                .enqueue(object : Callback<PoopRecord> {
                    override fun onResponse(
                        call: Call<PoopRecord>,
                        response: Response<PoopRecord>
                    ) {
                        if (response.isSuccessful) {
                            Log.d("Retrofit", "Success")
                        }
                    }

                    override fun onFailure(call: Call<PoopRecord>, t: Throwable) {
                        Toast.makeText(context, "新增成功", Toast.LENGTH_SHORT)
                            .show()
                        val transaction = activity?.supportFragmentManager?.beginTransaction()
                        transaction?.replace(
                            R.id.container_activity_main,
                            HomeActivity.poopRecordFragment
                        )
                            ?.commit()
                    }

                })

        }

        return view
    }

    private fun datePicker() {
        val year = calender.get(Calendar.YEAR)
        val month = calender.get(Calendar.MONTH)
        val day = calender.get(Calendar.DAY_OF_MONTH)
        var plusZero = "0"
        val datePickerDialog = DatePickerDialog(
            requireActivity(),
            { _, yearPick, monthOfYear, dayOfMonth ->
                plusZero = if (dayOfMonth < 10) {
                    plusZero.plus(dayOfMonth)
                } else {
                    dayOfMonth.toString()
                }
                val dat = (yearPick.toString() + "/" + (monthOfYear + 1) + "/" + plusZero)
                createPoopRecordDate.setText(dat)
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    private fun getNowDateTime() {
        val time = SimpleDateFormat("yyyy/MM/dd", Locale.TAIWAN)
        createPoopRecordDate.setText(time.format(Date()))
    }
}