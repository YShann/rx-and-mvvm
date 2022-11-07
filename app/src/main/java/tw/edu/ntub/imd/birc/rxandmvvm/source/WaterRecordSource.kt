package tw.edu.ntub.imd.birc.rxandmvvm.source

import io.reactivex.rxjava3.core.Observable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.*
import tw.edu.ntub.imd.birc.rxandmvvm.data.DietRecord
import tw.edu.ntub.imd.birc.rxandmvvm.data.ResponseBody
import tw.edu.ntub.imd.birc.rxandmvvm.data.User
import tw.edu.ntub.imd.birc.rxandmvvm.data.WaterRecord

interface WaterRecordSource {
    fun searchAll(): Observable<SourceState<ResponseBody<WaterRecord>>>

//    fun getDetail(@Query("id") id: Int): Observable<SourceState<ResponseBody<DietRecord>>>

    fun searchByWaterTime(
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): Observable<SourceState<ResponseBody<WaterRecord>>>

    fun createWaterRecord(@Body body: JsonObject): Call<WaterRecord>
//    fun createWaterRecord(
//        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
//    ): Call<ResponseBody<WaterRecord>>
}
