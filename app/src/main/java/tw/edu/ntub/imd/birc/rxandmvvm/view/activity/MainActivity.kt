package tw.edu.ntub.imd.birc.rxandmvvm.view.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.internal.NavigationMenu
import com.google.android.material.internal.NavigationMenuItemView
import com.google.android.material.internal.NavigationMenuView
import com.google.android.material.navigation.NavigationView
import org.koin.androidx.viewmodel.ext.android.viewModel
import tw.edu.ntub.imd.birc.rxandmvvm.R
import tw.edu.ntub.imd.birc.rxandmvvm.extension.attachToRecyclerView
import tw.edu.ntub.imd.birc.rxandmvvm.extension.mapSourceState
import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.ObservableAdapter
import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.item.UserItem
import tw.edu.ntub.imd.birc.rxandmvvm.view.fragement.HomeFragment
import tw.edu.ntub.imd.birc.rxandmvvm.view.fragement.OtherFragment
import tw.edu.ntub.imd.birc.rxandmvvm.view.fragement.RecordFragment
import tw.edu.ntub.imd.birc.rxandmvvm.view.fragement.UserFragment
import tw.edu.ntub.imd.birc.rxandmvvm.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    companion object {
        val homeFragment = HomeFragment()
        val recordFragment = RecordFragment()
        val userFragemnt = UserFragment()
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
            }
            return true
        }
    }
}