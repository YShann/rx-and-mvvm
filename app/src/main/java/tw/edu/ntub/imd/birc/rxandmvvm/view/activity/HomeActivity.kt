package tw.edu.ntub.imd.birc.rxandmvvm.view.activity

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.androidx.viewmodel.ext.android.viewModel
import tw.edu.ntub.imd.birc.rxandmvvm.R
import tw.edu.ntub.imd.birc.rxandmvvm.extension.attachToRecyclerView
import tw.edu.ntub.imd.birc.rxandmvvm.extension.mapSourceState
import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.ObservableAdapter
import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.item.HomeDietRecordItem
import tw.edu.ntub.imd.birc.rxandmvvm.view.fragement.*
import tw.edu.ntub.imd.birc.rxandmvvm.viewmodel.DietRecordViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.floor


class HomeActivity : AppCompatActivity() {
    private val viewModel: DietRecordViewModel by viewModel()
    private lateinit var navigationview: BottomNavigationView
    private lateinit var homeToolbarTitle: TextView
    private lateinit var pieChart: PieChart
    private lateinit var waterEditImgBtn: ImageButton
    private lateinit var waterEdit: EditText
    private lateinit var homeFoodRecycler: RecyclerView
    private lateinit var homeFoodToDateBtn: ImageButton
    private lateinit var homeFoodToCreateBtn: ImageButton
    private lateinit var homeWaterToDateBtn: ImageButton
    private lateinit var homePoopToDateBtn: ImageButton
    private lateinit var homeSettingBtn: ImageButton


    companion object {
        val homeFragment = HomeFragment()

        //原本是val recordFragment = RecordFragment()被警告有內存洩漏的問題，按照建議改成下面那行
        val recordFragment by lazy { RecordFragment() }
        val createRecordFragment by lazy { CreateDietRecordFragment() }
        val userFragemnt = UserFragment()
        val waterRecordFragment = WaterRecordFragment()
        val poopRecordFragment = PoopRecordFragment()
        val addFragment = AddFragment()
        val otherFragment = OtherFragment()

        //原本是val createDietRecordFragment = CreateDietRecordFragment()被警告有內存洩漏的問題，按照建議改成下面那行
        val createDietRecordFragment by lazy { CreateDietRecordFragment() }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        homeToolbarTitle = findViewById<TextView>(R.id.home_toolbar_title)
        pieChart = findViewById<PieChart>(R.id.home_pieChart)
        waterEdit = findViewById<EditText>(R.id.home_water_editText)
        homeFoodRecycler = findViewById<RecyclerView>(R.id.home_food_recycler)
        homeFoodToDateBtn = findViewById<ImageButton>(R.id.home_food_toDate_btn)
        homeFoodToCreateBtn = findViewById<ImageButton>(R.id.home_food_toCreate_btn)
        homeWaterToDateBtn = findViewById<ImageButton>(R.id.home_water_toDate_btn)
        homePoopToDateBtn = findViewById<ImageButton>(R.id.home_poop_toDate_btn)
        homeSettingBtn = findViewById<ImageButton>(R.id.home_setting_btn)

/////////////////////////////////////////////////////////////////////////////////////////////////////
        val date = getCurrentDateTime()
        val dateInString = date.toString("MM/dd")
        homeToolbarTitle.text =
            dateInString.replace("/", "月").plus("日（${date.getToday_Chinaname()}）")
/////////////////////////////////////////////////////////////////////////////////////////////////////
        val year = Calendar.getInstance().get(Calendar.YEAR)
        val month = Calendar.getInstance().get(Calendar.MONTH).plus(1)
        val day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        val startDate = "${year}-${month}-${day}"
        val endDate = "${year}-${month}-${day.plus(1)}"
        var grainCount = 0
        var vegetableCount = 0
        var meatCount = 0
        var milkCount = 0
        var fruitCount = 0
        var fatCount = 0
        val adapter = ObservableAdapter(
            viewModel.searchByMealTime(startDate, endDate)
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
                    R.id.action_profile -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.container_activity_main, userFragemnt)
                            .commit()
                    }
                    R.id.action_logout -> {

                    }

                }
                true
            })
            popupMenu.show()
        }
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
//        val pieEntries: ArrayList<PieEntry> = ArrayList()
        val label = ""
        val allCount = grains + vegetables + meats + milk + fruits + fats
        //initializing data
//        val typeAmountMap: MutableMap<String, Int> = HashMap()
//        typeAmountMap["全榖雜糧"] = grains / allCount
//        typeAmountMap["蛋豆魚肉"] = meats / allCount
//        typeAmountMap["乳品"] = milk / allCount
//        typeAmountMap["蔬菜"] = vegetables / allCount
//        typeAmountMap["水果"] = fruits / allCount
//        typeAmountMap["油脂與堅果種子"] = fats / allCount

        val colors: ArrayList<Int> = ArrayList()
        colors.add(resources.getColor(R.color.pieChart_0))
        colors.add(resources.getColor(R.color.pieChart_1))
        colors.add(resources.getColor(R.color.pieChart_2))
        colors.add(resources.getColor(R.color.pieChart_3))
        colors.add(resources.getColor(R.color.pieChart_4))
        colors.add(resources.getColor(R.color.pieChart_5))


//        for (type in typeAmountMap.keys) {
//            pieEntries.add(PieEntry(typeAmountMap[type]!!.toFloat(), type))
//        }
        val pieEntries = ArrayList<PieEntry>()
        pieEntries.add(PieEntry((grains / allCount).toFloat(),"全榖雜糧"))
        pieEntries.add(PieEntry((meats / allCount).toFloat(),"蛋豆魚肉"))
        pieEntries.add(PieEntry((milk / allCount).toFloat(),"乳品"))
        pieEntries.add(PieEntry((vegetables / allCount).toFloat(),"蔬菜"))
        pieEntries.add(PieEntry((fruits / allCount).toFloat(),"水果"))
        pieEntries.add(PieEntry((fats / allCount).toFloat(),"油脂與堅果種子"))


        val pieDataSet = PieDataSet(pieEntries, label)
        pieDataSet.valueTextSize = 14f
        pieDataSet.colors = colors
        pieChart.description.isEnabled = false;
        pieChart.setUsePercentValues(true)
        pieDataSet.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                if (value == 0.0f)
                    return "";
                return floor(value).toString().replace(".0","%")
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
