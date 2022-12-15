package tw.edu.ntub.imd.birc.rxandmvvm.model

import io.reactivex.rxjava3.core.Observable
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Query
import tw.edu.ntub.imd.birc.rxandmvvm.data.PoopRecord
import tw.edu.ntub.imd.birc.rxandmvvm.data.ResponseBody
import tw.edu.ntub.imd.birc.rxandmvvm.data.User
import tw.edu.ntub.imd.birc.rxandmvvm.source.SourceState
import tw.edu.ntub.imd.birc.rxandmvvm.source.UserSource

class UserModel(private val apiSource: UserSource) {
    fun searchAll():Observable<SourceState<ResponseBody<User>>>{
        return apiSource.searchAll()
    }

    fun login(
        @Query("account") account: String,
        @Query("password") password: String
    ): Observable<SourceState<ResponseBody<User>>> {
        return apiSource.login(account, password)
    }

    fun register(@Body body: RequestBody): Call<User> {
        return apiSource.register(body)
    }

    fun getUser(
        @Query("account") account: String
    ): Observable<SourceState<ResponseBody<User>>> {
        return apiSource.getUser(account)
    }

    fun editUser(@Body body: RequestBody): Call<User> {
        return apiSource.editUser(body)
    }
}