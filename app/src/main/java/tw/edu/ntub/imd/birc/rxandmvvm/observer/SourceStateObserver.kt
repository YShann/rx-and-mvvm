package tw.edu.ntub.imd.birc.rxandmvvm.observer

import android.util.Log
import io.reactivex.rxjava3.observers.ResourceObserver
import tw.edu.ntub.imd.birc.rxandmvvm.source.SourceState
import java.util.*
import kotlin.math.log

typealias IfError = (state: SourceState.Error<*>) -> Unit

class SourceStateObserver<T>(
    private val ifSuccess: (t: T) -> Unit,
    private val ifError: IfError? = null,
) : ResourceObserver<SourceState<T>>() {
    override fun onNext(t: SourceState<T>?) {
        t?.let {
            when (it) {
                is SourceState.Success -> ifSuccess(it.data)
//                is SourceState.Error -> ifError?.invoke(it)
                is SourceState.Error -> {
                    it.log()
                }
                is SourceState.Backup -> {
                    it.preErrorState.log()
                    ifError?.invoke(it.preErrorState)
                    onNext(it.backUpState)
                }
            }
        }
    }

    override fun onError(e: Throwable?) {
        e?.let {
            Log.e("SourceStateObserver", "發生錯誤，此Observer所訂閱的Observable已停止發送事件", it)
        }
    }

    override fun onComplete() {
        Log.d("SourceStateObserver", "發送完成，此Observer所訂閱的Observable已停止發送事件")
    }
}