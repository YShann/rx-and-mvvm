package tw.edu.ntub.imd.birc.rxandmvvm.source.api

import io.reactivex.rxjava3.core.Observable
import kotlinx.serialization.json.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*
import tw.edu.ntub.imd.birc.rxandmvvm.data.DietRecord
import tw.edu.ntub.imd.birc.rxandmvvm.data.ResponseBody
import tw.edu.ntub.imd.birc.rxandmvvm.data.WaterRecord


interface WaterRecordAPI {

    @GET("waterRecord")
    fun searchAll(): Observable<Response<ResponseBody<WaterRecord>>>

//    @GET("dietRecord")
//    fun getDetail(@Query("id") id: Int): Observable<Response<ResponseBody<DietRecord>>>

    @GET("waterRecord/waterTime")
    fun searchByWaterTime(
        @Query("waterTime") waterTime: String,
        @Query("account") account: String
    ): Observable<Response<ResponseBody<WaterRecord>>>

    @GET("waterRecord")
    fun searchByWaterTimeRange(
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String,
        @Query("account") account: String
    ): Observable<Response<ResponseBody<WaterRecord>>>

    //不需圖片上傳用這個
        @POST("waterRecord")
    fun createWaterRecord(@Body body: RequestBody): Call<WaterRecord>

    @PATCH("waterRecord")
    fun editWaterRecord(@Body body: RequestBody): Call<WaterRecord>

    @DELETE("waterRecord")
    fun deleteWaterRecord(@Query("id") id: String): Call<WaterRecord>
}
