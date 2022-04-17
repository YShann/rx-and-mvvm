package tw.edu.ntub.imd.birc.rxandmvvm.extension

import android.util.Log
import retrofit2.Response
import tw.edu.ntub.imd.birc.rxandmvvm.data.ResponseBody
import tw.edu.ntub.imd.birc.rxandmvvm.exception.EmptyResponseBodyException
import tw.edu.ntub.imd.birc.rxandmvvm.exception.HttpException
import tw.edu.ntub.imd.birc.rxandmvvm.exception.HttpResponseErrorException
import tw.edu.ntub.imd.birc.rxandmvvm.source.SourceState

fun <T> Response<T>.toSourceState(): SourceState<T> {
    return if (isSuccessful) {
        val body = body()
        if (body != null) {
            SourceState.success(body)
        } else {
            SourceState.error(
                EmptyResponseBodyException.MESSAGE,
                EmptyResponseBodyException()
            )
        }
    } else {
        SourceState.error(
            message(),
            HttpException(code(), errorBody())
        )
    }
}
