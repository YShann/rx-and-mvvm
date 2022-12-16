package tw.edu.ntub.imd.birc.rxandmvvm.source.api

import io.reactivex.rxjava3.core.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import tw.edu.ntub.imd.birc.rxandmvvm.data.DietRecord
import tw.edu.ntub.imd.birc.rxandmvvm.data.Food
import tw.edu.ntub.imd.birc.rxandmvvm.data.ResponseBody

interface FoodAPI {

    @GET("food")
    fun searchAll(): Observable<Response<ResponseBody<Food>>>

    @GET("food")
    fun getFood(
        @Query("name") name: String,
    ): Observable<Response<ResponseBody<Food>>>

}