package tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.item

import android.view.View
import android.widget.TextView
import tw.edu.ntub.imd.birc.rxandmvvm.R
import tw.edu.ntub.imd.birc.rxandmvvm.data.DietRecord
import tw.edu.ntub.imd.birc.rxandmvvm.data.User
import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.Adapter

class DietRecordItem(private val dietRecord: DietRecord) : AbstractViewItem() {
    override val layoutId: Int = R.layout.recycler_item_food_record
    override fun bindView(adapter: Adapter, view: View) {
        val recordDate = view.findViewById<TextView>(R.id.recycler_item_food_recordDate)
        val foodname = view.findViewById<TextView>(R.id.recycler_item_food_recordName)
        val portionSize = view.findViewById<TextView>(R.id.recycler_item_food_portionSize)
        val note = view.findViewById<TextView>(R.id.recycler_item_food_note)
        recordDate.text = dietRecord.mealTime
        foodname.text = dietRecord.foodName
        portionSize.text = dietRecord.portionSize
        note.text = dietRecord.note
    }

    override fun updateView(adapter: Adapter, view: View, payloads: List<Any>) {
        TODO("Not yet implemented")
    }

}