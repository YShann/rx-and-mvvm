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
import tw.edu.ntub.imd.birc.rxandmvvm.data.*

interface PoopRecordSource {
    fun searchAll(): Observable<SourceState<ResponseBody<PoopRecord>>>

//    fun getDetail(@Query("id") id: Int): Observable<SourceState<ResponseBody<DietRecord>>>

    fun searchByPoopTime(
        @Query("poopTime") poopTime: String,
        @Query("account") account: String
    ): Observable<SourceState<ResponseBody<PoopRecord>>>

    fun searchByPoopTimeRange(
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String,
        @Query("account") account: String
    ): Observable<SourceState<ResponseBody<PoopRecord>>>

    fun createPoopRecord(@Body body: RequestBody): Call<PoopRecord>

    fun editPoopRecord(@Body body: RequestBody): Call<PoopRecord>

    fun deletePoopRecord(@Query("id") id: String): Call<PoopRecord>
}
