package tw.edu.ntub.imd.birc.rxandmvvm.viewmodel

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Observable
import tw.edu.ntub.imd.birc.rxandmvvm.data.User
import tw.edu.ntub.imd.birc.rxandmvvm.model.UserModel
import tw.edu.ntub.imd.birc.rxandmvvm.source.SourceState

class MainViewModel(private val model:UserModel):ViewModel() {
    fun searchAll(): Observable<SourceState<List<User>>> {
        return model.searchAll()
    }
}