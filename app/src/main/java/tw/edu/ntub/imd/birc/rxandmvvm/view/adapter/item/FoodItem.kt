package tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.item

import android.view.View
import android.widget.TextView
import tw.edu.ntub.imd.birc.rxandmvvm.R
import tw.edu.ntub.imd.birc.rxandmvvm.data.Food
import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.Adapter

class FoodItem(private val food: Food) : AbstractViewItem() {
    override val layoutId: Int = R.layout.recycler_item_food
    override fun bindView(adapter: Adapter, view: View) {
        val name = view.findViewById<TextView>(R.id.recycler_item_food_name)
        val energy = view.findViewById<TextView>(R.id.recycler_item_food_energy)
        name.text = food.name
        energy.text = food.energy.toString()
    }

    override fun updateView(adapter: Adapter, view: View, payloads: List<Any>) {
        TODO("Not yet implemented")
    }

}