package tw.edu.ntub.imd.birc.rxandmvvm.view.activity

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import tw.edu.ntub.imd.birc.rxandmvvm.R

class BMIcalculateActivity : AppCompatActivity() {

    private lateinit var showbmi: TextView
    private lateinit var result: TextView
    private var height : Double = 160.5
    private var weight : Double = 99.9
    private var bmi : Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmicalculate)
        showbmi = findViewById(R.id.showbmi)
        result = findViewById(R.id.result)

        bmi = weight/((height/100)*(height/100))
        showbmi.text = String.format("%.1f",bmi)

        if (bmi < 18.5) {
            result.text = "「體重過輕」，需要再吃營養些，讓自己重一些！！"
        }
        else if (bmi >= 18.5 && bmi < 24) {
            result.text = "很不錯喔！「健康體重」，請您繼續保持！"
        }
        else if (bmi >= 24 && bmi < 27) {
            result.text = "「過重」，您得控制一下飲食了，請加油！"
        }
        else if (bmi >= 27 && bmi < 30) {
            result.text = "「輕度肥胖」，肥胖容易引起疾病，您得要多多注意自己的健康囉！"
        }
        else if (bmi >= 30 && bmi < 35) {
            result.text = "「中度肥胖」，肥胖容易引起疾病，您得要多多注意自己的健康囉！"
        }
        else if (bmi >= 35) {
            result.text = "「重度肥胖」，肥胖容易引起疾病，您得要多多注意自己的健康囉！"
        }

    }
}

