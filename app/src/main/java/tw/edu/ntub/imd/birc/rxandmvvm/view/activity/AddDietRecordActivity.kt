package tw.edu.ntub.imd.birc.rxandmvvm.view.activity

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import tw.edu.ntub.imd.birc.rxandmvvm.R

class AddDietRecordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_diet_record)



    }

    fun camera(view: View) {
        val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(i,123)
    }

    fun album(view: View) {
        val i = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(i,456)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==123)
        {
            var bmp = data?.extras?.get("data") as Bitmap
            val foodphoto: ImageView = findViewById(R.id.foodphoto)
            foodphoto.setImageBitmap(bmp)
        }else if(requestCode==456){
            val foodphoto: ImageView = findViewById(R.id.foodphoto)
            foodphoto.setImageURI(data?.data)
        }
    }

    fun back(view: View) {
        finish()
    }


}