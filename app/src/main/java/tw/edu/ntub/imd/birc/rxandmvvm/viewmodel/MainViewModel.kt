package tw.edu.ntub.imd.birc.rxandmvvm.viewmodel

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Observable
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Query
import tw.edu.ntub.imd.birc.rxandmvvm.data.PoopRecord
import tw.edu.ntub.imd.birc.rxandmvvm.data.ResponseBody
import tw.edu.ntub.imd.birc.rxandmvvm.data.User
import tw.edu.ntub.imd.birc.rxandmvvm.model.UserModel
import tw.edu.ntub.imd.birc.rxandmvvm.source.SourceState

class MainViewModel(private val model:UserModel):ViewModel() {
    fun searchAll(): Observable<SourceState<ResponseBody<User>>> {
        return model.searchAll()
    }

    fun login(
        @Query("account") account: String,
        @Query("password") password: String
    ): Observable<SourceState<ResponseBody<User>>> {
        return model.login(account,password)
    }

    fun register(@Body body: RequestBody): Call<User> {
        return model.register(body)
    }

    fun getUser(
        @Query("account") account: String
    ): Observable<SourceState<ResponseBody<User>>> {
        return model.getUser(account)
    }

    fun editUser(@Body body: RequestBody): Call<User> {
        return model.editUser(body)
    }
}