package tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.item

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import tw.edu.ntub.imd.birc.rxandmvvm.R
import tw.edu.ntub.imd.birc.rxandmvvm.constant.UrlConstant
import tw.edu.ntub.imd.birc.rxandmvvm.data.DietRecord
import tw.edu.ntub.imd.birc.rxandmvvm.view.activity.HomeActivity
import tw.edu.ntub.imd.birc.rxandmvvm.view.activity.MainActivity
import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.Adapter
import tw.edu.ntub.imd.birc.rxandmvvm.view.fragement.DietRecordDetailFragment
import java.util.concurrent.Executors


class DietRecordItem(private val dietRecord: DietRecord) : AbstractViewItem() {
    override val layoutId: Int = R.layout.recycler_item_food_record

    @SuppressLint("ResourceAsColor", "UseCompatLoadingForColorStateLists")
    override fun bindView(adapter: Adapter, view: View) {
        val recordDate = view.findViewById<TextView>(R.id.recycler_item_food_recordDate)
        val foodname = view.findViewById<TextView>(R.id.recycler_item_food_recordName)
        val portionSize = view.findViewById<TextView>(R.id.recycler_item_food_portionSize)
        val foodKind = view.findViewById<TextView>(R.id.recycler_item_dietRecord_text_foodKind)
        val image = view.findViewById<ImageView>(R.id.recycler_item_dietRecord_image)
        recordDate.text = dietRecord.mealTime!!.substring(5 until 16)
        foodname.text = dietRecord.foodName

        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        var imageBitmap: Bitmap? = null
        executor.execute {
            val imageURL = dietRecord.imageUrl!!.replace("http://localhost:8080/",UrlConstant.BASE_URL)

            try {
                val `in` = java.net.URL(imageURL).openStream()
                imageBitmap = BitmapFactory.decodeStream(`in`)

                handler.post {
                    image.setImageBitmap(imageBitmap)
                }
            }

            catch (e: Exception) {
                e.printStackTrace()
            }
        }

        val grains = "全榖雜糧類"
        val meatsAndProtein = "蛋豆魚肉類"
        val vegetables = "蔬菜類"
        val fruits = "水果類"
        val milkAndDairy = "乳品類"
        val fats = "油脂與堅果種子類"
        var foodKindTextList: MutableList<String> = mutableListOf<String>()

        if (dietRecord.grains == "1") {
            foodKindTextList.add(grains)
//                    SharedPreferences.putInt("grains", it.plus(1))
//            ?.getInt("grains",0)?.let { editor?.putInt("grains", it.plus(1) ) }

        }
        if (dietRecord.meatsAndProtein == "1") {
            foodKindTextList.add(meatsAndProtein)
//            sharedPref?.getInt("meatsAndProtein",0)?.let { editor?.putInt("meatsAndProtein", it.plus(1) ) }

        }
        if (dietRecord.vegetables == "1") {
            foodKindTextList.add(vegetables)
//            sharedPref?.getInt("vegetables",0)?.let { editor?.putInt("vegetables", it.plus(1) ) }

        }
        if (dietRecord.fruits == "1") {
            foodKindTextList.add(fruits)
//            sharedPref?.getInt("fruits",0)?.let { editor?.putInt("fruits", it.plus(1) ) }

        }
        if (dietRecord.milkAndDairy == "1") {
            foodKindTextList.add(milkAndDairy)
//            sharedPref?.getInt("milkAndDairy",0)?.let { editor?.putInt("milkAndDairy", it.plus(1) ) }

        }
        if (dietRecord.fats == "1") {
            foodKindTextList.add(fats)
//            sharedPref?.getInt("fats",0)?.let { editor?.putInt("fats", it.plus(1) ) }

        }
        foodKind.text=foodKindTextList.toString().replace(", ","、").replace("[","").replace("]","")

        if(dietRecord.portionSize!! <5){
            portionSize.text = dietRecord.portionSize.toString().plus("分")
            portionSize.setTextColor(view.resources.getColor(R.color.diet_record_portion_size_red))
        }else if(dietRecord.portionSize!! in 5..6){
            portionSize.text = dietRecord.portionSize.toString().plus("分")
            portionSize.setTextColor(view.resources.getColor(R.color.diet_record_portion_size_yellow))
        }else if(dietRecord.portionSize!! in 7..8){
            portionSize.text = dietRecord.portionSize.toString().plus("分")
            portionSize.setTextColor(view.resources.getColor(R.color.diet_record_portion_size_green))
        }else{
            portionSize.text = dietRecord.portionSize.toString().plus("分")
            portionSize.setTextColor(view.resources.getColor(R.color.diet_record_portion_size_red))
        }

    }

    override fun onClick(adapter: Adapter, view: View) {
        super.onClick(adapter, view)
        val transaction = (view.context as HomeActivity).supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container_activity_main, HomeActivity.dietRecordDetailFragment(dietRecord))
            .commit()
    }
    override fun updateView(adapter: Adapter, view: View, payloads: List<Any>) {
        TODO("Not yet implemented")
    }

    private fun HomeActivity.Companion.dietRecordDetailFragment(i: DietRecord?): Fragment {
        return DietRecordDetailFragment(i)
    }

}


