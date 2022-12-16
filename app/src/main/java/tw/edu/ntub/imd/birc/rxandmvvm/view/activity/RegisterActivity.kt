package tw.edu.ntub.imd.birc.rxandmvvm.view.activity

import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.get
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tw.edu.ntub.imd.birc.rxandmvvm.R
import tw.edu.ntub.imd.birc.rxandmvvm.data.PoopRecord
import tw.edu.ntub.imd.birc.rxandmvvm.data.User
import tw.edu.ntub.imd.birc.rxandmvvm.view.fragement.UserFragment
import tw.edu.ntub.imd.birc.rxandmvvm.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.*

class RegisterActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModel()
    private lateinit var name: EditText
    private lateinit var gender: Spinner
    private lateinit var birthday: EditText
    private lateinit var account: EditText
    private lateinit var password: EditText
    private lateinit var height: EditText
    private lateinit var weight: EditText
    private lateinit var registerBtn: Button
    private var genderPosition: Int = 0
    private val calender: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        name = findViewById(R.id.register_name)
        gender = findViewById(R.id.register_gender)
        birthday = findViewById(R.id.register_birthday)
        account = findViewById(R.id.register_account)
        password = findViewById(R.id.register_password)
        height = findViewById(R.id.register_height)
        weight = findViewById(R.id.register_weight)
        registerBtn = findViewById(R.id.register_btn)

        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.gender,
            android.R.layout.simple_spinner_dropdown_item
        )
        gender.adapter = adapter
        gender.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    println("error")
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    genderPosition = position
                }

            }

        birthday.setOnClickListener {
            datePicker()
        }


        registerBtn.setOnClickListener {
            val jsonName: String = name.text.toString()
            val jsonGender: String = genderPosition.toString()
            val jsonBirthday: String = birthday.text.toString()
            val jsonAccount: String = account.text.toString()
            val jsonPassword: String = password.text.toString()
            val jsonHeight: String = height.text.toString()
            val jsonWeight: String = weight.text.toString()
            val jsonObject = JSONObject()
            jsonObject.put("name", jsonName)
            jsonObject.put("gender", jsonGender)
            jsonObject.put("birthday", jsonBirthday)
            jsonObject.put("account", jsonAccount)
            jsonObject.put("password", jsonPassword)
            jsonObject.put("height", jsonHeight)
            jsonObject.put("weight", jsonWeight)
            jsonObject.put("isEmailLogin", "0")
            val jsonObjectString = jsonObject.toString()
            val requestBody =
                jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())
            viewModel.register(requestBody)
                .enqueue(object : Callback<User> {
                    override fun onResponse(
                        call: Call<User>,
                        response: Response<User>
                    ) {
                        if (response.isSuccessful) {
                            Log.d("Retrofit", "Success")
                        }
                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {
                        val sharedPreference = getSharedPreferences("LOGIN", Context.MODE_PRIVATE)
                        val editor = sharedPreference.edit()
                        editor.putString("account", jsonAccount)
                        editor.putString("password", jsonPassword)
                        editor.putString("isEmailLogin", "0")
                        editor.putString("isLogin", "1")
                        editor.apply()
                        Toast.makeText(this@RegisterActivity, "註冊成功", Toast.LENGTH_SHORT)
                            .show()
                        finish()
                        startActivity(Intent(this@RegisterActivity, HomeActivity::class.java))
                        name.setText("")
                        gender.setSelection(0)
                        birthday.setText("")
                        account.setText("")
                        password.setText("")
                        height.setText("")
                        weight.setText("")
                    }

                })
        }
    }

    private fun datePicker() {
        val year = calender.get(Calendar.YEAR)
        val month = calender.get(Calendar.MONTH)
        val day = calender.get(Calendar.DAY_OF_MONTH)
        var plusZero = "0"
        val datePickerDialog = DatePickerDialog(
            this,
            { _, yearPick, monthOfYear, dayOfMonth ->
                plusZero = if (dayOfMonth < 10) {
                    plusZero.plus(dayOfMonth)
                } else {
                    dayOfMonth.toString()
                }
                val dat = (yearPick.toString() + "/" + (monthOfYear + 1) + "/" + plusZero)
                birthday.setText(dat)
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }


}
