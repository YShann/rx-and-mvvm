package tw.edu.ntub.imd.birc.rxandmvvm.source

import io.reactivex.rxjava3.core.Observable
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Query
import tw.edu.ntub.imd.birc.rxandmvvm.data.PoopRecord
import tw.edu.ntub.imd.birc.rxandmvvm.data.ResponseBody
import tw.edu.ntub.imd.birc.rxandmvvm.data.User

interface UserSource {
    fun searchAll(): Observable<SourceState<ResponseBody<User>>>

    fun login(
        @Query("account") account: String,
        @Query("password") password: String
    ): Observable<SourceState<ResponseBody<User>>>

    fun getUser(
        @Query("account") account: String
    ): Observable<SourceState<ResponseBody<User>>>

    fun register(@Body body: RequestBody): Call<User>

    fun editUser(@Body body: RequestBody): Call<User>

}