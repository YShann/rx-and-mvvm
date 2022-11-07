package tw.edu.ntub.imd.birc.rxandmvvm

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity


class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

//    if(SaveSharedPreference.getUserName(MainActivity.this).length() == 0)
//    {
//        // call Login Activity
//    }
//    else
//    {
//        // Stay at the current activity.
//    }

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

//object SaveSharedPreference {
//    const val PREF_USER_NAME = "username"
//    fun getSharedPreferences(ctx: Context?): SharedPreferences {
//        return PreferenceManager.getDefaultSharedPreferences(ctx)
//    }
//
//    fun setUserName(ctx: Context?, userName: String?) {
//        val editor: Editor = getSharedPreferences(ctx).edit()
//        editor.putString(PREF_USER_NAME, userName)
//        editor.commit()
//    }
//
//    fun getUserName(ctx: Context?): String? {
//        return getSharedPreferences(ctx).getString(PREF_USER_NAME, "")
//    }
//}