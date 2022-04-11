package tw.edu.ntub.imd.birc.rxandmvvm.source.api

import io.reactivex.rxjava3.core.Observable
import tw.edu.ntub.imd.birc.rxandmvvm.data.User
import tw.edu.ntub.imd.birc.rxandmvvm.extension.toApiSourceState
import tw.edu.ntub.imd.birc.rxandmvvm.source.SourceState
import tw.edu.ntub.imd.birc.rxandmvvm.source.UserSource


class UserAPISource(private val userAPI:UserAPI) : UserSource {
    override fun searchAll(): Observable<SourceState<List<User>>> {
        return userAPI.searchAll().toApiSourceState()
    }
}