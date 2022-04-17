package tw.edu.ntub.imd.birc.rxandmvvm.source.api

import android.util.Log
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableSource
import tw.edu.ntub.imd.birc.rxandmvvm.data.ResponseBody
import tw.edu.ntub.imd.birc.rxandmvvm.data.User
import tw.edu.ntub.imd.birc.rxandmvvm.extension.toApiSourceState
import tw.edu.ntub.imd.birc.rxandmvvm.source.SourceState
import tw.edu.ntub.imd.birc.rxandmvvm.source.UserSource


class UserAPISource(private val userAPI:UserAPI) : UserSource {
    override fun searchAll(): Observable<SourceState<ResponseBody<User>>> {
        return userAPI.searchAll().toApiSourceState()
    }
}


