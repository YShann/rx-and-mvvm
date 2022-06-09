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
        val type = view.findViewById<TextView>(R.id.recycler_item_food_type)
        val energy = view.findViewById<TextView>(R.id.recycler_item_food_energy)
        val carbohydrate = view.findViewById<TextView>(R.id.recycler_item_food_carbohydrate)
        val protein = view.findViewById<TextView>(R.id.recycler_item_food_protein)
        val fat = view.findViewById<TextView>(R.id.recycler_item_food_fat)
        name.text = food.name
        type.text = food.type
        energy.text = food.energy.toString()
        carbohydrate.text = food.carbohydrate.toString()
        protein.text = food.protein.toString()
        fat.text = food.fat.toString()
    }

    override fun updateView(adapter: Adapter, view: View, payloads: List<Any>) {
        TODO("Not yet implemented")
    }

}