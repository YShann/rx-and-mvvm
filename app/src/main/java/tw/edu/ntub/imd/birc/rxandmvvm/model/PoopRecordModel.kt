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
import tw.edu.ntub.imd.birc.rxandmvvm.data.*
import tw.edu.ntub.imd.birc.rxandmvvm.source.*

class PoopRecordModel(private val apiSource: PoopRecordSource) {
    fun searchAll(): Observable<SourceState<ResponseBody<PoopRecord>>> {
        return apiSource.searchAll()
    }

//    fun getDetail(@Query("id") id: Int): Observable<SourceState<ResponseBody<DietRecord>>> {
//        return apiSource.searchAll()
//    }

    fun searchByPoopTime(
        @Query("poopTime") poopTime: String
    ): Observable<SourceState<ResponseBody<PoopRecord>>> {
        return apiSource.searchByPoopTime(poopTime)
    }

    fun searchByPoopTimeRange(
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): Observable<SourceState<ResponseBody<PoopRecord>>> {
        return apiSource.searchByPoopTimeRange(startDate, endDate)
    }

    fun createPoopRecord(@Body body: RequestBody): Call<PoopRecord> {
        return apiSource.createPoopRecord(body)
    }

    fun editPoopRecord(@Body body: RequestBody): Call<PoopRecord> {
        return apiSource.editPoopRecord(body)
    }
    fun deletePoopRecord(@Query("id") id: String): Call<PoopRecord> {
        return apiSource.deletePoopRecord(id)
    }


}
