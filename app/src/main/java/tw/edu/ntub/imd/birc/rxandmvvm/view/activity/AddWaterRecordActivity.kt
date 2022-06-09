package tw.edu.ntub.imd.birc.rxandmvvm.view.activity

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextClock
import androidx.appcompat.app.AppCompatActivity
import tw.edu.ntub.imd.birc.rxandmvvm.R



class AddWaterRecordActivity : AppCompatActivity() {

    private lateinit var waterIntake: EditText
    private lateinit var addBtn: ImageButton
    private lateinit var reduceBtn: ImageButton
    var water : Int = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_water_record)

        waterIntake = findViewById(R.id.waterIntake)
        addBtn = findViewById(R.id.addBtn)
        reduceBtn = findViewById(R.id.reduceBtn)

        // 顯示今天日期
        val textClock:TextClock= findViewById(R.id.textclock)
        textClock.format12Hour = "yyyy/MM/dd"



    }


    fun back(view: View) {
        finish()
    }

    fun addWater(view: View) {
        water = waterIntake.text.toString().toInt()
        water += 100
        waterIntake.setText(water.toString())
    }

    fun reduceWater(view: View) {
        water = waterIntake.text.toString().toInt()
        water -= 100
        waterIntake.setText(water.toString())
    }

    fun finsh(view: View) {
        finish()
    }
}