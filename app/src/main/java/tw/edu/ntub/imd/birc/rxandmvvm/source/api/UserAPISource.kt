package tw.edu.ntub.imd.birc.rxandmvvm.source.api

import android.util.Log
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableSource
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Query
import tw.edu.ntub.imd.birc.rxandmvvm.data.PoopRecord
import tw.edu.ntub.imd.birc.rxandmvvm.data.ResponseBody
import tw.edu.ntub.imd.birc.rxandmvvm.data.User
import tw.edu.ntub.imd.birc.rxandmvvm.extension.toApiSourceState
import tw.edu.ntub.imd.birc.rxandmvvm.source.SourceState
import tw.edu.ntub.imd.birc.rxandmvvm.source.UserSource


class UserAPISource(private val userAPI:UserAPI) : UserSource {
    override fun searchAll(): Observable<SourceState<ResponseBody<User>>> {
        return userAPI.searchAll().toApiSourceState()
    }

    override fun login(
        @Query("account") account: String,
        @Query("password") password: String
    ): Observable<SourceState<ResponseBody<User>>> {
        return userAPI.login(account, password).toApiSourceState()
    }

    override fun register(@Body body: RequestBody): Call<User> {
        return userAPI.register(body)
    }

    override fun getUser(
        @Query("account") account: String
    ): Observable<SourceState<ResponseBody<User>>> {
        return userAPI.getUser(account).toApiSourceState()
    }

    override fun editUser(@Body body: RequestBody): Call<User> {
        return userAPI.editUser(body)
    }
}


