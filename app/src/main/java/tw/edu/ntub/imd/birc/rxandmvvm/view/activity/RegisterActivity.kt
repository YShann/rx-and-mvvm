package tw.edu.ntub.imd.birc.rxandmvvm

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity


class RegisterActivity : AppCompatActivity() {

    var name: EditText? = null
    var username: EditText? = null
    var password: EditText? = null
    var phone: EditText? = null
    var age: EditText? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        name = findViewById(R.id.name)
        username = findViewById(R.id.username);
        password = findViewById(R.id.password)
        phone = findViewById(R.id.phone);
        age = findViewById(R.id.age);
    }

//    fun register(view: View?) {
//        val cname = name!!.text.toString()
//        val cusername = username!!.text.toString()
//        val cpassword = password!!.text.toString()
//        println(phone!!.text.toString())
//        val cphone = phone!!.text.toString()
//        val cgae = age!!.text.toString().toInt()
//        if (cname.length < 2 || cusername.length < 2 || cpassword.length < 2) {
//            Toast.makeText(applicationContext, "輸入資訊不符合要求請重新輸入", Toast.LENGTH_LONG).show()
//            return
//        }
//        val user = User()
//        user.setName(cname)
//        user.setUsername(cusername)
//        user.setPassword(cpassword)
//        user.setAge(cgae)
//        user.setPhone(cphone)
//        object : Thread() {
//            override fun run() {
//                var msg = 0
//                val userDao = UserDao()
//                val uu: User = userDao.findUser(user.getName())
//                if (uu != null) {
//                    msg = 1
//                }
//                val flag: Boolean = userDao.register(user)
//                if (flag) {
//                    msg = 2
//                }
//                hand.sendEmptyMessage(msg)
//            }
//        }.start()
//    }
//
//    val hand: Handler = object : Handler() {
//        override fun handleMessage(msg: Message) {
//            if (msg.what == 0) {
//                Toast.makeText(applicationContext, "註冊失敗", Toast.LENGTH_LONG).show()
//            }
//            if (msg.what == 1) {
//                Toast.makeText(applicationContext, "該賬號已經存在，請換一個賬號", Toast.LENGTH_LONG).show()
//            }
//            if (msg.what == 2) {
//                //startActivity(new Intent(getApplication(),MainActivity.class));
//                val intent = Intent()
//                //將想要傳遞的資料用putExtra封裝在intent中
//                intent.putExtra("a", "註冊")
//                setResult(RESULT_CANCELED, intent)
//                finish()
//            }
//        }
//    }
}