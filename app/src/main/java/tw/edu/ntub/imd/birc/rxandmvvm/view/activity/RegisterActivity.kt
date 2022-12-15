package tw.edu.ntub.imd.birc.rxandmvvm.view.activity

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.get
import tw.edu.ntub.imd.birc.rxandmvvm.R
import tw.edu.ntub.imd.birc.rxandmvvm.view.fragement.UserFragment
import java.text.SimpleDateFormat
import java.util.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var name: EditText
    private lateinit var gender: Spinner
    private lateinit var birthday: EditText
    private lateinit var account: EditText
    private lateinit var password: EditText
    private lateinit var height: EditText
    private lateinit var weight: EditText
    private lateinit var registerBtn: Button
    private val calender: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        name = findViewById<EditText>(R.id.register_name)
        gender = findViewById<Spinner>(R.id.register_gender)
        birthday = findViewById<EditText>(R.id.register_birthday)
        account = findViewById<EditText>(R.id.register_account)
        password = findViewById<EditText>(R.id.register_password)
        height = findViewById<EditText>(R.id.register_height)
        weight = findViewById<EditText>(R.id.register_weight)
        registerBtn = findViewById<Button>(R.id.register_btn)

        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.gender,
            android.R.layout.simple_spinner_dropdown_item
        )
        gender.adapter = adapter

        birthday.setOnClickListener {
            datePicker()
        }





        registerBtn.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("name", name.text.toString())
            bundle.putString("gender", gender.selectedItemPosition.toString())
            bundle.putString("birthday", birthday.text.toString())
            bundle.putString("account", account.text.toString())
            bundle.putString("height", height.text.toString())
            bundle.putString("weight", weight.text.toString())

            val frag = UserFragment()
            frag.arguments = bundle


//            val fragmentTransaction = supportFragmentManager.beginTransaction()
//            fragmentTransaction.replace(R.id.container_activity_main, UserFragment())
//            fragmentTransaction.commit()
            val i= Intent(this, HomeActivity::class.java)
            startActivity(i)
            Toast.makeText(this, "註冊成功", Toast.LENGTH_SHORT).show()
        }
    }

    private fun datePicker() {
        val picker = DatePickerDialog(
            this,
            dateListener,
            calender.get(Calendar.YEAR),
            calender.get(Calendar.MONTH),
            calender.get(Calendar.DATE)
        )
        picker.setCancelable(true)
        picker.setCanceledOnTouchOutside(true)
        picker.setButton(
            DialogInterface.BUTTON_POSITIVE, "確定"
        ) { date, which ->
            birthday.setText("2001/11/16")
        }
        picker.setButton(
            DialogInterface.BUTTON_NEGATIVE, "取消"
        ) { _, _ -> Log.d("Picker", "Cancel!") }
        picker.show()
    }

    private val dateListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
        calender.set(year, month, day)
        format("yyyy/MM/dd HH:mm", birthday)

    }
    private fun format(format: String, view: View) {
        val time = SimpleDateFormat(format, Locale.TAIWAN)
        (view as EditText).setText(time.format(calender.time))
    }


}
