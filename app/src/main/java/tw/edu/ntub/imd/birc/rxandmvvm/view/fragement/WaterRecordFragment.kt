package tw.edu.ntub.imd.birc.rxandmvvm.view.fragement

import android.R.attr.font
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.Align
import android.os.Build
import android.os.Bundle
import android.text.style.LineBackgroundSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import org.koin.androidx.viewmodel.ext.android.viewModel
import tw.edu.ntub.imd.birc.rxandmvvm.R
import tw.edu.ntub.imd.birc.rxandmvvm.extension.attachToRecyclerView
import tw.edu.ntub.imd.birc.rxandmvvm.extension.mapSourceState
import tw.edu.ntub.imd.birc.rxandmvvm.view.activity.HomeActivity
import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.ObservableAdapter
import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.item.WaterRecordItem
import tw.edu.ntub.imd.birc.rxandmvvm.viewmodel.WaterRecordViewModel


class WaterRecordFragment : Fragment() {

    private lateinit var waterReturnBtn: ImageButton
    private lateinit var calendarView: MaterialCalendarView
    private lateinit var waterRecordRecyclerView: RecyclerView
    private val viewModel: WaterRecordViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_water_record, container, false)
        waterReturnBtn = view.findViewById<ImageButton>(R.id.water_record_return)
        calendarView = view.findViewById<MaterialCalendarView>(R.id.water_record_calendarView)
        waterRecordRecyclerView = view.findViewById<RecyclerView>(R.id.water_record_recyclerView)

        waterReturnBtn.setOnClickListener {
            requireActivity().run {
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
        }

        val adapter = ObservableAdapter(
            viewModel.searchAll()
                .mapSourceState {
                    it.data.map { waterRecord ->
                        val year = waterRecord.waterTime?.substring(0, 4)?.toInt()
                        val month = waterRecord.waterTime?.substring(5, 7)?.toInt()
                        val date = waterRecord.waterTime?.substring(8, 10)?.toInt()
                        calendarView.addDecorator(object : DayViewDecorator {
                            override fun shouldDecorate(day: CalendarDay): Boolean {
                                val currentDay  = CalendarDay.from(year!!, month!!, date!!)
                                return day == currentDay
                                return true
                            }

                            override fun decorate(view: DayViewFacade) {
                                view.addSpan(AddTextToDates(waterRecord.waterVolume.toString().plus(" c.c.")))
                                view.setDaysDisabled(true)
                            }
                        })
                    }
                    it.data.map { waterRecord ->
                        WaterRecordItem(waterRecord)
                    }
                }
        )
        adapter.attachToRecyclerView(waterRecordRecyclerView)


        return view
    }


}


class AddTextToDates(text: String) : LineBackgroundSpan {

    private var dayPrice = text

    @SuppressLint("ResourceAsColor")
    override fun drawBackground(
        canvas: Canvas,
        paint: Paint,
        left: Int,
        right: Int,
        top: Int,
        baseline: Int,
        bottom: Int,
        text: CharSequence,
        start: Int,
        end: Int,
        lnum: Int
    ) {
        paint.setARGB(200, 254, 0, 0)
        paint.textAlign = Align.CENTER
        paint.textSize = 32f
        canvas.drawText(
            dayPrice, (canvas.width / 2).toFloat(),
            (canvas.height / 2).toFloat(), paint
        )
    }
}