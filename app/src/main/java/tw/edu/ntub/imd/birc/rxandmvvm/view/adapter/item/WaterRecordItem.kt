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
import tw.edu.ntub.imd.birc.rxandmvvm.data.WaterRecord
import tw.edu.ntub.imd.birc.rxandmvvm.view.activity.HomeActivity
import tw.edu.ntub.imd.birc.rxandmvvm.view.activity.MainActivity
import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.Adapter
import tw.edu.ntub.imd.birc.rxandmvvm.view.fragement.DietRecordDetailFragment
import java.util.concurrent.Executors


class WaterRecordItem(private val waterRecord: WaterRecord) : AbstractViewItem() {
    override val layoutId: Int = R.layout.recycler_item_water_record

    @SuppressLint("ResourceAsColor", "UseCompatLoadingForColorStateLists")
    override fun bindView(adapter: Adapter, view: View) {
        val waterRecordDate = view.findViewById<TextView>(R.id.recycler_item_water_recordDate)
        val waterRecordVolume = view.findViewById<TextView>(R.id.recycler_item_water_recordVolume)
        waterRecordDate.text = waterRecord.waterTime
        waterRecordVolume.text = waterRecord.waterVolume.toString()
    }

    override fun onClick(adapter: Adapter, view: View) {
        super.onClick(adapter, view)

    }
    override fun updateView(adapter: Adapter, view: View, payloads: List<Any>) {
        TODO("Not yet implemented")
    }


}


