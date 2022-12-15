package tw.edu.ntub.imd.birc.rxandmvvm.view.fragement

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tw.edu.ntub.imd.birc.rxandmvvm.R
import tw.edu.ntub.imd.birc.rxandmvvm.data.WaterRecord
import tw.edu.ntub.imd.birc.rxandmvvm.extension.attachToRecyclerView
import tw.edu.ntub.imd.birc.rxandmvvm.extension.mapSourceState
import tw.edu.ntub.imd.birc.rxandmvvm.view.activity.HomeActivity
import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.ObservableAdapter
import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.item.WaterRecordItem
import tw.edu.ntub.imd.birc.rxandmvvm.viewmodel.DietRecordViewModel
import tw.edu.ntub.imd.birc.rxandmvvm.viewmodel.WaterRecordViewModel
import java.text.SimpleDateFormat
import java.util.*


class CreateWaterRecordFragment : Fragment() {
    private val viewModel: WaterRecordViewModel by viewModel()
    private lateinit var createWaterRecordDate: EditText
    private lateinit var createWaterRecordVolume: EditText
    private lateinit var createWaterReturnBtn: ImageButton
    private lateinit var createWaterReturnCheck: ImageButton
    private val calender: Calendar = Calendar.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create_water_record, container, false)
        createWaterReturnBtn = view.findViewById<ImageButton>(R.id.create_water_record_return)
        createWaterRecordDate = view.findViewById(R.id.create_water_record_date)
        createWaterRecordVolume = view.findViewById(R.id.create_water_record_volume)
        createWaterReturnCheck = view.findViewById(R.id.create_water_record_check)
        this.getNowDateTime()
        createWaterRecordDate.setOnClickListener {
            this.datePicker()
        }
        createWaterReturnBtn.setOnClickListener {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.container_activity_main, HomeActivity.waterRecordFragment)
                ?.commit()
        }
        createWaterReturnCheck.setOnClickListener {
            val sharedPreference =
                this.activity?.getSharedPreferences("LOGIN", Context.MODE_PRIVATE)
            val account = sharedPreference?.getString("account", "defaultAccount")
            val waterVolume: Int = createWaterRecordVolume.text.toString().toInt()
            val waterTime: String = createWaterRecordDate.text.toString()
            val jsonObject = JSONObject()
            jsonObject.put("waterVolume", waterVolume)
            jsonObject.put("waterTime", waterTime)
            jsonObject.put("account", account)
            val jsonObjectString = jsonObject.toString()
            val requestBody =
                jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())
            viewModel.createWaterRecord(requestBody)
                .enqueue(object : Callback<WaterRecord> {
                    override fun onResponse(
                        call: Call<WaterRecord>,
                        response: Response<WaterRecord>
                    ) {
                        if (response.isSuccessful) {
                            Log.d("Retrofit", "Success")
                        }
                    }

                    override fun onFailure(call: Call<WaterRecord>, t: Throwable) {
                        Toast.makeText(context, "新增成功", Toast.LENGTH_SHORT)
                            .show()
                        val transaction = activity?.supportFragmentManager?.beginTransaction()
                        transaction?.replace(
                            R.id.container_activity_main,
                            HomeActivity.waterRecordFragment
                        )
                            ?.commit()
                        createWaterRecordVolume.setText("")
                    }

                })
            this.getNowDateTime()
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
                createWaterRecordDate.setText(dat)
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    private fun getNowDateTime() {
        val time = SimpleDateFormat("yyyy/MM/dd", Locale.TAIWAN)
        createWaterRecordDate.setText(time.format(Date()))
    }
}