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
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.Part
import retrofit2.http.PartMap
import tw.edu.ntub.imd.birc.rxandmvvm.data.DietRecord
import tw.edu.ntub.imd.birc.rxandmvvm.data.ResponseBody
import tw.edu.ntub.imd.birc.rxandmvvm.data.User

interface DietRecordSource {
    fun searchAll(): Observable<SourceState<ResponseBody<DietRecord>>>

    //    fun createDietRecord(@Body body: JsonObject): Call<DietRecord>
    fun createDietRecord(
        @PartMap params : Map<String,@JvmSuppressWildcards RequestBody> ,
        @Part imageFile: MultipartBody.Part
    ): Call<ResponseBody<DietRecord>>
}
