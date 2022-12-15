package tw.edu.ntub.imd.birc.rxandmvvm.view.fragement

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.borax12.materialdaterangepicker.date.DatePickerDialog
import com.google.android.material.tabs.TabLayout
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import org.koin.androidx.viewmodel.ext.android.viewModel
import tw.edu.ntub.imd.birc.rxandmvvm.R
import tw.edu.ntub.imd.birc.rxandmvvm.extension.attachToRecyclerView
import tw.edu.ntub.imd.birc.rxandmvvm.extension.mapSourceState
import tw.edu.ntub.imd.birc.rxandmvvm.view.activity.DividerItemDecorator
import tw.edu.ntub.imd.birc.rxandmvvm.view.activity.HomeActivity
import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.ObservableAdapter
import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.item.DietRecordItem
import tw.edu.ntub.imd.birc.rxandmvvm.viewmodel.DietRecordViewModel
import java.util.*


class RecordFragment : Fragment(), DatePickerDialog.OnDateSetListener {
    private val viewModel: DietRecordViewModel by viewModel()
    private var currentDate: CalendarDay? = null
    private var currentYear: Int = 0
    var currentMonth: Int = 0
    var currentDay: Int = 0
    var dayList: MutableList<String> =
        mutableListOf("31", "28", "31", "30", "31", "30", "31", "31", "30", "31", "30", "31")
    var startDate: String? = null
    var endDate: String? = null
    var mealDate: String? = null
    var searchStatusIsDay: Boolean = true
    var searchStatusIsMonth: Boolean = false
    var searchStatusIsCustom: Boolean = false
    val sharedPref = context?.getSharedPreferences("foodCategory", Context.MODE_PRIVATE)
    val editor = sharedPref?.edit()
    private lateinit var dateEdit: EditText
    private lateinit var recyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_record, container, false)
        val calendarView = view.findViewById<MaterialCalendarView>(R.id.record_calendarView)
        val tabLayout = view.findViewById<TabLayout>(R.id.record_tabLayout_date)
        recyclerView = view.findViewById(R.id.recycler_food_record)
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
        val endDay = dayList[currentMonth - 1].toInt()
        tabLayout.removeAllTabs()
        for (i in 1..endDay) {
            tabLayout.addTab(tabLayout.newTab().setText("$i 日"))
        }
        Handler().postDelayed(
            Runnable {
                tabLayout.getTabAt(Calendar.getInstance().get(Calendar.DAY_OF_MONTH).minus(1))
                    ?.select()
            }, 100
        )

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (searchStatusIsDay) {
                    val tabDate = tab?.position?.plus(1)
                    mealDate = "$currentYear-$currentMonth-${tabDate}"
                    val adapter = ObservableAdapter(
                        viewModel.searchByMealDate(mealDate!!)
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
            if (searchStatusIsDay) {
                mealDate = "$currentYear-$currentMonth-${
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                }"
                val adapter = ObservableAdapter(
                    viewModel.searchByMealDate(mealDate!!)
                        .mapSourceState { it.data.map { dietRecord -> DietRecordItem(dietRecord) } }
                )
                adapter.attachToRecyclerView(recyclerView)
            } else {
                val day = dayList[currentMonth.minus(1)].toInt()
                startDate = "$currentYear-$currentMonth-01"
                endDate = "$currentYear-$currentMonth-$day"

                val adapter = ObservableAdapter(
                    viewModel.searchByMealDateRange(startDate!!, endDate!!)
                        .mapSourceState { it.data.map { dietRecord -> DietRecordItem(dietRecord) } }
                )
                adapter.attachToRecyclerView(recyclerView)
            }
        }

        val dateChoice = view.findViewById(R.id.record_date_choice) as ImageButton
        dateChoice.setOnClickListener {
            val popupMenu: PopupMenu = PopupMenu(context, dateChoice)
            popupMenu.menuInflater.inflate(R.menu.menu_record_date_choice, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_date -> {
                        searchStatusIsDay = true
                        searchStatusIsMonth = false
                        searchStatusIsCustom = false
                        mealDate = "$currentYear-$currentMonth-${
                            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                        }"
                        val adapter = ObservableAdapter(
                            viewModel.searchByMealDate(mealDate!!)
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
                        searchStatusIsMonth = true
                        searchStatusIsCustom = false
                        val day = dayList[currentMonth.minus(1)].toInt()
                        startDate = "$currentYear-$currentMonth-01"
                        endDate = "$currentYear-$currentMonth-$day"
                        val adapter = ObservableAdapter(
                            viewModel.searchByMealDateRange(startDate!!, endDate!!)
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
                        searchStatusIsDay = false
                        searchStatusIsMonth = false
                        searchStatusIsCustom = true
                        this.showDatePickerDialog()
                    }

                }
                true
            })
            popupMenu.show()
        }


        return view
    }

    private fun showDatePickerDialog(){
        val now = Calendar.getInstance()
        val dpd: DatePickerDialog = DatePickerDialog.newInstance(
            this,
            now[Calendar.YEAR],
            now[Calendar.MONTH],
            now[Calendar.DAY_OF_MONTH]
        )
        dpd.show(activity?.fragmentManager, "Datepickerdialog")
    }

    override fun onDateSet(
        view: DatePickerDialog?,
        year: Int,
        monthOfYear: Int,
        dayOfMonth: Int,
        yearEnd: Int,
        monthOfYearEnd: Int,
        dayOfMonthEnd: Int
    ) {
        val timeStart = "$year-${monthOfYear.plus(1)}-$dayOfMonth"
        val timeEnd = "$yearEnd-${monthOfYearEnd.plus(1)}-$dayOfMonthEnd"
        val adapter = ObservableAdapter(
            viewModel.searchByMealDateRange(timeStart, timeEnd)
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

}
