package tw.edu.ntub.imd.birc.rxandmvvm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class loginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

//
//    fun login(view: View?) {
//        val EditTextname = findViewById<View>(R.id.name) as EditText
//        val EditTextpassword = findViewById<View>(R.id.password) as EditText
//        object : Thread() {
//            override fun run() {
//                val userDao = UserDao()
//                val aa: Boolean =
//                    userDao.login(EditTextname.text.toString(), EditTextpassword.text.toString())
//                var msg = 0
//                if (aa) {
//                    msg = 1
//                }
//                hand1.sendEmptyMessage(msg)
//            }
//        }.start()
//    }
//
//    val hand1: Handler = object : Handler() {
//        override fun handleMessage(msg: Message) {
//            if (msg.what == 1) {
//                Toast.makeText(applicationContext, "登入成功", Toast.LENGTH_LONG).show()
//            } else {
//                Toast.makeText(applicationContext, "登入失敗", Toast.LENGTH_LONG).show()
//            }
//        }
//    }
}