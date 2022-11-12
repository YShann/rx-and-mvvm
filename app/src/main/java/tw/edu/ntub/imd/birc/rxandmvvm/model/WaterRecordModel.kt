package tw.edu.ntub.imd.birc.rxandmvvm.model

import io.reactivex.rxjava3.core.Observable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Query
import tw.edu.ntub.imd.birc.rxandmvvm.data.DietRecord
import tw.edu.ntub.imd.birc.rxandmvvm.data.ResponseBody
import tw.edu.ntub.imd.birc.rxandmvvm.data.User
import tw.edu.ntub.imd.birc.rxandmvvm.data.WaterRecord
import tw.edu.ntub.imd.birc.rxandmvvm.source.DietRecordSource
import tw.edu.ntub.imd.birc.rxandmvvm.source.SourceState
import tw.edu.ntub.imd.birc.rxandmvvm.source.UserSource
import tw.edu.ntub.imd.birc.rxandmvvm.source.WaterRecordSource

class WaterRecordModel(private val apiSource: WaterRecordSource) {
    fun searchAll(): Observable<SourceState<ResponseBody<WaterRecord>>> {
        return apiSource.searchAll()
    }

//    fun getDetail(@Query("id") id: Int): Observable<SourceState<ResponseBody<DietRecord>>> {
//        return apiSource.searchAll()
//    }

    fun searchByWaterTime(
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): Observable<SourceState<ResponseBody<WaterRecord>>> {
        return apiSource.searchByWaterTime(startDate, endDate)
    }

    fun createWaterRecord(@Body body: RequestBody): Call<WaterRecord> {
        return apiSource.createWaterRecord(body)
    }
//    fun createDietRecord(
//        @PartMap params : Map<String,@JvmSuppressWildcards RequestBody> ,
//        @Part imageFile: MultipartBody.Part
//    ): Call<ResponseBody<WaterRecord>> {
//        return apiSource.createWaterRecord(params,imageFile)
//    }

}
