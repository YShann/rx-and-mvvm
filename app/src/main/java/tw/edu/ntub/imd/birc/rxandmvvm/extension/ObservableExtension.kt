package tw.edu.ntub.imd.birc.rxandmvvm.extension

import android.util.Log
import io.reactivex.rxjava3.core.Observable
import retrofit2.Response
import tw.edu.ntub.imd.birc.rxandmvvm.data.ResponseBody
import tw.edu.ntub.imd.birc.rxandmvvm.data.User
import tw.edu.ntub.imd.birc.rxandmvvm.dto.ApiRetryDTO
import tw.edu.ntub.imd.birc.rxandmvvm.exception.HttpRequestTimeoutException
import tw.edu.ntub.imd.birc.rxandmvvm.exception.ProjectException
import tw.edu.ntub.imd.birc.rxandmvvm.exception.UnknownHttpRequestException
import tw.edu.ntub.imd.birc.rxandmvvm.source.SourceState
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit

fun <T> Observable<Response<ResponseBody<T>>>.toApiSourceState(): Observable<SourceState<ResponseBody<T>>> {
    return retryWhen {
        Observable.range(1, 4)
            .zipWith(it) { times, t -> ApiRetryDTO(times, t) }
            .flatMap { apiRetryDTO ->
                when (apiRetryDTO.t) {
                    is SocketTimeoutException -> {
                        if (apiRetryDTO.times < 4) {
                            Log.d("Retrofit", "連線逾時，第${apiRetryDTO.times}次重試")
                            return@flatMap Observable.timer(1, TimeUnit.SECONDS)
                        } else {
                            return@flatMap Observable.error(apiRetryDTO.t)
                        }
                    }
                    else -> return@flatMap Observable.error(apiRetryDTO.t)
                }
            }
    }.map {
        it.toSourceState()
    }.onErrorResumeNext {
        when (it) {
            is SocketTimeoutException -> Observable.just(
                SourceState.error(
                    HttpRequestTimeoutException.MESSAGE,
                    HttpRequestTimeoutException(it)
                )
            )
            is ProjectException -> Observable.just(
                SourceState.error(
                    it.message ?: UnknownHttpRequestException.MESSAGE, it
                )
            )
            else -> Observable.just(
                SourceState.error(
                    UnknownHttpRequestException.MESSAGE,
                    UnknownHttpRequestException(it)
                )
            )
        }
    }
}

fun <T> Observable<T>.wrapSourceState(errorSupplier: (t: Throwable) -> SourceState.Error<T>): Observable<SourceState<T>> {
    return map {
        SourceState.success(it)
    }.onErrorResumeNext {
        Observable.just(errorSupplier(it))
    }
}

fun <T, R> Observable<SourceState<ResponseBody<T>>>.mapSourceState(mapper: (t: ResponseBody<T>) -> List<R>): Observable<SourceState<List<R>>> {
    return map { it.map(mapper) }
}

fun <T> Observable<SourceState<T>>.setBackupSource(
    backupSourceSupplier: () -> Observable<SourceState<T>>,
    doAfterMainSourceSuccess: ((data: T) -> Unit)? = null
): Observable<SourceState<T>> = flatMap {
    when (it) {
        is SourceState.Success -> {
            doAfterMainSourceSuccess?.invoke(it.data)
            return@flatMap Observable.just(it)
        }
        is SourceState.Error -> {
            return@flatMap backupSourceSupplier().map { backUpState ->
                SourceState.backup(it, backUpState)
            }
        }
        is SourceState.Backup -> {
            return@flatMap Observable.just(it)
        }
    }
}
