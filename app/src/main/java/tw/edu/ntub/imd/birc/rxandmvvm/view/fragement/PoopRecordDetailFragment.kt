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
import tw.edu.ntub.imd.birc.rxandmvvm.data.PoopRecord
import tw.edu.ntub.imd.birc.rxandmvvm.data.WaterRecord
import tw.edu.ntub.imd.birc.rxandmvvm.extension.attachToRecyclerView
import tw.edu.ntub.imd.birc.rxandmvvm.extension.mapSourceState
import tw.edu.ntub.imd.birc.rxandmvvm.view.activity.HomeActivity
import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.ObservableAdapter
import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.item.FoodItem
import tw.edu.ntub.imd.birc.rxandmvvm.viewmodel.DietRecordViewModel
import tw.edu.ntub.imd.birc.rxandmvvm.viewmodel.FoodViewModel
import tw.edu.ntub.imd.birc.rxandmvvm.viewmodel.PoopRecordViewModel
import tw.edu.ntub.imd.birc.rxandmvvm.viewmodel.WaterRecordViewModel
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors



class PoopRecordDetailFragment(val poopRecord: PoopRecord?) : Fragment() {
    private lateinit var poopRecordDetailArrow: ImageButton
    private lateinit var poopRecordDetailCheck: ImageButton
    private lateinit var poopRecordDetailDelete: ImageButton
    private lateinit var detailDate: TextView
    private lateinit var detailCount: TextView
    private lateinit var detailStatus: TextView
    private lateinit var editDetailCount: EditText
    private lateinit var poopRecordRecyclerInvisibility: RecyclerView
    private lateinit var spinnerPoopRecordStatus: Spinner
    private var isDetailEdit: Boolean = false
    private var statusPosition: Int = poopRecord?.poopStatus?.toInt()!!
    private val viewModel: PoopRecordViewModel by viewModel()
    private val poopStatusList: MutableList<String> =
        mutableListOf("硬球狀(便祕型)", "有凹凸長條狀(便祕型)", "有裂痕長條狀(正常型)", "光滑的長條狀(正常型)", "鬆軟的顆粒狀(腹瀉型)", "軟泥狀(腹瀉型)", "水狀(腹瀉型)")

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
        val view = inflater.inflate(R.layout.fragment_poop_record_detail, container, false)
        poopRecordDetailArrow = view.findViewById(R.id.detail_poop_record_return)
        poopRecordDetailCheck = view.findViewById(R.id.detail_poop_record_check)
        poopRecordDetailDelete = view.findViewById(R.id.poop_record_detail_delete)
        detailDate = view.findViewById(R.id.detail_poop_record_date)
        detailCount = view.findViewById(R.id.detail_poop_record_count)
        detailStatus = view.findViewById(R.id.detail_poop_record_status)
        editDetailCount = view.findViewById(R.id.edit_poop_record_count)
        poopRecordRecyclerInvisibility = view.findViewById(R.id.poop_detail_recycler_invisibility)
        spinnerPoopRecordStatus = view.findViewById(R.id.spinner_poop_record_status)

        val spinnerAdapter = this.let {
            ArrayAdapter.createFromResource(
                requireActivity(),
                R.array.poop_state,
                android.R.layout.simple_spinner_item
            )
        }
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPoopRecordStatus.adapter = spinnerAdapter
        spinnerPoopRecordStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                statusPosition=position
            }

        }

        poopRecordDetailDelete.setOnClickListener {
            val builderSingle = AlertDialog.Builder(context)
            builderSingle.setTitle("刪除紀錄")
                .setMessage("請確認是否要刪除這筆紀錄")
                .setPositiveButton("確定",
                    DialogInterface.OnClickListener { dialog, which ->
                        dialog.dismiss()
                        val id:String = poopRecord?.id.toString()
                        viewModel.deletePoopRecord(id)
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
                                    Toast.makeText(context,"刪除成功",Toast. LENGTH_SHORT).show()
                                    val transaction = activity?.supportFragmentManager?.beginTransaction()
                                    transaction?.replace(R.id.container_activity_main, HomeActivity.poopRecordFragment)
                                        ?.commit()
                                }

                            })
                    })
                .setNegativeButton("取消",
                    DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
            builderSingle.show()
        }

        editDetailCount.visibility = View.INVISIBLE
        spinnerPoopRecordStatus.visibility = View.INVISIBLE
        poopRecordRecyclerInvisibility.visibility = View.INVISIBLE
        editDetailCount.setText(poopRecord?.poopCount.toString())
        spinnerPoopRecordStatus.setSelection(statusPosition)

        detailDate.text = poopRecord?.poopTime
        detailCount.text = poopRecord?.poopCount.toString()
        detailStatus.text =  poopStatusList[statusPosition]


        poopRecordDetailCheck.setBackgroundResource(R.drawable.ic_edit_img)
        poopRecordDetailCheck.setOnClickListener {
            if (isDetailEdit) {
                poopRecordDetailCheck.setBackgroundResource(R.drawable.ic_edit_img)
                isDetailEdit = false

                detailCount.text = editDetailCount.text
                detailStatus.text =  poopStatusList[statusPosition]
                detailCount.visibility = View.VISIBLE
                detailStatus.visibility = View.VISIBLE
                editDetailCount.visibility = View.INVISIBLE
                spinnerPoopRecordStatus.visibility = View.INVISIBLE
                val poopTime = poopRecord?.poopTime
                val poopCount = editDetailCount.text.toString()
                val poopStatus = statusPosition.toString()
                val id:String = poopRecord?.id.toString()

                val jsonObject = JSONObject()
                jsonObject.put("id", id)
                jsonObject.put("poopTime", poopTime)
                jsonObject.put("poopCount", poopCount)
                jsonObject.put("poopStatus", poopStatus)
                val jsonObjectString = jsonObject.toString()
                val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())
                viewModel.editPoopRecord(requestBody)
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
                            Toast.makeText(context,"修改成功",Toast. LENGTH_SHORT).show()
                        }

                    })
            } else {
                poopRecordDetailCheck.setBackgroundResource(R.drawable.ic_diet_record_check)
                isDetailEdit = true

                editDetailCount.visibility = View.VISIBLE
                spinnerPoopRecordStatus.visibility = View.VISIBLE
                detailCount.visibility = View.INVISIBLE
                detailStatus.visibility = View.INVISIBLE
                editDetailCount.setText(poopRecord?.poopCount.toString())
                spinnerPoopRecordStatus.setSelection(statusPosition)

            }
        }

        poopRecordDetailArrow.setOnClickListener {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.container_activity_main, HomeActivity.poopRecordFragment)
                ?.commit()
        }


        return view
    }


}