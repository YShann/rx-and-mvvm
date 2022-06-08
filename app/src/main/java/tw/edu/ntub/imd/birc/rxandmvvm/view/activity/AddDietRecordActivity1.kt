
package tw.edu.ntub.imd.birc.rxandmvvm.view.activity

//import kotlinx.android.synthetic.main.activity_add_diet_record.*

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import tw.edu.ntub.imd.birc.rxandmvvm.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class AddDietRecordActivity1 : AppCompatActivity() {

    private lateinit var cameraButton: ImageButton
    private lateinit var photoButton: ImageButton
    private lateinit var foodPhoto: ImageView
    private lateinit var dateEdit: EditText
    private lateinit var timeEdit: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_diet_record1)

        cameraButton = findViewById(R.id.cameraButton)
        photoButton = findViewById(R.id.photoButton)
        foodPhoto = findViewById(R.id.foodphoto)
        foodPhoto.scaleType = ImageView.ScaleType.CENTER_CROP

        dateEdit = findViewById(R.id.date_edit)
        timeEdit = findViewById(R.id.time_edit)
        dateEdit.setOnClickListener(listener)
        timeEdit.setOnClickListener(listener)

        //使用 camera獲取圖片
        var filePath =""
        val fileLauncher : ActivityResultLauncher<Intent> = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()){
            val option = BitmapFactory.Options()
            //option.inSampleSize = 3
            option.inSampleSize = 2
            val bitmap = BitmapFactory.decodeFile(filePath, option)
            bitmap?.let{
                handleCameraImage(bitmap)
            }
        }
        cameraButton.setOnClickListener{
            Log.d("tag", "-------------------------------1111")
            // intent to open camera app
            //val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            //launcher.launch(cameraIntent)
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val file = File.createTempFile(
                "JPEG_${timeStamp}_",
                ".jpg",
                storageDir
            )
            Log.d("tag", "-------------------------------2222")

            filePath = file.absolutePath
            Log.d("tag", filePath)
            val uri = FileProvider.getUriForFile(
                this,
                "tw.edu.ntub.imd.birc.rxandmvvm.fileprovider",
                file
            )
            Log.d("tag", uri.toString())
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
            fileLauncher.launch(intent)
        }

        // 使用album選擇圖片
        val pickLauncher = registerForActivityResult(ActivityResultContracts.GetContent()){ uri: Uri? ->
            uri?.let { it ->
                Log.d("tag", it.toString())
                val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    ImageDecoder.decodeBitmap(ImageDecoder.createSource(this.contentResolver, it))
                } else {
                    MediaStore.Images.Media.getBitmap(this.contentResolver, it)
                }
                handleCameraImage(bitmap)
            }
        }
        photoButton.setOnClickListener{
            //val intent = Intent(Intent.ACTION_GET_CONTENT)
            //intent.type = "image/*"
            pickLauncher.launch("image/*")
        }

        //暫放
        //        //使用 camera獲取圖片
//        var filePath =""
//        val fileLauncher : ActivityResultLauncher<Intent> = registerForActivityResult(
//            ActivityResultContracts.StartActivityForResult()){
//            val option = BitmapFactory.Options()
//            //option.inSampleSize = 3
//            option.inSampleSize = 2
//            val bitmap = BitmapFactory.decodeFile(filePath, option)
//            bitmap?.let{
//                handleCameraImage(bitmap)
//            }
//        }
//        cameraBtn.setOnClickListener{
//            Log.d("tag", "-------------------------------1111")
//            // intent to open camera app
//            //val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//            //launcher.launch(cameraIntent)
//            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
//            //val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//            val storageDir = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//            val file = File.createTempFile(
//                "JPEG_${timeStamp}_",
//                ".jpg",
//                storageDir
//            )
//            Log.d("tag", "-------------------------------2222")
//
//            filePath = file.absolutePath
//            Log.d("tag", filePath)
//            val uri = FileProvider.getUriForFile(
//                this.requireContext(),
//                "tw.edu.ntub.imd.birc.rxandmvvm.fileprovider",
//                file
//            )
//            Log.d("tag", uri.toString())
//            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
//            fileLauncher.launch(intent)
//        }
//
//        // 使用album選擇圖片
//        val pickLauncher = registerForActivityResult(ActivityResultContracts.GetContent()){ uri: Uri? ->
//            uri?.let { it ->
//                Log.d("tag", it.toString())
//                val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//                    ImageDecoder.decodeBitmap(ImageDecoder.createSource(requireActivity().contentResolver, it))
//                } else {
//                    MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, it)
//                }
//                handleCameraImage(bitmap)
//            }
//        }
//        albumBtn.setOnClickListener{
//            //val intent = Intent(Intent.ACTION_GET_CONTENT)
//            //intent.type = "image/*"
//            pickLauncher.launch("image/*")
//        }
    }

    //把圖片設定到foodPhoto
    private fun handleCameraImage(bitmap: Bitmap) {
        foodPhoto.setImageBitmap(bitmap)
    }


//    private fun permission() {
//        val permissionList = arrayListOf(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE)
//        var size = permissionList.size
//        var i = 0
//        while (i < size) {
//            if (ActivityCompat.checkSelfPermission(this, permissionList[i]) == PackageManager.PERMISSION_GRANTED) {
//                permissionList.removeAt(i)
//                i -= 1
//                size -= 1
//            }
//            i += 1
//        }
//        val array = arrayOfNulls<String>(permissionList.size)
//        if (permissionList.isNotEmpty()) ActivityCompat.requestPermissions(this, permissionList.toArray(array), 0)
//    }


    private val calender: Calendar = Calendar.getInstance()
    private val listener = View.OnClickListener {

        when (it) {

            dateEdit -> datePicker()

            timeEdit -> timePicker()

//            button -> {
//                Dialog()
//            }
        }
    }

    private fun datePicker() {
        DatePickerDialog(this,
            dateListener,
            calender.get(Calendar.YEAR),
            calender.get(Calendar.MONTH),
            calender.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun timePicker() {
        TimePickerDialog(this,
            timeListener,
            calender.get(Calendar.HOUR_OF_DAY),
            calender.get(Calendar.MINUTE),
            true
        ).show()
    }

    private val dateListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
        calender.set(year, month, day)
        format("yyyy / MM / dd", dateEdit)
    }

    private val timeListener = TimePickerDialog.OnTimeSetListener { _, hour, min->
        calender.set(Calendar.HOUR_OF_DAY, hour)
        calender.set(Calendar.MINUTE, min)
        format("HH : mm", timeEdit)
    }

    private fun format(format: String, view: View) {
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
