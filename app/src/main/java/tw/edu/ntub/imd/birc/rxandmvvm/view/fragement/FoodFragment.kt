package tw.edu.ntub.imd.birc.rxandmvvm.view.fragement

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import org.koin.androidx.viewmodel.ext.android.viewModel
import tw.edu.ntub.imd.birc.rxandmvvm.R
import tw.edu.ntub.imd.birc.rxandmvvm.extension.attachToRecyclerView
import tw.edu.ntub.imd.birc.rxandmvvm.extension.mapSourceState
import tw.edu.ntub.imd.birc.rxandmvvm.view.activity.DividerItemDecorator
import tw.edu.ntub.imd.birc.rxandmvvm.view.activity.HomeActivity
import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.ObservableAdapter
import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.item.FoodItem
import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.item.WaterRecordItem
import tw.edu.ntub.imd.birc.rxandmvvm.viewmodel.FoodViewModel
import tw.edu.ntub.imd.birc.rxandmvvm.viewmodel.WaterRecordViewModel


class FoodFragment : Fragment() {

    private lateinit var foodReturnBtn: ImageButton
    private lateinit var foodRecyclerView: RecyclerView
    private val viewModel: FoodViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_food, container, false)
        foodRecyclerView = view.findViewById<RecyclerView>(R.id.food_recyclerView)
        foodReturnBtn = view.findViewById<ImageButton>(R.id.food_return)
        foodReturnBtn.setOnClickListener {
            requireActivity().run {
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
        }

        val adapter = ObservableAdapter(
            viewModel.searchAll()
                .mapSourceState {
                    it.data.map { food ->
                        FoodItem(food)
                    }
                }
        )
        adapter.attachToRecyclerView(foodRecyclerView)
        val dividerItemDecoration: RecyclerView.ItemDecoration =
            DividerItemDecorator(ContextCompat.getDrawable(requireActivity(), R.drawable.divider))
        foodRecyclerView.addItemDecoration(dividerItemDecoration)

        return view
    }


}