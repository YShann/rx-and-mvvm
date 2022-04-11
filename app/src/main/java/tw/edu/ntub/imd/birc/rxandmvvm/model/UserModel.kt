package tw.edu.ntub.imd.birc.rxandmvvm.model

import io.reactivex.rxjava3.core.Observable
import tw.edu.ntub.imd.birc.rxandmvvm.data.User
import tw.edu.ntub.imd.birc.rxandmvvm.source.SourceState
import tw.edu.ntub.imd.birc.rxandmvvm.source.UserSource

class UserModel(private val apiSource: UserSource) {
    fun searchAll():Observable<SourceState<List<User>>>{
        return apiSource.searchAll()
    }
}