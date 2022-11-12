package tw.edu.ntub.imd.birc.rxandmvvm.view.fragement

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Point
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.android.material.tabs.TabLayout
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import org.koin.androidx.viewmodel.ext.android.viewModel
import tw.edu.ntub.imd.birc.rxandmvvm.R
import tw.edu.ntub.imd.birc.rxandmvvm.extension.attachToRecyclerView
import tw.edu.ntub.imd.birc.rxandmvvm.extension.mapSourceState
import tw.edu.ntub.imd.birc.rxandmvvm.view.activity.DividerItemDecorator
import tw.edu.ntub.imd.birc.rxandmvvm.view.activity.HomeActivity
import tw.edu.ntub.imd.birc.rxandmvvm.view.activity.RegisterActivity
import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.ObservableAdapter
import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.item.DietRecordItem
import tw.edu.ntub.imd.birc.rxandmvvm.viewmodel.DietRecordViewModel
import java.text.SimpleDateFormat
import java.time.Year
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class RecordFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private var point: Point? = null
    private val viewModel: DietRecordViewModel by viewModel()
    private var currentDate: CalendarDay? = null
    private var currentYear: Int = 0
    var currentMonth: Int = 0
    var currentDay: Int = 0
    var dayList: MutableList<String> =
        mutableListOf("31", "28", "31", "30", "31", "30", "31", "31", "30", "31", "30", "31")
    var startDate: String? = null
    var endDate: String? = null
    var searchStatusIsDay: Boolean = true
    var searchStatusIsCustom: Boolean = false
    val sharedPref = context?.getSharedPreferences("foodCategory", Context.MODE_PRIVATE)
    val editor = sharedPref?.edit()
    private lateinit var dateEdit: EditText
    private var customStartDate: String? = null
    private var customEndDate: String? = null
    private lateinit var recyclerView: RecyclerView


    //private lateinit var adapter : Adapter
    private lateinit var spinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        //val adapter = ArrayAdapter.createFromResource(this, R.array.lunch, android.R.layout.simple_spinner_dropdown_item)
        //spinner.adapter = adapter

    }

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_record, container, false)
        val calendarView = view.findViewById<MaterialCalendarView>(R.id.record_calendarView)
        val tabLayout = view.findViewById<TabLayout>(R.id.record_tabLayout_date)
        recyclerView = view.findViewById<RecyclerView>(R.id.recycler_food_record)
        val recordDateReturn = view.findViewById<ImageButton>(R.id.record_date_return)
        val adapter = ObservableAdapter(
            viewModel.searchAll()
                .mapSourceState { it.data.map { dietRecord -> DietRecordItem(dietRecord) } }
        )
        adapter.attachToRecyclerView(recyclerView)
        val dividerItemDecoration: RecyclerView.ItemDecoration =
            DividerItemDecorator(context?.let { ContextCompat.getDrawable(it, R.drawable.divider) })
        recyclerView.addItemDecoration(dividerItemDecoration)

        recordDateReturn.setOnClickListener {
            requireActivity().run {
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
        }

        currentDate = calendarView.currentDate
        currentYear = calendarView.currentDate.year
        currentMonth = calendarView.currentDate.month
        currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        val endDay = dayList[currentMonth].toInt()
        tabLayout.removeAllTabs()
        for (i in 1..endDay) {
            tabLayout.addTab(tabLayout.newTab().setText("$i 日"))
        }
//        tabLayout.getTabAt(Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
        Handler().postDelayed(
            Runnable {
                tabLayout.getTabAt(Calendar.getInstance().get(Calendar.DAY_OF_MONTH).minus(1))
                    ?.select()
            }, 100
        )

        ///滑動月份查詢
        calendarView.setOnMonthChangedListener { widget, date ->
            tabLayout.removeAllTabs()
            currentYear = date.year
            currentMonth = date.month
            val day = dayList[currentMonth.minus(1)].toInt()
            for (i in 1..day) {
                tabLayout.addTab(tabLayout.newTab().setText("$i 日"))
            }
            Handler().postDelayed(
                Runnable {
                    tabLayout.getTabAt(Calendar.getInstance().get(Calendar.DAY_OF_MONTH).minus(1))
                        ?.select()
                }, 100
            )
            if (searchStatusIsDay && !searchStatusIsCustom) {
                startDate = "$currentYear-$currentMonth-${
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH).plus(1)
                }"
                endDate = "$currentYear-$currentMonth-${
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH)?.plus(2)
                }"
                val adapter = ObservableAdapter(
                    viewModel.searchByMealTime(startDate!!, endDate!!)
                        .mapSourceState { it.data.map { dietRecord -> DietRecordItem(dietRecord) } }
                )
                adapter.attachToRecyclerView(recyclerView)
            } else {
                val day = dayList[currentMonth.minus(1)].toInt()
                startDate = "$currentYear-$currentMonth-01"
                endDate = "$currentYear-$currentMonth-$day"

                val adapter = ObservableAdapter(
                    viewModel.searchByMealTime(startDate!!, endDate!!)
                        .mapSourceState { it.data.map { dietRecord -> DietRecordItem(dietRecord) } }
                )
                adapter.attachToRecyclerView(recyclerView)
            }
        }

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (searchStatusIsDay && !searchStatusIsCustom) {
                    val tabDate = tab?.position?.plus(1)
                    Log.d("tabDate", tab?.position.toString())
                    currentDay = tab?.position?.plus(1)!!
                    startDate = "$currentYear-$currentMonth-${tabDate}"
                    endDate = "$currentYear-$currentMonth-${tabDate?.plus(1)}"
                    val adapter = ObservableAdapter(
                        viewModel.searchByMealTime(startDate!!, endDate!!)
                            .mapSourceState { it.data.map { dietRecord -> DietRecordItem(dietRecord) } }
                    )
                    adapter.attachToRecyclerView(recyclerView)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })


        val dateChoice = view.findViewById(R.id.record_date_choice) as ImageButton
        dateChoice.setOnClickListener {
            val popupMenu: PopupMenu = PopupMenu(context, dateChoice)
            popupMenu.menuInflater.inflate(R.menu.menu_record_date_choice, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_date -> {
                        searchStatusIsDay = true
                        searchStatusIsCustom = false
                        startDate = "$currentYear-$currentMonth-${
                            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                        }"
                        endDate = "$currentYear-$currentMonth-${
                            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)?.plus(1)
                        }"
                        val adapter = ObservableAdapter(
                            viewModel.searchByMealTime(startDate!!, endDate!!)
                                .mapSourceState {
                                    it.data.map { dietRecord ->
                                        DietRecordItem(
                                            dietRecord
                                        )
                                    }
                                }
                        )
                        adapter.attachToRecyclerView(recyclerView)
                    }
                    R.id.action_month -> {
                        searchStatusIsDay = false
                        searchStatusIsCustom = false
                        val day = dayList[currentMonth.minus(1)].toInt()
                        startDate = "$currentYear-$currentMonth-01"
                        endDate = "$currentYear-$currentMonth-$day"

                        val adapter = ObservableAdapter(
                            viewModel.searchByMealTime(startDate!!, endDate!!)
                                .mapSourceState {
                                    it.data.map { dietRecord ->
                                        DietRecordItem(
                                            dietRecord
                                        )
                                    }
                                }
                        )
                        adapter.attachToRecyclerView(recyclerView)
                    }
                    R.id.action_customize -> {
                        searchStatusIsCustom = true
                        startDatePicker()
                    }

                }
                true
            })
            popupMenu.show()
        }


        return view
    }

    private fun startDatePicker() {
        val calender: Calendar = Calendar.getInstance()
        val picker = context?.let {
            DatePickerDialog(
                it,
                { _, year, month, day ->
                    calender.set(year, month, day)
                },
                calender.get(Calendar.YEAR),
                calender.get(Calendar.MONTH),
                calender.get(Calendar.DATE)
            )
        }
        picker?.setCancelable(true)
        picker?.setCanceledOnTouchOutside(true)
        picker?.setButton(
            DialogInterface.BUTTON_POSITIVE, "確定"
        ) { _, _ ->
            customStartDate = "${calender.time}"
            Log.d("startDate", customStartDate.toString())

//            this.endDatePicker()
        }
        picker?.setButton(
            DialogInterface.BUTTON_NEGATIVE, "取消"
        ) { _, _ -> Log.d("Picker", "Cancel!") }
        picker?.show()
    }

//    private val startDateListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
//        Log.d("test","ttttttttttttttttttttttttttttttttt")
//        startCustomCalender.set(year, month, day)
//        customStartDate="${year}-${month}-${day}"
//                Log.d("startDate", customStartDate.toString())
//
//    }

    private fun endDatePicker() {
        val calender: Calendar = Calendar.getInstance()
        val picker = context?.let {
            DatePickerDialog(
                it,
                { _, year, month, day ->
                    calender.set(year, month, day)
                },
                calender.get(Calendar.YEAR),
                calender.get(Calendar.MONTH),
                calender.get(Calendar.DATE)
            )
        }
        picker?.setCancelable(true)
        picker?.setCanceledOnTouchOutside(true)
        picker?.setButton(
            DialogInterface.BUTTON_POSITIVE, "確定"
        ) { _, _ ->
            customEndDate = "${calender.get(Calendar.YEAR)}-${calender.get(Calendar.MONTH)}-${
                calender.get(Calendar.DAY_OF_MONTH)
            }"
            Log.d("endDate", customEndDate.toString())
            this.customDateSearch()
        }
        picker?.setButton(
            DialogInterface.BUTTON_NEGATIVE, "取消"
        ) { _, _ -> Log.d("Picker", "Cancel!") }
        picker?.show()
    }

//    private val endDateListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
//        endCustomCalender.set(year, month, day)
//        customEndDate="${year}-${month}-${day}"
//    }

    private fun customDateSearch() {

//        customStartDate =
//            "${startCustomCalender.get(Calendar.YEAR)}-${startCustomCalender.get(Calendar.MONTH)}-${
//                startCustomCalender.get(Calendar.DAY_OF_MONTH)
//            }"
//        customEndDate =
//            "${endCustomCalender.get(Calendar.YEAR)}-${endCustomCalender.get(Calendar.MONTH)}-${
//                endCustomCalender.get(Calendar.DAY_OF_MONTH).plus(1)
//            }"
//        val adapter = ObservableAdapter(
//            viewModel.searchByMealTime(customStartDate!!, customEndDate!!)
//                .mapSourceState { it.data.map { dietRecord -> DietRecordItem(dietRecord) } }
//        )
//        adapter.attachToRecyclerView(recyclerView)
    }

}
