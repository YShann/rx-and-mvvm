package tw.edu.ntub.imd.birc.rxandmvvm.source.api

import io.reactivex.rxjava3.core.Observable
import kotlinx.serialization.json.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*
import tw.edu.ntub.imd.birc.rxandmvvm.data.DietRecord
import tw.edu.ntub.imd.birc.rxandmvvm.data.PoopRecord
import tw.edu.ntub.imd.birc.rxandmvvm.data.ResponseBody
import tw.edu.ntub.imd.birc.rxandmvvm.data.WaterRecord


interface PoopRecordAPI {

    @GET("poopRecord")
    fun searchAll(): Observable<Response<ResponseBody<PoopRecord>>>

//    @GET("dietRecord")
//    fun getDetail(@Query("id") id: Int): Observable<Response<ResponseBody<DietRecord>>>

    @GET("poopRecord")
    fun searchByPoopTime(
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): Observable<Response<ResponseBody<PoopRecord>>>

    //不需圖片上傳用這個
        @POST("poopRecord")
    fun createPoopRecord(@Body body: RequestBody): Call<PoopRecord>

//    @Multipart
//    @POST("waterRecord")
//    fun createDietRecord(
//        @PartMap params : Map<String, @JvmSuppressWildcards RequestBody> ,
//        @Part imageFile: MultipartBody.Part
//    ): Call<ResponseBody<DietRecord>>

}
