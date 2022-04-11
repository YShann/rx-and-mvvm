package tw.edu.ntub.imd.birc.rxandmvvm.view.adapter

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import tw.edu.ntub.imd.birc.rxandmvvm.observer.IfError
import tw.edu.ntub.imd.birc.rxandmvvm.observer.SourceStateObserver
import tw.edu.ntub.imd.birc.rxandmvvm.source.SourceState
import tw.edu.ntub.imd.birc.rxandmvvm.view.adapter.item.AdapterItem

class ObservableAdapter(
    source: Observable<SourceState<List<AdapterItem>>>,
    private val mutableAdapter: MutableAdapter = MutableItemAdapter(mutableListOf()),
    ifSourceError: IfError? = null
) : MutableAdapter by mutableAdapter {
    init {
        source.observeOn(AndroidSchedulers.mainThread())
            .subscribe(SourceStateObserver(ifSuccess = {
                mutableAdapter.addItem(it)
            }, ifError = ifSourceError))
    }
}