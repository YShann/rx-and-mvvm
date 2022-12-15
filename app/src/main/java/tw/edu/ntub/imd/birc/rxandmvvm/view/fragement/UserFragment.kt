package tw.edu.ntub.imd.birc.rxandmvvm.view.fragement

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import tw.edu.ntub.imd.birc.rxandmvvm.R
import tw.edu.ntub.imd.birc.rxandmvvm.extension.attachToRecyclerView
import tw.edu.ntub.imd.birc.rxandmvvm.extension.mapSourceState
import tw.edu.ntub.imd.birc.rxandmvvm.view.activity.AddWaterRecordActivity
import tw.edu.ntub.imd.birc.rxandmvvm.view.activity.HomeActivity
import tw.edu.ntub.imd.birc.rxandmvvm.view.activity.MainActivity
import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.ObservableAdapter
import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.item.FoodItem
import java.text.SimpleDateFormat
import java.util.*


class UserFragment : Fragment() {

    private lateinit var firebaseAuth:FirebaseAuth
    private lateinit var profileReturn:ImageButton
    private lateinit var profileEdit:ImageButton
    private lateinit var profileEmail:TextView
    private lateinit var profileAccount:TextView
    private lateinit var profileName:TextView
    private lateinit var profileGender:TextView
    private lateinit var profileBirthday:TextView
    private lateinit var profileHeight:TextView
    private lateinit var profileWeight:TextView
    private lateinit var editProfileName:EditText
    private lateinit var editProfileGender:Spinner
    private lateinit var editProfileBirthday:EditText
    private lateinit var editProfileHeight:EditText
    private lateinit var editProfileWeight:EditText
    private var isProfileEdit: Boolean = false
    private val calender: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user, container, false)
        profileReturn = view.findViewById<ImageButton>(R.id.profile_return)
        profileEdit = view.findViewById<ImageButton>(R.id.profile_edit)
//        profileEmail = view.findViewById<TextView>(R.id.profile_account)
        profileAccount = view.findViewById<TextView>(R.id.profile_account)
        profileGender = view.findViewById<TextView>(R.id.profile_gender)
        profileBirthday = view.findViewById<TextView>(R.id.profile_birthday)
        profileHeight = view.findViewById<TextView>(R.id.profile_height)
        profileWeight = view.findViewById<TextView>(R.id.profile_weight)
        profileName = view.findViewById<TextView>(R.id.profile_name)
        editProfileGender = view.findViewById(R.id.edit_profile_gender)
        editProfileBirthday = view.findViewById(R.id.edit_profile_birthday)
        editProfileHeight = view.findViewById(R.id.edit_profile_height)
        editProfileWeight = view.findViewById(R.id.edit_profile_weight)
        editProfileName = view.findViewById(R.id.edit_profile_name)

        profileReturn.setOnClickListener {
            requireActivity().run {
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
        }

        firebaseAuth = FirebaseAuth.getInstance()
//        profileEmail.text= firebaseAuth.currentUser?.email
//        checkUser()
//        Log.d("User",arguments?.getString("name").toString())
//        Log.d("User",requireArguments().getString("name").toString())
        profileName.text= "王小明"
        profileGender.text= "男"
        profileBirthday.text= "2001/11/16"
        profileAccount.text= "abcd(帳號無法更改)"
        profileHeight.text= "180"
        profileWeight.text= "70"

        editProfileGender.visibility = View.INVISIBLE
        editProfileBirthday.visibility = View.INVISIBLE
        editProfileHeight.visibility = View.INVISIBLE
        editProfileWeight.visibility = View.INVISIBLE
        editProfileName.visibility = View.INVISIBLE

        profileEdit.setBackgroundResource(R.drawable.ic_edit_img)
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
                profileName.text = editProfileName.text
                profileHeight.text = editProfileHeight.text
                profileWeight.text = editProfileWeight.text
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
                editProfileName.setText(profileName.text)
                editProfileHeight.setText(profileHeight.text)
                editProfileWeight.setText(profileWeight.text)

                val adapter = activity?.let {
                    ArrayAdapter.createFromResource(
                        it,
                        R.array.profile_gender,
                        android.R.layout.simple_spinner_item
                    )
                }
                adapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                editProfileGender.adapter = adapter
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
                            val type = parent?.getItemAtPosition(position).toString()
                            profileGender.text = type
                            println(type)
                        }

                    }
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
        val picker = context?.let {
            DatePickerDialog(
                it,
                dateListener,
                calender.get(Calendar.YEAR),
                calender.get(Calendar.MONTH),
                calender.get(Calendar.DATE)
            )
        }
        picker?.setCancelable(true)
        picker?.setCanceledOnTouchOutside(true)
        picker?.setButton(
            DialogInterface.BUTTON_POSITIVE, "確定"
        ) { date, which ->
            this.timePicker()
        }
        picker?.setButton(
            DialogInterface.BUTTON_NEGATIVE, "取消"
        ) { _, _ -> Log.d("Picker", "Cancel!") }
        picker?.show()
    }


    private fun timePicker() {
        val timePicker = context?.let {
            TimePickerDialog(
                it,
                timeListener,  // instead of a listener
                calender.get(Calendar.HOUR_OF_DAY), calender.get(Calendar.MINUTE), true
            )
        }
        timePicker?.setCancelable(true)
        timePicker?.setCanceledOnTouchOutside(true)
        timePicker?.show()
    }

    private val dateListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
        calender.set(year, month, day)
    }

    private val timeListener = TimePickerDialog.OnTimeSetListener { _, hour, min ->
        calender.set(Calendar.HOUR_OF_DAY, hour)
        calender.set(Calendar.MINUTE, min)
        format("yyyy/MM/dd HH:mm", editProfileBirthday)
        textViewFormat("yyyy/MM/dd HH:mm", profileBirthday)
    }

    private fun format(format: String, view: View) {
        val time = SimpleDateFormat(format, Locale.TAIWAN)
        (view as EditText).setText(time.format(calender.time))
    }

    private fun textViewFormat(format: String, view: View) {
        val time = SimpleDateFormat(format, Locale.TAIWAN)
        (view as TextView).text = time.format(calender.time)
    }
}