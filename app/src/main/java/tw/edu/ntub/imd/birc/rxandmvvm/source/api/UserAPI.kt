package tw.edu.ntub.imd.birc.rxandmvvm.source.api

import io.reactivex.rxjava3.core.Observable
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*
import tw.edu.ntub.imd.birc.rxandmvvm.data.PoopRecord
import tw.edu.ntub.imd.birc.rxandmvvm.data.ResponseBody
import tw.edu.ntub.imd.birc.rxandmvvm.data.User

interface UserAPI {

    @GET("user")
    fun searchAll(): Observable<Response<ResponseBody<User>>>

    @GET("user/login")
    fun login(
        @Query("account") account: String,
        @Query("password") password: String
    ): Observable<Response<ResponseBody<User>>>

    @GET("user")
    fun getUser(
        @Query("account") account: String
    ): Observable<Response<ResponseBody<User>>>

    @POST("user/register")
    fun register(@Body body: RequestBody): Call<User>


    @PATCH("user")
    fun editUser(@Body body: RequestBody): Call<User>

}