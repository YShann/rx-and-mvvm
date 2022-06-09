package tw.edu.ntub.imd.birc.rxandmvvm.source.api

import io.reactivex.rxjava3.core.Observable
import kotlinx.serialization.json.JsonObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import tw.edu.ntub.imd.birc.rxandmvvm.data.DietRecord
import tw.edu.ntub.imd.birc.rxandmvvm.data.ResponseBody

interface DietRecordAPI {

    @GET("dietRecord")
    fun searchAll(): Observable<Response<ResponseBody<DietRecord>>>

    @POST("dietRecord")
    fun createDietRecord(@Body body: JsonObject): Call<DietRecord>
}