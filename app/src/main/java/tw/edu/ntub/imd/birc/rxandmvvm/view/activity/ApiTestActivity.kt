package tw.edu.ntub.imd.birc.rxandmvvm.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import tw.edu.ntub.imd.birc.rxandmvvm.R
import tw.edu.ntub.imd.birc.rxandmvvm.extension.attachToRecyclerView
import tw.edu.ntub.imd.birc.rxandmvvm.extension.mapSourceState
import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.ObservableAdapter
import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.item.UserItem
import tw.edu.ntub.imd.birc.rxandmvvm.viewmodel.MainViewModel

class ApiTestActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_api_test)

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_main_user)
        val adapter = ObservableAdapter(
            viewModel.searchAll()
                .mapSourceState { it.data.map { user -> UserItem(user) } }
        )
        adapter.attachToRecyclerView(recyclerView)

        //        Observable.fromArray(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
//            .subscribeOn(Schedulers.io())
//            .filter { it % 2 == 0 }//2,4,6,8,10
//            .observeOn(Schedulers.computation())
//            .map { it * 2 }//4,8,12,16,20
//            .flatMap { Observable.range(it, 4) }//4,5,6,7,8,9,10
//            .subscribeBy(onNext = {
//                Log.d("TAG", it)
//            })
    }
}