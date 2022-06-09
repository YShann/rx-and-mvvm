package tw.edu.ntub.imd.birc.rxandmvvm.view.activity

import android.Manifest
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import tw.edu.ntub.imd.birc.rxandmvvm.R
import tw.edu.ntub.imd.birc.rxandmvvm.view.fragement.*


class MainActivity : AppCompatActivity() {


    companion object {
        val homeFragment = HomeFragment()
        //原本是val recordFragment = RecordFragment()被警告有內存洩漏的問題，按照建議改成下面那行
        val recordFragment by lazy { RecordFragment() }
        val userFragemnt = UserFragment()
        val addFragment = AddFragment()
        val otherFragment = OtherFragment()
        //原本是val createDietRecordFragment = CreateDietRecordFragment()被警告有內存洩漏的問題，按照建議改成下面那行
        val createDietRecordFragment by lazy { CreateDietRecordFragment() }

        private const val MY_PERMISSIONS_REQUEST_READ_CONTACTS = 100
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        if (ContextCompat.checkSelfPermission(this@MainActivity,  Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this@MainActivity,       Manifest.permission.CAMERA)) {
                AlertDialog.Builder(this@MainActivity)
                    .setMessage("本APP需要相機權限才可以運作, 請開啟權限")
                    .setPositiveButton("OK") { _, _ ->
                        ActivityCompat.requestPermissions(this@MainActivity,
                            arrayOf(Manifest.permission.CAMERA),
                            MY_PERMISSIONS_REQUEST_READ_CONTACTS)
                    }
                    .setNegativeButton("No") { _, _ -> finish() }
                    .show()
            } else {
                ActivityCompat.requestPermissions(this@MainActivity,
                    arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS)
            }
        }else if(ContextCompat.checkSelfPermission(this@MainActivity,  READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this@MainActivity,       READ_EXTERNAL_STORAGE)) {
                AlertDialog.Builder(this@MainActivity)
                    .setMessage("本APP需要存取權限才可以運作, 請開啟權限")
                    .setPositiveButton("OK") { _, _ ->
                        ActivityCompat.requestPermissions(this@MainActivity,
                            arrayOf(READ_EXTERNAL_STORAGE),
                            MY_PERMISSIONS_REQUEST_READ_CONTACTS)
                    }
                    .setNegativeButton("No") { _, _ -> finish() }
                    .show()
            } else {
                ActivityCompat.requestPermissions(this@MainActivity,
                    arrayOf(Manifest.permission.CAMERA, READ_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS)
            }
        }


        val navigationview = findViewById<BottomNavigationView>(R.id.main_navigationView)


        //設定初始時要show哪個fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_activity_main, homeFragment)
            .commit()
        navigationview.selectedItemId = R.id.f1
        navigationview.setOnNavigationItemSelectedListener(listener)



    }

    override fun onRequestPermissionsResult(requestCode: Int,  permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_READ_CONTACTS -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    finish()
                }
                return
            }
        }
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
                    t.replace(R.id.container_activity_main, addFragment).commit()
                }

                R.id.f4 -> {
                    val t = supportFragmentManager.beginTransaction()
                    t.replace(R.id.container_activity_main, userFragemnt).commit()
                }

                R.id.f5 -> {
                    val t = supportFragmentManager.beginTransaction()
                    t.replace(R.id.container_activity_main, otherFragment).commit()
                }
            }
            return true
        }
    }



    //新版button.setOnClickListener的實作方法(歷史測試用按鈕)
    fun btn_onClik(view: View) {
        //val button: Button = findViewById(R.id.button1),好像不用
        //button.setOnClickListener {  },好像不用了刪掉也沒報錯
            startActivity(Intent(this, FoodDetailActivity::class.java))

    }

    //切到添加飲食紀錄的畫面
    fun goBtn1(view: View) {
        startActivity(Intent(this, AddDietRecordActivity::class.java))
    }
    //切到添加飲水紀錄的畫面
    fun goBtn2(view: View) {
        startActivity(Intent(this, AddWaterRecordActivity::class.java))
    }




}