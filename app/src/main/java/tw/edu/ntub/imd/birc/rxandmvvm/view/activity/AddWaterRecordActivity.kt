package tw.edu.ntub.imd.birc.rxandmvvm.view.activity

import android.os.Bundle
import android.view.View
import android.widget.TextClock
import androidx.appcompat.app.AppCompatActivity
import tw.edu.ntub.imd.birc.rxandmvvm.R

class AddWaterRecordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_water_record)

        // 顯示今天日期
        val textclock:TextClock= findViewById(R.id.textclock)
        textclock.setFormat12Hour("yyyy/MM/dd");
    }

    fun back(view: View) {
        finish()
    }
}