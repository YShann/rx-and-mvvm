@file:Suppress("UnusedImport")

package tw.edu.ntub.imd.birc.rxandmvvm.view.activity

//import kotlinx.android.synthetic.main.activity_add_diet_record.*

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import tw.edu.ntub.imd.birc.rxandmvvm.R

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

    }

    // 通過 intent 使用 Camera
    private fun takeImageFromCameraWithIntent() {
        println("take image from camera")

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, ACTION_CAMERA_REQUEST_CODE)
    }

    // 通過 intent 使用 album
    private fun takeImageFromAlbumWithIntent() {
        println("take image from album")

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

    private fun displayImage(bitmap: Bitmap) {
        val foodphoto: ImageView = findViewById(R.id.foodphoto)

        foodphoto.setImageBitmap(bitmap)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        println("收到 result code $requestCode")

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

    fun back(view: View) {
        finish()
    }
}