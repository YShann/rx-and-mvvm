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
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DietRecordDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DietRecordDetailFragment(val dietRecord: DietRecord?) : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var dietRecordDetailArrow: ImageButton
    private lateinit var imageDietRecordDetail: ImageView
    private lateinit var detailName: TextView
    private lateinit var detailContent: TextView
    private lateinit var detailDateTime: TextView
    private lateinit var foodKind: TextView
    private lateinit var note: TextView
    private lateinit var editDetailName: AutoCompleteTextView
    private lateinit var editDetailContent: EditText
    private lateinit var editDetailDate: EditText
    private lateinit var editDetailNote: EditText
    private lateinit var dietRecordDetailEdit: ImageButton
    private lateinit var dietRecordDetailDelete: ImageButton
    private lateinit var cardViewDetailDietRecordGone: CardView
    private var isDetailEdit: Boolean = false
    private lateinit var dietRecordRecyclerInvisibility: RecyclerView
    private val foodViewModel: FoodViewModel by viewModel()
    private val viewModel: DietRecordViewModel by viewModel()
    private val calender: Calendar = Calendar.getInstance()
    private lateinit var checkBoxGrains: CheckBox
    private lateinit var checkBoxMeatsAndProtein: CheckBox
    private lateinit var checkBoxMilkAndDairy: CheckBox
    private lateinit var checkBoxFats: CheckBox
    private lateinit var checkBoxVegetables: CheckBox
    private lateinit var checkBoxFruits: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_diet_record_detail, container, false)
        dietRecordDetailArrow = view.findViewById(R.id.diet_record_detail_arrow)
        imageDietRecordDetail = view.findViewById(R.id.image_diet_record_detail)
        detailName = view.findViewById(R.id.text_diet_record_detail_name)
        detailContent = view.findViewById(R.id.text_diet_record_detail_content)
        detailDateTime = view.findViewById(R.id.text_diet_record_detail_dateTime)
        note = view.findViewById(R.id.text_diet_record_detail_note)
        editDetailName = view.findViewById(R.id.edit_record_detail_name)
        editDetailDate = view.findViewById(R.id.edit_record_detail_date)
        editDetailNote = view.findViewById(R.id.edit_record_detail_note)
        editDetailContent = view.findViewById(R.id.edit_record_detail_content)
        dietRecordDetailEdit = view.findViewById(R.id.diet_record_detail_edit)
        dietRecordDetailDelete = view.findViewById(R.id.diet_record_detail_delete)
        cardViewDetailDietRecordGone = view.findViewById(R.id.detail_diet_record_cardView_gone)
        checkBoxGrains = view.findViewById(R.id.checkBox_detail_record_grains)
        checkBoxFats = view.findViewById(R.id.checkBox_detail_record_fats)
        checkBoxMeatsAndProtein = view.findViewById(R.id.checkBox_detail_record_meatsAndProtein)
        checkBoxMilkAndDairy = view.findViewById(R.id.checkBox_detail_record_milkAndDairy)
        checkBoxVegetables = view.findViewById(R.id.checkBox_detail_record_vegetables)
        checkBoxFruits = view.findViewById(R.id.checkBox_detail_record_fruits)
        dietRecordRecyclerInvisibility = view.findViewById(R.id.diet_record_recycler_invisibility)

        dietRecordDetailDelete.setOnClickListener {
            val builderSingle = AlertDialog.Builder(context)
            builderSingle.setTitle("刪除紀錄")
                .setMessage("請確認是否要刪除這筆紀錄")
                .setPositiveButton("確定",
                    DialogInterface.OnClickListener { dialog, which ->
                        dialog.dismiss()
                        val id:String = dietRecord?.id.toString()
                        viewModel.deleteDietRecord(id)
                            .enqueue(object : Callback<DietRecord> {
                                override fun onResponse(
                                    call: Call<DietRecord>,
                                    response: Response<DietRecord>
                                ) {
                                    if (response.isSuccessful) {
                                        Log.d("Retrofit", "Success")
                                    }
                                }

                                override fun onFailure(call: Call<DietRecord>, t: Throwable) {
                                    Toast.makeText(context,"刪除成功",Toast. LENGTH_SHORT).show()
                                    val transaction = activity?.supportFragmentManager?.beginTransaction()
                                    transaction?.replace(R.id.container_activity_main, HomeActivity.recordFragment)
                                        ?.commit()
                                }

                            })
                    })
                .setNegativeButton("取消",
                    DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
            builderSingle.show()
        }

        editDetailName.visibility = View.INVISIBLE
        editDetailContent.visibility = View.INVISIBLE
        editDetailDate.visibility = View.INVISIBLE
        editDetailNote.visibility = View.INVISIBLE
        dietRecordRecyclerInvisibility.visibility = View.INVISIBLE
        checkBoxGrains.isEnabled = false
        checkBoxVegetables.isEnabled = false
        checkBoxFruits.isEnabled = false
        checkBoxMeatsAndProtein.isEnabled = false
        checkBoxMilkAndDairy.isEnabled = false
        checkBoxFats.isEnabled = false
        editDetailName.setText(dietRecord?.foodName)
        editDetailContent.setText(dietRecord?.foodContent)
        editDetailDate.setText(dietRecord?.mealDate?.plus(" ").plus(dietRecord?.mealTime?.substring(0 until 5)))
        editDetailNote.setText(dietRecord?.note)


        detailName.text = dietRecord?.foodName
        if(dietRecord?.foodContent?.isNotEmpty()!!){
            detailContent.text = dietRecord.foodContent
        }else{
            detailContent.text = "未填寫"
        }
        if(dietRecord.note?.isNotEmpty()!!){
            note.text = dietRecord.note
        }else{
            note.text = "未填寫"
        }
        detailDateTime.text = dietRecord.mealDate?.plus(" ").plus(dietRecord.mealTime?.substring(0 until 5))
        note.text = dietRecord.note
        if (dietRecord.grains == "1") {
            checkBoxGrains.isChecked=true
        }
        if (dietRecord.meatsAndProtein == "1") {
            checkBoxMeatsAndProtein.isChecked=true
        }
        if (dietRecord.vegetables == "1") {
            checkBoxVegetables.isChecked=true
        }
        if (dietRecord.fruits == "1") {
            checkBoxFruits.isChecked=true
        }
        if (dietRecord.milkAndDairy == "1") {
            checkBoxMilkAndDairy.isChecked=true
        }
        if (dietRecord.fats == "1") {
            checkBoxFats.isChecked=true
        }


        dietRecordDetailEdit.setBackgroundResource(R.drawable.ic_edit_img)
        dietRecordDetailEdit.setOnClickListener {
            if (isDetailEdit) {
                dietRecordDetailEdit.setBackgroundResource(R.drawable.ic_edit_img)
                isDetailEdit = false

                editDetailName.visibility = View.INVISIBLE
                editDetailContent.visibility = View.INVISIBLE
                editDetailDate.visibility = View.INVISIBLE
                editDetailNote.visibility = View.INVISIBLE
                detailName.visibility = View.VISIBLE
                detailContent.visibility = View.VISIBLE
                detailDateTime.visibility = View.VISIBLE
                note.visibility = View.VISIBLE
                checkBoxGrains.isEnabled = false
                checkBoxVegetables.isEnabled = false
                checkBoxFruits.isEnabled = false
                checkBoxMeatsAndProtein.isEnabled = false
                checkBoxMilkAndDairy.isEnabled = false
                checkBoxFats.isEnabled = false
                detailName.text = editDetailName.text
                if(editDetailContent.text.isNotEmpty()){
                    detailContent.text = editDetailContent.text
                }else{
                    detailContent.text = "未填寫"
                }
                detailDateTime.text = editDetailDate.text
                if(editDetailNote.text.isNotEmpty()){
                    note.text = editDetailContent.text
                }else{
                    note.text = "未填寫"
                }

                val dateTime = editDetailDate.text.toString().substring(0,10)
                val seconds = calender.get(Calendar.SECOND)
                val one = "1"
                val zero = "0"
                val id:String = dietRecord?.id.toString()
                val foodName:String = editDetailName.text.toString()
                val foodContent:String = editDetailContent.text.toString()
                val mealTime = "${editDetailDate.text.toString().substring(11,16)}:$seconds"
                val note:String = editDetailNote.text.toString()
                val grains = if (checkBoxGrains.isChecked) one else zero
                val vegetables = if (checkBoxVegetables.isChecked) one else zero
                val meatsAndProtein = if (checkBoxMeatsAndProtein.isChecked) one else zero
                val milkAndDairy = if (checkBoxMilkAndDairy.isChecked) one else zero
                val fruits = if (checkBoxFruits.isChecked) one else zero
                val fats = if (checkBoxFats.isChecked) one else zero
                val jsonObject = JSONObject()
                jsonObject.put("id", id)
                jsonObject.put("foodName", foodName)
                jsonObject.put("foodContent", foodContent)
                jsonObject.put("mealDate", dateTime)
                jsonObject.put("mealTime", mealTime)
                jsonObject.put("note", note)
                jsonObject.put("grains", grains)
                jsonObject.put("vegetables", vegetables)
                jsonObject.put("meatsAndProtein", meatsAndProtein)
                jsonObject.put("milkAndDairy", milkAndDairy)
                jsonObject.put("fruits", fruits)
                jsonObject.put("fats", fats)
                val jsonObjectString = jsonObject.toString()
                val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())
                viewModel.editDietRecord(requestBody)
                    .enqueue(object : Callback<DietRecord> {
                        override fun onResponse(
                            call: Call<DietRecord>,
                            response: Response<DietRecord>
                        ) {
                            if (response.isSuccessful) {
                                Log.d("Retrofit", "Success")
                            }
                        }

                        override fun onFailure(call: Call<DietRecord>, t: Throwable) {
                            Toast.makeText(context,"修改成功",Toast. LENGTH_SHORT).show()
                        }

                    })
            } else {
                dietRecordDetailEdit.setBackgroundResource(R.drawable.ic_diet_record_check)
                isDetailEdit = true

                checkBoxGrains.isEnabled = true
                checkBoxVegetables.isEnabled = true
                checkBoxFruits.isEnabled = true
                checkBoxMeatsAndProtein.isEnabled = true
                checkBoxMilkAndDairy.isEnabled = true
                checkBoxFats.isEnabled = true
                editDetailName.visibility = View.VISIBLE
                editDetailContent.visibility = View.VISIBLE
                editDetailDate.visibility = View.VISIBLE
                editDetailNote.visibility = View.VISIBLE
                detailName.visibility = View.INVISIBLE
                detailContent.visibility = View.INVISIBLE
                detailDateTime.visibility = View.INVISIBLE
                note.visibility = View.INVISIBLE
                editDetailName.setText(dietRecord.foodName)
                editDetailDate.setText(dietRecord.mealDate?.plus(" ").plus(dietRecord.mealTime?.substring(0 until 5)))
                if(dietRecord.foodContent!!.isNotEmpty()){
                    editDetailContent.setText(dietRecord.foodContent)
                }else{
                    editDetailContent.setText("未填寫")
                }
                if(dietRecord.note!!.isNotEmpty()){
                    editDetailNote.setText(dietRecord.note)
                }else{
                    editDetailNote.setText("未填寫")
                }

                val searchList: ArrayList<String> = ArrayList()
                val searchAdapter = ObservableAdapter(
                    foodViewModel.searchAll()
                        .mapSourceState {
                            it.data.map { food ->
                                activity?.runOnUiThread(java.lang.Runnable {
                                    searchList.add(food.name!!.plus(" ${food.energy}kcal"))
                                })
                            }
                            it.data.map { food ->
                                FoodItem(food)
                            }
                        }
                )
                searchAdapter.attachToRecyclerView(dietRecordRecyclerInvisibility)
                val arrayAdapter =
                    ArrayAdapter<String>(
                        requireActivity(),
                        android.R.layout.simple_dropdown_item_1line,
                        searchList
                    )
                editDetailName.setAdapter(arrayAdapter)
                editDetailName.setAdapter(arrayAdapter)
                editDetailName.threshold = 1
                editDetailName.onFocusChangeListener = View.OnFocusChangeListener { _, _ ->
                    editDetailName.setAdapter(arrayAdapter)
                    editDetailName.threshold = 1
                }

                editDetailName.setOnItemClickListener { _, _, _, _ ->
                    val searchName = editDetailName.text.split(" ")[0]
                    val searchNameAdapter = ObservableAdapter(
                        foodViewModel.getFood(searchName)
                            .mapSourceState {
                                it.data.map { food ->
                                    activity?.runOnUiThread(java.lang.Runnable {
                                        checkBoxGrains.isChecked = food.grains == "1"
                                        checkBoxVegetables.isChecked = food.vegetables == "1"
                                        checkBoxMeatsAndProtein.isChecked = food.meatsAndProtein == "1"
                                        checkBoxMilkAndDairy.isChecked = food.milkAndDairy == "1"
                                        checkBoxFruits.isChecked = food.fruits == "1"
                                        checkBoxFats.isChecked = food.fats == "1"
                                    })
                                }
                                it.data.map { food ->
                                    FoodItem(food)
                                }
                            }
                    )
                    searchNameAdapter.attachToRecyclerView(dietRecordRecyclerInvisibility)
                }
                editDetailDate.setOnClickListener {
                    this.datePicker()
                }
            }
        }

        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        var imageBitmap: Bitmap? = null
        executor.execute {
            val imageURL =
                dietRecord?.imageUrl!!.replace("http://localhost:8080/", UrlConstant.BASE_URL)

            try {
                val `in` = java.net.URL(imageURL).openStream()
                imageBitmap = BitmapFactory.decodeStream(`in`)

                handler.post {
                    imageDietRecordDetail.setImageBitmap(imageBitmap)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        dietRecordDetailArrow.setOnClickListener {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.container_activity_main, HomeActivity.recordFragment)
                ?.commit()
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
                editDetailDate.setText(dat)
                this.timePicker()
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
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


    private val timeListener = TimePickerDialog.OnTimeSetListener { _, hour, min ->
        calender.set(Calendar.HOUR_OF_DAY, hour)
        calender.set(Calendar.MINUTE, min)
        format("yyyy/MM/dd HH:mm", editDetailDate)
    }

    @SuppressLint("SetTextI18n")
    private fun format(format: String, view: View) {
        val time = SimpleDateFormat(format, Locale.TAIWAN)
        val text = editDetailDate.text.toString()
        (view as EditText).setText(text +" "+ time.format(calender.time).substring(11,16))
    }

}