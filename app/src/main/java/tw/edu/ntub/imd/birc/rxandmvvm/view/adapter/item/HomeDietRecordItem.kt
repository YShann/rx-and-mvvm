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


class HomeDietRecordItem(private val dietRecord: DietRecord) : AbstractViewItem() {
    override val layoutId: Int = R.layout.recycler_item_home_record

    @SuppressLint("ResourceAsColor", "UseCompatLoadingForColorStateLists")
    override fun bindView(adapter: Adapter, view: View) {
        val recordTime = view.findViewById<TextView>(R.id.recycler_item_home_recordTime)
        val foodname = view.findViewById<TextView>(R.id.recycler_item_home_recordName)
        val foodKind = view.findViewById<TextView>(R.id.recycler_item_home_recordFoodKind)
        recordTime.text = dietRecord.mealTime?.substring(0,5)
        foodname.text = dietRecord.foodName

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
        foodKind.text=foodKindTextList.toString().replace(", ","、").replace("[","").replace("]","")

    }


    override fun onClick(adapter: Adapter, view: View) {
//        super.onClick(adapter, view)
//        val transaction = (view.context as HomeActivity).supportFragmentManager.beginTransaction()
//        transaction.replace(R.id.container_activity_main, HomeActivity.dietRecordDetailFragment(dietRecord))
//            .commit()
    }
    override fun updateView(adapter: Adapter, view: View, payloads: List<Any>) {
        TODO("Not yet implemented")
    }

    private fun HomeActivity.Companion.dietRecordDetailFragment(i: DietRecord?): Fragment {
        return DietRecordDetailFragment(i)
    }

}


