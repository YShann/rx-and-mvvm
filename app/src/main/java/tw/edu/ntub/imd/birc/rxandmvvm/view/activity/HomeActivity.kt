package tw.edu.ntub.imd.birc.rxandmvvm.view.activity

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.material.bottomnavigation.BottomNavigationView
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
import tw.edu.ntub.imd.birc.rxandmvvm.extension.attachToRecyclerView
import tw.edu.ntub.imd.birc.rxandmvvm.extension.mapSourceState
import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.ObservableAdapter
import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.item.HomeDietRecordItem
import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.item.PoopRecordItem
import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.item.WaterRecordItem
import tw.edu.ntub.imd.birc.rxandmvvm.view.fragement.*
import tw.edu.ntub.imd.birc.rxandmvvm.viewmodel.DietRecordViewModel
import tw.edu.ntub.imd.birc.rxandmvvm.viewmodel.PoopRecordViewModel
import tw.edu.ntub.imd.birc.rxandmvvm.viewmodel.WaterRecordViewModel
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.floor


class HomeActivity : AppCompatActivity() {
    private val viewModel: DietRecordViewModel by viewModel()
    private val waterViewModel: WaterRecordViewModel by viewModel()
    private val poopViewModel: PoopRecordViewModel by viewModel()
    private lateinit var navigationview: BottomNavigationView
    private lateinit var homeToolbarTitle: TextView
    private lateinit var pieChart: PieChart
    private lateinit var waterEditImgBtn: ImageButton
    private lateinit var waterEdit: EditText
    private lateinit var poopEdit: EditText
    private lateinit var homeFoodRecycler: RecyclerView
    private lateinit var homeFoodToDateBtn: ImageButton
    private lateinit var homeFoodToCreateBtn: ImageButton
    private lateinit var homeWaterToDateBtn: ImageButton
    private lateinit var homePoopToDateBtn: ImageButton
    private lateinit var homeSettingBtn: ImageButton
    private lateinit var homeRecyclerInvisibility: RecyclerView
    private lateinit var homePoopStatusSpinner: Spinner
    private var isWaterRecordCreate: Boolean = true
    private var isPoopRecordCreate: Boolean = true
    private lateinit var waterRecordId: String
    private lateinit var poopRecordId: String


    companion object {
        //原本是val recordFragment = RecordFragment()被警告有內存洩漏的問題，按照建議改成下面那行
        val recordFragment by lazy { RecordFragment() }
        val createDietRecordFragment by lazy { CreateDietRecordFragment() }
        val userFragemnt by lazy { UserFragment() }
        val foodFragemnt by lazy { FoodFragment() }
        val waterRecordFragment by lazy { WaterRecordFragment() }
        val poopRecordFragment by lazy { PoopRecordFragment() }
        val createWaterRecordFragment by lazy { CreateWaterRecordFragment() }
        val createPoopRecordFragment by lazy { CreatePoopRecordFragment() }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        homeToolbarTitle = findViewById(R.id.home_toolbar_title)
        pieChart = findViewById(R.id.home_pieChart)
        waterEdit = findViewById(R.id.home_water_editText)
        poopEdit = findViewById(R.id.home_poop_editText)
        homeFoodRecycler = findViewById(R.id.home_food_recycler)
        homeFoodToDateBtn = findViewById(R.id.home_food_toDate_btn)
        homeFoodToCreateBtn = findViewById(R.id.home_food_toCreate_btn)
        homeWaterToDateBtn = findViewById(R.id.home_water_toDate_btn)
        homePoopToDateBtn = findViewById(R.id.home_poop_toDate_btn)
        homeSettingBtn = findViewById(R.id.home_setting_btn)
        homeRecyclerInvisibility = findViewById(R.id.home_recycler_invisibility)
        homePoopStatusSpinner = findViewById(R.id.home_poop_status_spinner)


        val sharedPreference = this.getSharedPreferences("LOGIN", Context.MODE_PRIVATE)
        val account = sharedPreference?.getString("account", "defaultAccount")
        if (account == "defaultAccount") {
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
        } else {
            homeRecyclerInvisibility.visibility = View.INVISIBLE
            val spinnerAdapter = this.let {
                ArrayAdapter.createFromResource(
                    it,
                    R.array.poop_state,
                    android.R.layout.simple_spinner_item
                )
            }
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            homePoopStatusSpinner.adapter = spinnerAdapter
/////////////////////////////////////////////////////////////////////////////////////////////////////
            val date = getCurrentDateTime()
            val dateInString = date.toString("MM/dd")
            homeToolbarTitle.text =
                dateInString.replace("/", "月").plus("日（${date.getToday_Chinaname()}）")
/////////////////////////////////////////////////////////////////////////////////////////////////////
            val year = Calendar.getInstance().get(Calendar.YEAR)
            val month = Calendar.getInstance().get(Calendar.MONTH).plus(1)
            val day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            val mealDate = "${year}-${month}-${day}"
            var grainCount = 0
            var vegetableCount = 0
            var meatCount = 0
            var milkCount = 0
            var fruitCount = 0
            var fatCount = 0
            val adapter = ObservableAdapter(
                viewModel.searchByMealDate(mealDate, account.toString())
                    .mapSourceState {
                        it.data.map { dietRecord ->
                            if (dietRecord.grains == "1") {
                                grainCount += 1
                            }
                            if (dietRecord.vegetables == "1") {
                                vegetableCount += 1
                            }
                            if (dietRecord.meatsAndProtein == "1") {
                                meatCount += 1
                            }
                            if (dietRecord.milkAndDairy == "1") {
                                milkCount += 1
                            }
                            if (dietRecord.fruits == "1") {
                                fruitCount += 1
                            }
                            if (dietRecord.fats == "1") {
                                fatCount += 1
                            }
                        }
                        showPieChart(
                            pieChart,
                            grainCount,
                            vegetableCount,
                            meatCount,
                            milkCount,
                            fruitCount,
                            fatCount
                        )
                        it.data.map { dietRecord -> HomeDietRecordItem(dietRecord) }
                    }
            )
            adapter.attachToRecyclerView(homeFoodRecycler)
            val dividerItemDecoration: ItemDecoration =
                DividerItemDecorator(ContextCompat.getDrawable(this, R.drawable.divider))
            homeFoodRecycler.addItemDecoration(dividerItemDecoration)
            val waterAdapter = ObservableAdapter(
                waterViewModel.searchByWaterTime(mealDate, account.toString())
                    .mapSourceState {
                        it.data.map { waterRecord ->
                            waterEdit.setText(waterRecord.waterVolume.toString())
                            if (waterEdit.text.isNotEmpty()) {
                                isWaterRecordCreate = false
                                waterRecordId = waterRecord.id.toString()
                            }
                        }
                        it.data.map { waterRecord ->
                            WaterRecordItem(waterRecord)
                        }
                    }
            )
            waterAdapter.attachToRecyclerView(homeRecyclerInvisibility)

            val poopAdapter = ObservableAdapter(
                poopViewModel.searchByPoopTime(mealDate, account.toString())
                    .mapSourceState {
                        it.data.map { poopRecord ->
                            poopEdit.setText(poopRecord.poopCount.toString())
                            if (poopEdit.text.isNotEmpty()) {
                                isPoopRecordCreate = false
                                poopRecordId = poopRecord.id.toString()
                                poopEdit.setText(poopRecord.poopCount.toString())
                                this.runOnUiThread(java.lang.Runnable {
                                    homePoopStatusSpinner.setSelection(poopRecord.poopStatus?.toInt()!!)

                                })
                            }
                        }
                        it.data.map { poopRecord ->
                            PoopRecordItem(poopRecord)
                        }
                    }
            )
            poopAdapter.attachToRecyclerView(homeRecyclerInvisibility)

            homePoopStatusSpinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {

                    }

                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        try {
                            val poopCount: Int = poopEdit.text.toString().toInt()
                            val poopStatus: String = position.toString()
                            val jsonObject = JSONObject()
                            jsonObject.put("poopCount", poopCount)
                            jsonObject.put("poopStatus", poopStatus)
                            jsonObject.put("account", account)
                            if (isPoopRecordCreate) {
                                val jsonObjectString = jsonObject.toString()
                                val requestBody =
                                    jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())
                                poopViewModel.createPoopRecord(requestBody)
                                    .enqueue(object : Callback<PoopRecord> {
                                        override fun onResponse(
                                            call: Call<PoopRecord>,
                                            response: Response<PoopRecord>
                                        ) {
                                            if (response.isSuccessful) {
                                                Log.d("Retrofit", "Success")
                                            }
                                        }

                                        override fun onFailure(
                                            call: Call<PoopRecord>,
                                            t: Throwable
                                        ) {
                                            Toast.makeText(
                                                this@HomeActivity,
                                                "新增成功",
                                                Toast.LENGTH_SHORT
                                            )
                                                .show()
                                            isPoopRecordCreate = false
                                        }

                                    })
                            } else {
                                jsonObject.put("id", poopRecordId)
                                val jsonObjectString = jsonObject.toString()
                                val requestBody =
                                    jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())
                                poopViewModel.editPoopRecord(requestBody)
                                    .enqueue(object : Callback<PoopRecord> {
                                        override fun onResponse(
                                            call: Call<PoopRecord>,
                                            response: Response<PoopRecord>
                                        ) {
                                            if (response.isSuccessful) {
                                                Log.d("Retrofit", "Success")
                                            }
                                        }

                                        override fun onFailure(
                                            call: Call<PoopRecord>,
                                            t: Throwable
                                        ) {

                                        }

                                    })
                            }
                        } catch (e: Exception) {
                            print(e.stackTrace)
                        }
                    }

                }

            homeFoodToDateBtn.setOnClickListener {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container_activity_main, recordFragment)
                    .commit()
            }
            homeFoodToCreateBtn.setOnClickListener {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container_activity_main, createDietRecordFragment)
                    .commit()
            }
            homeWaterToDateBtn.setOnClickListener {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container_activity_main, waterRecordFragment)
                    .commit()
            }
            homePoopToDateBtn.setOnClickListener {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container_activity_main, poopRecordFragment)
                    .commit()
            }
/////////////////////////////////////////////////////////////////////////////////////////////////////
            homeSettingBtn.setOnClickListener {
                val popupMenu: PopupMenu = PopupMenu(this, homeSettingBtn)
                popupMenu.menuInflater.inflate(R.menu.menu_home_setting, popupMenu.menu)
                popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.action_food -> {
                            supportFragmentManager.beginTransaction()
                                .replace(R.id.container_activity_main, foodFragemnt)
                                .commit()
                        }
                        R.id.action_profile -> {
                            supportFragmentManager.beginTransaction()
                                .replace(R.id.container_activity_main, userFragemnt)
                                .commit()
                        }
                        R.id.action_logout -> {
                            finish()
                            Toast.makeText(this, "登出成功", Toast.LENGTH_SHORT).show()

                        }

                    }
                    true
                })
                popupMenu.show()
            }
///////////////////////////////////////////////////////////////////////////////////////////////////
            waterEdit.setOnFocusChangeListener { v, hasFocus ->
                if (!hasFocus) {
                    try {
                        val waterVolume: Int = waterEdit.text.toString().toInt()
                        val jsonObject = JSONObject()
                        jsonObject.put("waterVolume", waterVolume)
                        jsonObject.put("waterTime", mealDate)
                        jsonObject.put("account", account)
                        if (isWaterRecordCreate) {
                            val jsonObjectString = jsonObject.toString()
                            val requestBody =
                                jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())
                            waterViewModel.createWaterRecord(requestBody)
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
                                        Toast.makeText(
                                            this@HomeActivity,
                                            "新增成功",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                        isWaterRecordCreate = false
                                    }

                                })
                        } else {
                            jsonObject.put("id", waterRecordId)
                            val jsonObjectString = jsonObject.toString()
                            val requestBody =
                                jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())
                            waterViewModel.editWaterRecord(requestBody)
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
                                        Toast.makeText(
                                            this@HomeActivity,
                                            "修改成功",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    }

                                })
                        }
                    } catch (e: Exception) {
                        print(e.stackTrace)
                    }
                }
            }
///////////////////////////////////////////////////////////////////////////////////////////////////
            poopEdit.setOnFocusChangeListener { v, hasFocus ->
                if (!hasFocus) {
                    try {
                        val poopCount: Int = poopEdit.text.toString().toInt()
                        val poopStatus: String =
                            homePoopStatusSpinner.selectedItemPosition.toString()
                        val jsonObject = JSONObject()
                        jsonObject.put("poopCount", poopCount)
                        jsonObject.put("poopStatus", poopStatus)
                        jsonObject.put("account", account)
                        if (isPoopRecordCreate) {
                            val jsonObjectString = jsonObject.toString()
                            val requestBody =
                                jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())
                            poopViewModel.createPoopRecord(requestBody)
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
                                        Toast.makeText(
                                            this@HomeActivity,
                                            "新增成功",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                        isPoopRecordCreate = false
                                    }

                                })
                        } else {
                            jsonObject.put("id", poopRecordId)
                            val jsonObjectString = jsonObject.toString()
                            val requestBody =
                                jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())
                            poopViewModel.editPoopRecord(requestBody)
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
                                        Toast.makeText(
                                            this@HomeActivity,
                                            "修改成功",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    }

                                })
                        }
                    } catch (e: Exception) {
                        print(e.stackTrace)
                    }
                }
            }
        }

    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v: View? = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm: InputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    ///////////////////////////////////////////////////////////////////////////////
    fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }

    fun Date.getToday_Chinaname(): String {
        val data: Date = Date()
        val list = arrayOf("日", "一", "二", "三", "四", "五", "六")
        val calendar: Calendar = Calendar.getInstance()
        calendar.time = data
        var index: Int = calendar.get(Calendar.DAY_OF_WEEK) - 1
        if (index < 0) {
            index = 0
        }
        return list[index]
    }
    ///////////////////////////////////////////////////////////////////////////////

    private fun showPieChart(
        pieChart: PieChart,
        grains: Int,
        vegetables: Int,
        meats: Int,
        milk: Int,
        fruits: Int,
        fats: Int

    ) {
        val label = ""
        val allCount: Float = (grains + vegetables + meats + milk + fruits + fats).toFloat()
        val colors: ArrayList<Int> = ArrayList()
        colors.add(resources.getColor(R.color.pieChart_grains))
        colors.add(resources.getColor(R.color.pieChart_meats_protein))
        colors.add(resources.getColor(R.color.pieChart_milk_dairy))
        colors.add(resources.getColor(R.color.pieChart_vegetables))
        colors.add(resources.getColor(R.color.pieChart_fruits))
        colors.add(resources.getColor(R.color.pieChart_fats))


//        for (type in typeAmountMap.keys) {
//            pieEntries.add(PieEntry(typeAmountMap[type]!!.toFloat(), type))
//        }

        val pieEntries = ArrayList<PieEntry>()
        pieEntries.add(PieEntry((grains.toFloat() / allCount), "全榖雜糧"))
        pieEntries.add(PieEntry((meats.toFloat() / allCount), "蛋豆魚肉"))
        pieEntries.add(PieEntry((milk.toFloat() / allCount), "乳品"))
        pieEntries.add(PieEntry((vegetables.toFloat() / allCount), "蔬菜"))
        pieEntries.add(PieEntry((fruits.toFloat() / allCount), "水果"))
        pieEntries.add(PieEntry((fats.toFloat() / allCount), "油脂與堅果種子"))


        val pieDataSet = PieDataSet(pieEntries, label)
        pieDataSet.valueTextSize = 14f
        pieDataSet.colors = colors
        pieChart.description.isEnabled = false;
        pieChart.setUsePercentValues(true)
        pieDataSet.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                if (value == 0.0f)
                    return "";
                return floor(value).toString().replace(".0", "%")
            }
        }
        pieChart.legend.textSize = 14f
        pieChart.legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        pieChart.legend.verticalAlignment = Legend.LegendVerticalAlignment.CENTER
        pieChart.legend.orientation = Legend.LegendOrientation.VERTICAL
        pieChart.legend.setDrawInside(false)
        pieChart.setDrawSliceText(false)

        val pieData = PieData(pieDataSet)
        pieData.setDrawValues(true)
        pieChart.data = pieData
        pieChart.invalidate()
    }
}

class DividerItemDecorator(private val divider: Drawable?) : RecyclerView.ItemDecoration() {

    override fun onDrawOver(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val dividerLeft = parent.paddingLeft
        val dividerRight = parent.width - parent.paddingRight
        val childCount = parent.childCount
        for (i in 0..childCount - 2) {
            val child: View = parent.getChildAt(i)
            val params =
                child.layoutParams as RecyclerView.LayoutParams
            val dividerTop: Int = child.bottom + params.bottomMargin
            val dividerBottom = dividerTop + (divider?.intrinsicHeight ?: 0)
            divider?.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom)
            divider?.draw(canvas)
        }
    }
}
