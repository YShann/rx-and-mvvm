@file:Suppress("UnusedImport")

package tw.edu.ntub.imd.birc.rxandmvvm.view.activity

//import kotlinx.android.synthetic.main.activity_add_diet_record.*

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import tw.edu.ntub.imd.birc.rxandmvvm.R
import tw.edu.ntub.imd.birc.rxandmvvm.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*



class AddDietRecordActivity : AppCompatActivity() {

    private val ACTION_CAMERA_REQUEST_CODE = 100
    private val ACTION_ALBUM_REQUEST_CODE = 200


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_diet_record)

        val cameraButton: ImageButton = findViewById(R.id.cameraButton)
        cameraButton.setOnClickListener(cameraAppButtonHandler)
        val photoButton: ImageButton = findViewById(R.id.photoButton)
        photoButton.setOnClickListener(albumAppButtonHandler)
        val foodphoto: ImageView = findViewById(R.id.foodphoto)
        foodphoto.scaleType = ImageView.ScaleType.CENTER_CROP

        val dateEdit: EditText = findViewById(R.id.date_edit)
        val timeEdit: EditText = findViewById(R.id.time_edit)
        dateEdit.setOnClickListener(listener)
        timeEdit.setOnClickListener(listener)

    }


    // 通過 intent 使用 Camera
    private fun takeImageFromCameraWithIntent() {

            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, ACTION_CAMERA_REQUEST_CODE)


    }

    // 通過 intent 使用 album
    private fun takeImageFromAlbumWithIntent() {


        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, ACTION_ALBUM_REQUEST_CODE)
    }

    private val cameraAppButtonHandler = View.OnClickListener { view ->
        takeImageFromCameraWithIntent()
    }

    private val albumAppButtonHandler = View.OnClickListener { view ->
        takeImageFromAlbumWithIntent()
    }

    //顯示圖片在foodphoto上
    private fun displayImage(bitmap: Bitmap) {
        val foodphoto: ImageView = findViewById(R.id.foodphoto)

        foodphoto.setImageBitmap(bitmap)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //println("收到 result code $requestCode")

        when (requestCode) {
            ACTION_CAMERA_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    displayImage(data.extras?.get("data") as Bitmap)
                }
            }

            ACTION_ALBUM_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val resolver = this.contentResolver
                    val bitmap = MediaStore.Images.Media.getBitmap(resolver, data.data)
                    displayImage(bitmap)

                }
            }
            else -> {
                println("no handler onActivityReenter")
            }
        }

//    fun camera(view: View) {
//        val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        startActivityForResult(i,123)
//    }
//
//    fun album(view: View) {
//        val i = Intent(Intent.ACTION_PICK)
//        intent.type = "image/*"
//        startActivityForResult(i,456)
//
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode==123)
//        {
//            var bmp = data?.extras?.get("data") as Bitmap
//            val foodphoto: ImageView = findViewById(R.id.foodphoto)
//            foodphoto.setImageBitmap(bmp)
//        }else if(requestCode==456){
//            val foodphoto: ImageView = findViewById(R.id.foodphoto)
//            foodphoto.setImageURI(data?.data)
//        }
//    }


    }

    private val calender: Calendar = Calendar.getInstance()

    private val listener = View.OnClickListener {
        val dateEdit: EditText = findViewById(R.id.date_edit)
        val timeEdit: EditText = findViewById(R.id.time_edit)
        when (it) {

            dateEdit -> {
                datePicker()
            }

            timeEdit -> {
                timePicker()
            }

//            button -> {
//                Dialog()
//            }
        }
    }

    fun datePicker() {
        DatePickerDialog(this,
            dateListener,
            calender.get(Calendar.YEAR),
            calender.get(Calendar.MONTH),
            calender.get(Calendar.DAY_OF_MONTH)).show()
    }

    fun timePicker() {
        TimePickerDialog(this,
            timeListener,
            calender.get(Calendar.HOUR_OF_DAY),
            calender.get(Calendar.MINUTE),
            true
        ).show()
    }

    val dateListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
        calender.set(year, month, day)
        val dateEdit: EditText = findViewById(R.id.date_edit)
        format("yyyy / MM / dd", dateEdit)
    }

    val timeListener = TimePickerDialog.OnTimeSetListener { _, hour, min->
        calender.set(Calendar.HOUR_OF_DAY, hour)
        calender.set(Calendar.MINUTE, min)
        val timeEdit: EditText = findViewById(R.id.time_edit)
        format("HH : mm", timeEdit)
    }

    fun format(format: String, view: View) {
        val time = SimpleDateFormat(format, Locale.TAIWAN)
        (view as EditText).setText(time.format(calender.time))
    }

    //用一個小視窗顯示日期時間
//    fun Dialog(){
//        AlertDialog.Builder(this)
//            .setTitle("Your Time")
//            .setMessage("${date_edit.text}   ${time_edit.text}")
//            .setNegativeButton("OK"){ dialog, which ->
//                dialog.cancel()
//            }.create().show()
//    }

    fun back(view: View) {
        finish()
    }
}

