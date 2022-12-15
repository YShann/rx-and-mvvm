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
import tw.edu.ntub.imd.birc.rxandmvvm.data.PoopRecord
import tw.edu.ntub.imd.birc.rxandmvvm.data.WaterRecord
import tw.edu.ntub.imd.birc.rxandmvvm.view.activity.HomeActivity
import tw.edu.ntub.imd.birc.rxandmvvm.view.activity.MainActivity
import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.Adapter
import tw.edu.ntub.imd.birc.rxandmvvm.view.fragement.DietRecordDetailFragment
import tw.edu.ntub.imd.birc.rxandmvvm.view.fragement.PoopRecordDetailFragment
import tw.edu.ntub.imd.birc.rxandmvvm.view.fragement.WaterRecordDetailFragment
import java.util.concurrent.Executors


class PoopRecordItem(private val poopRecord: PoopRecord) : AbstractViewItem() {
    override val layoutId: Int = R.layout.recycler_item_poop_record

    @SuppressLint("ResourceAsColor", "UseCompatLoadingForColorStateLists")
    override fun bindView(adapter: Adapter, view: View) {
        val poopRecordDate = view.findViewById<TextView>(R.id.recycler_item_poop_recordDate)
        val poopRecordVolume = view.findViewById<TextView>(R.id.recycler_item_poop_recordCount)
        val poopStatusList: MutableList<String> =
            mutableListOf("硬球狀(便祕型)", "有凹凸長條狀(便祕型)", "有裂痕長條狀(正常型)", "光滑的長條狀(正常型)", "鬆軟的顆粒狀(腹瀉型)", "軟泥狀(腹瀉型)", "水狀(腹瀉型)")
        val poopStatus=poopStatusList[poopRecord.poopStatus?.toInt()!!]
        poopRecordDate.text = poopRecord.poopTime?.substring(5,10)
        poopRecordVolume.text = poopRecord.poopCount.toString().plus(" 次，").plus(poopStatus)
    }

    override fun onClick(adapter: Adapter, view: View) {
        super.onClick(adapter, view)
        val transaction = (view.context as HomeActivity).supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container_activity_main, HomeActivity.poopRecordDetailFragment(poopRecord))
            .commit()
    }
    override fun updateView(adapter: Adapter, view: View, payloads: List<Any>) {
        TODO("Not yet implemented")
    }

    private fun HomeActivity.Companion.poopRecordDetailFragment(i: PoopRecord?): Fragment {
        return PoopRecordDetailFragment(i)
    }
}


