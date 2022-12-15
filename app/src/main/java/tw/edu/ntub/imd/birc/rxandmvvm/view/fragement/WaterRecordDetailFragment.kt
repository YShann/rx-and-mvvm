package tw.edu.ntub.imd.birc.rxandmvvm.view.fragement

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
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
import tw.edu.ntub.imd.birc.rxandmvvm.constant.UrlConstant
import tw.edu.ntub.imd.birc.rxandmvvm.data.DietRecord
import tw.edu.ntub.imd.birc.rxandmvvm.data.WaterRecord
import tw.edu.ntub.imd.birc.rxandmvvm.extension.attachToRecyclerView
import tw.edu.ntub.imd.birc.rxandmvvm.extension.mapSourceState
import tw.edu.ntub.imd.birc.rxandmvvm.view.activity.HomeActivity
import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.ObservableAdapter
import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.item.FoodItem
import tw.edu.ntub.imd.birc.rxandmvvm.viewmodel.DietRecordViewModel
import tw.edu.ntub.imd.birc.rxandmvvm.viewmodel.FoodViewModel
import tw.edu.ntub.imd.birc.rxandmvvm.viewmodel.WaterRecordViewModel
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors



class WaterRecordDetailFragment(val waterRecord: WaterRecord?) : Fragment() {
    private lateinit var waterRecordDetailArrow: ImageButton
    private lateinit var waterRecordDetailCheck: ImageButton
    private lateinit var waterRecordDetailDelete: ImageButton
    private lateinit var detailDate: TextView
    private lateinit var detailVolume: TextView
    private lateinit var editDetailVolume: EditText
    private lateinit var waterRecordRecyclerInvisibility: RecyclerView
    private var isDetailEdit: Boolean = false
    private val viewModel: WaterRecordViewModel by viewModel()
    private val calender: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_water_record_detail, container, false)
        waterRecordDetailArrow = view.findViewById(R.id.detail_water_record_return)
        waterRecordDetailCheck = view.findViewById(R.id.detail_water_record_check)
        waterRecordDetailDelete = view.findViewById(R.id.water_record_detail_delete)
        detailDate = view.findViewById(R.id.detail_water_record_date)
        detailVolume = view.findViewById(R.id.detail_water_record_volume)
        editDetailVolume = view.findViewById(R.id.edit_water_record_volume)
        waterRecordRecyclerInvisibility = view.findViewById(R.id.water_detail_recycler_invisibility)

        waterRecordDetailDelete.setOnClickListener {
            val builderSingle = AlertDialog.Builder(context)
            builderSingle.setTitle("刪除紀錄")
                .setMessage("請確認是否要刪除這筆紀錄")
                .setPositiveButton("確定",
                    DialogInterface.OnClickListener { dialog, which ->
                        dialog.dismiss()
                        val id:String = waterRecord?.id.toString()
                        viewModel.deleteWaterRecord(id)
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
                                    Toast.makeText(context,"刪除成功",Toast. LENGTH_SHORT).show()
                                    val transaction = activity?.supportFragmentManager?.beginTransaction()
                                    transaction?.replace(R.id.container_activity_main, HomeActivity.waterRecordFragment)
                                        ?.commit()
                                }

                            })
                    })
                .setNegativeButton("取消",
                    DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
            builderSingle.show()
        }

        editDetailVolume.visibility = View.INVISIBLE
        waterRecordRecyclerInvisibility.visibility = View.INVISIBLE
        editDetailVolume.setText(waterRecord?.waterVolume.toString())


        detailDate.text = waterRecord?.waterTime
        detailVolume.text = waterRecord?.waterVolume.toString()


        waterRecordDetailCheck.setBackgroundResource(R.drawable.ic_edit_img)
        waterRecordDetailCheck.setOnClickListener {
            if (isDetailEdit) {
                waterRecordDetailCheck.setBackgroundResource(R.drawable.ic_edit_img)
                isDetailEdit = false

                detailVolume.text = editDetailVolume.text
                detailVolume.visibility = View.VISIBLE
                editDetailVolume.visibility = View.INVISIBLE
                val waterTime = waterRecord?.waterTime
                val waterVolume = editDetailVolume.text.toString()
                val id:String = waterRecord?.id.toString()

                val jsonObject = JSONObject()
                jsonObject.put("id", id)
                jsonObject.put("waterTime", waterTime)
                jsonObject.put("waterVolume", waterVolume)
                val jsonObjectString = jsonObject.toString()
                val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())
                viewModel.editWaterRecord(requestBody)
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
                            Toast.makeText(context,"修改成功",Toast. LENGTH_SHORT).show()
                        }

                    })
            } else {
                waterRecordDetailCheck.setBackgroundResource(R.drawable.ic_diet_record_check)
                isDetailEdit = true

                editDetailVolume.visibility = View.VISIBLE
                detailVolume.visibility = View.INVISIBLE
                editDetailVolume.setText(waterRecord?.waterVolume.toString())

            }
        }

        waterRecordDetailArrow.setOnClickListener {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.container_activity_main, HomeActivity.waterRecordFragment)
                ?.commit()
        }


        return view
    }


}