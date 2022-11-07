package tw.edu.ntub.imd.birc.rxandmvvm.view.activity

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.androidx.viewmodel.ext.android.viewModel
import tw.edu.ntub.imd.birc.rxandmvvm.R
import tw.edu.ntub.imd.birc.rxandmvvm.extension.attachToRecyclerView
import tw.edu.ntub.imd.birc.rxandmvvm.extension.mapSourceState
import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.ObservableAdapter
import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.item.DietRecordItem
import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.item.HomeDietRecordItem
import tw.edu.ntub.imd.birc.rxandmvvm.view.fragement.*
import tw.edu.ntub.imd.birc.rxandmvvm.viewmodel.DietRecordViewModel
import java.text.SimpleDateFormat
import java.time.Year
import java.util.*


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
        showPieChart(pieChart)
        val year = Calendar.getInstance().get(Calendar.YEAR)
        val month = Calendar.getInstance().get(Calendar.MONTH).plus(1)
        val day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        val startDate = "${year}-${month}-${day}"
        val endDate = "${year}-${month}-${day.plus(1)}"
        Log.d("startDate",startDate)
        Log.d("endDate",endDate)
        val adapter = ObservableAdapter(
            viewModel.searchByMealTime(startDate, endDate)
                .mapSourceState { it.data.map { dietRecord -> HomeDietRecordItem(dietRecord) } }
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


//                    //        //設定初始時要show哪個fragment
//            navigationview = findViewById<BottomNavigationView>(R.id.main_navigationView)
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.container_activity_main, recordFragment)
//                .commit()
//            navigationview.selectedItemId = R.id.f2
//            navigationview.setOnNavigationItemSelectedListener(listener)


    //    private var listener = object : BottomNavigationView.OnNavigationItemSelectedListener {
//        //設定navigationBar裡的item們的點擊事件 被點擊後採動態載入fragment的方式
//        override fun onNavigationItemSelected(item: MenuItem): Boolean {
//            when (item.itemId) {
////                R.id.f1 -> {
////                    val manager = supportFragmentManager
////                    val transaction = manager.beginTransaction()
////                    transaction.replace(R.id.container_activity_main, homeFragment).commit()
////
////                }
//                R.id.f2 -> {
//                    val t = supportFragmentManager.beginTransaction()
//                    t.replace(R.id.container_activity_main, recordFragment).commit()
//                }
//                R.id.f3 -> {
//                    val t = supportFragmentManager.beginTransaction()
//                    t.replace(R.id.container_activity_main, addFragment).commit()
//                }
//
//                R.id.f4 -> {
//                    val t = supportFragmentManager.beginTransaction()
//                    t.replace(R.id.container_activity_main, userFragemnt).commit()
//                }
//
////                R.id.f5 -> {
////                    val t = supportFragmentManager.beginTransaction()
////                    t.replace(R.id.container_activity_main, otherFragment).commit()
////                }
//            }
//            return true
//        }

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

    private fun showPieChart(pieChart: PieChart) {
        val pieEntries: ArrayList<PieEntry> = ArrayList()
        val label = ""

        //initializing data

        //initializing data
        val typeAmountMap: MutableMap<String, Int> = HashMap()
        typeAmountMap["全榖雜糧"] = 1
        typeAmountMap["蛋豆魚肉"] = 1
        typeAmountMap["乳品"] = 1
        typeAmountMap["蔬菜"] = 1
        typeAmountMap["水果"] = 2
        typeAmountMap["油脂與堅果種子"] = 1

        val colors: ArrayList<Int> = ArrayList()
        colors.add(resources.getColor(R.color.pieChart_0))
        colors.add(resources.getColor(R.color.pieChart_1))
        colors.add(resources.getColor(R.color.pieChart_2))
        colors.add(resources.getColor(R.color.pieChart_3))
        colors.add(resources.getColor(R.color.pieChart_4))
        colors.add(resources.getColor(R.color.pieChart_5))


        for (type in typeAmountMap.keys) {
            pieEntries.add(PieEntry(typeAmountMap[type]!!.toFloat(), type))
        }

        val pieDataSet = PieDataSet(pieEntries, label)
        pieDataSet.valueTextSize = 14f
        pieDataSet.colors = colors
        pieChart.description.isEnabled = false;
        pieChart.legend.textSize = 14f
        pieChart.legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        pieChart.legend.verticalAlignment = Legend.LegendVerticalAlignment.CENTER
        pieChart.legend.orientation = Legend.LegendOrientation.VERTICAL
        pieChart.legend.setDrawInside(false)
        pieChart.setDrawSliceText(false)
//        pieChart.animateY(1400, Easing.EaseInOutQuad);

        val pieData = PieData(pieDataSet)
        pieData.setDrawValues(true)
        pieChart.data = pieData
        pieChart.invalidate()
    }
}
