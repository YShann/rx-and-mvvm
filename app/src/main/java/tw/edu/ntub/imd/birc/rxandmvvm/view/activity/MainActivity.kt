package tw.edu.ntub.imd.birc.rxandmvvm.view.activity

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import tw.edu.ntub.imd.birc.rxandmvvm.R
import tw.edu.ntub.imd.birc.rxandmvvm.view.fragement.*

class MainActivity : AppCompatActivity() {

    companion object {
        val homeFragment = HomeFragment()
        val recordFragment = RecordFragment()
        val userFragemnt = UserFragment()
        val addFragment = AddFragment()
        val otherFragment = OtherFragment()

    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navigationview = findViewById<BottomNavigationView>(R.id.main_navigationView)

        //設定初始時要show哪個fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_activity_main, homeFragment)
            .commit()
        navigationview.selectedItemId = R.id.f1
        navigationview.setOnNavigationItemSelectedListener(listener)



    }

    private var listener = object : BottomNavigationView.OnNavigationItemSelectedListener {
        //設定navigationBar裡的item們的點擊事件 被點擊後採動態載入fragment的方式
        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.f1 -> {
                    val manager = supportFragmentManager
                    val transaction = manager.beginTransaction()
                    transaction.replace(R.id.container_activity_main, homeFragment).commit()

                }
                R.id.f2 -> {
                    val t = supportFragmentManager.beginTransaction()
                    t.replace(R.id.container_activity_main, recordFragment).commit()
                }
                R.id.f3 -> {
                    val t = supportFragmentManager.beginTransaction()
                    t.replace(R.id.container_activity_main, userFragemnt).commit()
                }

                R.id.f4 -> {
                    val t = supportFragmentManager.beginTransaction()
                    t.replace(R.id.container_activity_main, otherFragment).commit()
                }

                R.id.f5 -> {
                    val t = supportFragmentManager.beginTransaction()
                    t.replace(R.id.container_activity_main, addFragment).commit()

                }
            }
            return true
        }
    }

    //新版button.setOnClickListener的實作方法(歷史測試用按鈕)
    fun btn_onClik(view: View) {
        //val button: Button = findViewById(R.id.button1),好像不用
        //button.setOnClickListener {  },好像不用了刪掉也沒報錯
            startActivity(Intent(this, MainActivity2::class.java))

    }


    //開啟相機
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


}