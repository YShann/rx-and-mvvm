package tw.edu.ntub.imd.birc.rxandmvvm.source.api

import io.reactivex.rxjava3.core.Observable
import retrofit2.Response
import retrofit2.http.GET
import tw.edu.ntub.imd.birc.rxandmvvm.data.ResponseBody
import tw.edu.ntub.imd.birc.rxandmvvm.data.User

interface UserAPI {

    @GET("student")
    fun searchAll(): Observable<Response<ResponseBody<User>>>
//
//    @GET("posts")
//    fun searchAll(): Observable<Response<List<User>>>
}