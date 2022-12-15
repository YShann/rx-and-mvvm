package tw.edu.ntub.imd.birc.rxandmvvm.view.fragement

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
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
import tw.edu.ntub.imd.birc.rxandmvvm.extension.attachToRecyclerView
import tw.edu.ntub.imd.birc.rxandmvvm.extension.mapSourceState
import tw.edu.ntub.imd.birc.rxandmvvm.view.activity.AddWaterRecordActivity
import tw.edu.ntub.imd.birc.rxandmvvm.view.activity.HomeActivity
import tw.edu.ntub.imd.birc.rxandmvvm.view.activity.MainActivity
import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.ObservableAdapter
import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.item.FoodItem
import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.item.UserItem
import tw.edu.ntub.imd.birc.rxandmvvm.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.*


class UserFragment : Fragment() {
    private val viewModel: MainViewModel by viewModel()
    private lateinit var profileRecyclerInvisibility: RecyclerView
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var profileReturn: ImageButton
    private lateinit var profileEdit: ImageButton
    private lateinit var profileEmail: TextView
    private lateinit var profileAccount: TextView
    private lateinit var profileName: TextView
    private lateinit var profileGender: TextView
    private lateinit var profileBirthday: TextView
    private lateinit var profileHeight: TextView
    private lateinit var profileWeight: TextView
    private lateinit var editProfileName: EditText
    private lateinit var editProfileGender: Spinner
    private lateinit var editProfileBirthday: EditText
    private lateinit var editProfileHeight: EditText
    private lateinit var editProfileWeight: EditText
    private lateinit var editProfilePassword: EditText
    private lateinit var textProfileName: TextView
    private lateinit var textProfileGender: TextView
    private lateinit var textProfileBirthday: TextView
    private lateinit var textProfileHeight: TextView
    private lateinit var textProfileWeight: TextView
    private lateinit var textProfilePassword: TextView
    private var isProfileEdit: Boolean = false
    private val calender: Calendar = Calendar.getInstance()
    private val genderList: MutableList<String> =
        mutableListOf("男", "女")
    private var genderPosition: Int = 0
    private val sharedPreference =
        this.activity?.getSharedPreferences("LOGIN", Context.MODE_PRIVATE)
    private var isEmailLogin = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user, container, false)
        profileRecyclerInvisibility = view.findViewById(R.id.profile_recycler_invisibility)
        profileReturn = view.findViewById(R.id.profile_return)
        profileEdit = view.findViewById(R.id.profile_edit)
//        profileEmail = view.findViewById(R.id.profile_account)
        profileAccount = view.findViewById(R.id.profile_account)
        profileGender = view.findViewById(R.id.profile_gender)
        profileBirthday = view.findViewById(R.id.profile_birthday)
        profileHeight = view.findViewById(R.id.profile_height)
        profileWeight = view.findViewById(R.id.profile_weight)
        profileName = view.findViewById(R.id.profile_name)
        editProfileGender = view.findViewById(R.id.edit_profile_gender)
        editProfileBirthday = view.findViewById(R.id.edit_profile_birthday)
        editProfileHeight = view.findViewById(R.id.edit_profile_height)
        editProfileWeight = view.findViewById(R.id.edit_profile_weight)
        editProfileName = view.findViewById(R.id.edit_profile_name)
        editProfilePassword = view.findViewById(R.id.edit_profile_password)
        textProfileName = view.findViewById(R.id.text_profile_name)
        textProfileGender = view.findViewById(R.id.text_profile_gender)
        textProfileBirthday = view.findViewById(R.id.text_profile_birthday)
        textProfileHeight = view.findViewById(R.id.text_profile_height)
        textProfileWeight = view.findViewById(R.id.text_profile_weight)
        textProfilePassword = view.findViewById(R.id.text_profile_password)

        profileRecyclerInvisibility.visibility = View.INVISIBLE
        profileReturn.setOnClickListener {
            requireActivity().run {
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
        }

        val genderAdapter = activity?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.profile_gender,
                android.R.layout.simple_spinner_item
            )
        }
        genderAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        editProfileGender.adapter = genderAdapter
        editProfileGender.onItemSelectedListener =
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

        val sharedPreference = this.activity?.getSharedPreferences("LOGIN", Context.MODE_PRIVATE)
        val account = sharedPreference?.getString("account", "defaultAccount")
        if (sharedPreference?.getString("isEmailLogin", "default") == "0") {
            val adapter = ObservableAdapter(
                viewModel.getUser(account!!)
                    .mapSourceState {
                        it.data.map { user ->
                            profileName.text = user.name
                            profileGender.text = genderList[user.gender?.toInt()!!]
                            genderPosition = user.gender!!.toInt()
                            isEmailLogin = user.isEmailLogin.toString()
                            profileBirthday.text = user.birthday
                            profileAccount.text = user.account
                            profileHeight.text = user.height.toString()
                            profileWeight.text = user.weight.toString()
                        }
                        it.data.map { user -> UserItem(user) }
                    }
            )
            adapter.attachToRecyclerView(profileRecyclerInvisibility)
        }



        firebaseAuth = FirebaseAuth.getInstance()
//        profileEmail.text= firebaseAuth.currentUser?.email
//        checkUser()
//        Log.d("User",arguments?.getString("name").toString())
//        Log.d("User",requireArguments().getString("name").toString())


        editProfileGender.visibility = View.INVISIBLE
        editProfileBirthday.visibility = View.INVISIBLE
        editProfileHeight.visibility = View.INVISIBLE
        editProfileWeight.visibility = View.INVISIBLE
        editProfileName.visibility = View.INVISIBLE
        editProfilePassword.visibility = View.INVISIBLE
        textProfilePassword.visibility = View.INVISIBLE

        if (sharedPreference?.getString("isEmailLogin", "default") == "0") {
            profileEdit.visibility = View.VISIBLE
            profileEdit.setBackgroundResource(R.drawable.ic_edit_img)
            textProfileName.visibility = View.VISIBLE
            textProfileGender.visibility = View.VISIBLE
            textProfileBirthday.visibility = View.VISIBLE
            textProfileHeight.visibility = View.VISIBLE
            textProfileWeight.visibility = View.VISIBLE
            textProfilePassword.visibility = View.VISIBLE
            profileGender.visibility = View.VISIBLE
            profileBirthday.visibility = View.VISIBLE
            profileHeight.visibility = View.VISIBLE
            profileWeight.visibility = View.VISIBLE
            profileName.visibility = View.VISIBLE
        }else{
            profileAccount.text = account
            profileEdit.visibility = View.INVISIBLE
            textProfileName.visibility = View.INVISIBLE
            textProfileGender.visibility = View.INVISIBLE
            textProfileBirthday.visibility = View.INVISIBLE
            textProfileHeight.visibility = View.INVISIBLE
            textProfileWeight.visibility = View.INVISIBLE
            textProfilePassword.visibility = View.INVISIBLE
            profileGender.visibility = View.INVISIBLE
            profileBirthday.visibility = View.INVISIBLE
            profileHeight.visibility = View.INVISIBLE
            profileWeight.visibility = View.INVISIBLE
            profileName.visibility = View.INVISIBLE
        }
        profileEdit.setOnClickListener {
            if (isProfileEdit) {
                profileEdit.setBackgroundResource(R.drawable.ic_edit_img)
                isProfileEdit = false
                profileGender.visibility = View.VISIBLE
                profileBirthday.visibility = View.VISIBLE
                profileHeight.visibility = View.VISIBLE
                profileWeight.visibility = View.VISIBLE
                profileName.visibility = View.VISIBLE
                editProfileGender.visibility = View.INVISIBLE
                editProfileBirthday.visibility = View.INVISIBLE
                editProfileHeight.visibility = View.INVISIBLE
                editProfileWeight.visibility = View.INVISIBLE
                editProfileName.visibility = View.INVISIBLE
                editProfilePassword.visibility = View.INVISIBLE
                textProfilePassword.visibility = View.INVISIBLE
                profileName.text = editProfileName.text
                profileHeight.text = editProfileHeight.text
                profileWeight.text = editProfileWeight.text
                profileBirthday.text = editProfileBirthday.text
                profileGender.text = genderList[genderPosition]
                val name: String = profileName.text.toString()
                val gender: String = genderPosition.toString()
                val height: Double = profileHeight.text.toString().toDouble()
                val weight: Double = profileWeight.text.toString().toDouble()
                val isEmailLogin: String = isEmailLogin.toString()
                val birthday: String = profileBirthday.text.toString()
                val account: String = sharedPreference?.getString("account", "defaultAccount")!!
                val password: String = editProfilePassword.text.toString()

                val jsonObject = JSONObject()
                jsonObject.put("name", name)
                jsonObject.put("gender", gender)
                jsonObject.put("height", height)
                jsonObject.put("weight", weight)
                jsonObject.put("isEmailLogin", isEmailLogin)
                jsonObject.put("birthday", birthday)
                jsonObject.put("account", account)
                jsonObject.put("password", password)
                val jsonObjectString = jsonObject.toString()
                val requestBody =
                    jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())
                viewModel.editUser(requestBody)
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
                            Toast.makeText(context, "修改成功", Toast.LENGTH_SHORT).show()
                        }

                    })
            } else {
                profileEdit.setBackgroundResource(R.drawable.ic_diet_record_check)
                isProfileEdit = true

                profileGender.visibility = View.INVISIBLE
                profileBirthday.visibility = View.INVISIBLE
                profileHeight.visibility = View.INVISIBLE
                profileWeight.visibility = View.INVISIBLE
                profileName.visibility = View.INVISIBLE
                editProfileGender.visibility = View.VISIBLE
                editProfileBirthday.visibility = View.VISIBLE
                editProfileHeight.visibility = View.VISIBLE
                editProfileWeight.visibility = View.VISIBLE
                editProfileName.visibility = View.VISIBLE
                editProfilePassword.visibility = View.VISIBLE
                textProfilePassword.visibility = View.VISIBLE
                editProfileName.setText(profileName.text)
                editProfileBirthday.setText(profileBirthday.text)
                editProfileHeight.setText(profileHeight.text)
                editProfileWeight.setText(profileWeight.text)

                editProfilePassword.setText(
                    sharedPreference?.getString(
                        "password",
                        "defaultPassword"
                    )!!
                )
                editProfileGender.setSelection(genderPosition)

                editProfileBirthday.setOnClickListener {
                    this.datePicker()
                }

            }

        }


        return view
    }

//    private fun checkUser() {
//        val firebaseUser = firebaseAuth.currentUser
//        if(firebaseUser==null){
//            requireActivity().run{
//                startActivity(Intent(this, MainActivity::class.java))
//                finish()
//            }
//        }else{
//            val email = firebaseUser.email
//        }
//    }

    private fun datePicker() {
        val year = calender.get(Calendar.YEAR)
        val month = calender.get(Calendar.MONTH)
        val day = calender.get(Calendar.DAY_OF_MONTH)
        var plusZero = "0"
        val datePickerDialog = DatePickerDialog(
            requireActivity(),
            { _, yearPick, monthOfYear, dayOfMonth ->
                plusZero = if (dayOfMonth < 10) {
                    plusZero.plus(dayOfMonth)
                } else {
                    dayOfMonth.toString()
                }
                val dat = (yearPick.toString() + "/" + (monthOfYear + 1) + "/" + plusZero)
                editProfileBirthday.setText(dat)
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

}