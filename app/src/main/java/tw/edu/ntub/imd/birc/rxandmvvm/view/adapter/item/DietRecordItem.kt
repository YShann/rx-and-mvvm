package tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.item

import android.annotation.SuppressLint
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
        recordDate.text = dietRecord.mealTime!!.substring(0 until 16)
        foodname.text = dietRecord.foodName
        portionSize.text = dietRecord.portionSize

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
        }
        if (dietRecord.meatsAndProtein == "1") {
            foodKindTextList.add(meatsAndProtein)
        }
        if (dietRecord.vegetables == "1") {
            foodKindTextList.add(vegetables)
        }
        if (dietRecord.fruits == "1") {
            foodKindTextList.add(fruits)
        }
        if (dietRecord.milkAndDairy == "1") {
            foodKindTextList.add(milkAndDairy)
        }
        if (dietRecord.fats == "1") {
            foodKindTextList.add(fats)
        }
        foodKind.text=foodKindTextList.toString().replace(",","、").replace("[","").replace("]","")

        when (dietRecord.portionSize) {
            "0" -> {
                portionSize.text = "少量"
                portionSize.backgroundTintList = view.resources.getColorStateList(R.color.diet_record_portion_size_yellow)
            }
            "1" -> {
                portionSize.text = "適中"
                portionSize.backgroundTintList = view.resources.getColorStateList(R.color.diet_record_portion_size_green)
            }
            "2" -> {
                portionSize.text = "飽食"
                portionSize.backgroundTintList = view.resources.getColorStateList(R.color.diet_record_portion_size_yellow)
            }
            else -> {
                portionSize.text = "過量"
                portionSize.backgroundTintList = view.resources.getColorStateList(R.color.diet_record_portion_size_red)
            }
        }

    }

    override fun onClick(adapter: Adapter, view: View) {
        super.onClick(adapter, view)
        val transaction = (view.context as MainActivity).supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container_activity_main, MainActivity.dietRecordDetailFragment(dietRecord))
            .commit()
    }
    override fun updateView(adapter: Adapter, view: View, payloads: List<Any>) {
        TODO("Not yet implemented")
    }

    private fun MainActivity.Companion.dietRecordDetailFragment(i: DietRecord?): Fragment {
        return DietRecordDetailFragment(i)
    }

}


