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

interface DietRecordSource {
    fun searchAll(): Observable<SourceState<ResponseBody<DietRecord>>>

//    fun getDetail(@Query("id") id: Int): Observable<SourceState<ResponseBody<DietRecord>>>

    fun searchByMealTime(
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): Observable<SourceState<ResponseBody<DietRecord>>>

    //    fun createDietRecord(@Body body: JsonObject): Call<DietRecord>
    fun createDietRecord(
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part imageFile: MultipartBody.Part
    ): Call<ResponseBody<DietRecord>>
}
