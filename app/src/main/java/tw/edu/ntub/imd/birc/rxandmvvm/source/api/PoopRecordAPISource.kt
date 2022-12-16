package tw.edu.ntub.imd.birc.rxandmvvm.source.api

import io.reactivex.rxjava3.core.Observable
import kotlinx.serialization.json.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Query
import tw.edu.ntub.imd.birc.rxandmvvm.data.DietRecord
import tw.edu.ntub.imd.birc.rxandmvvm.data.PoopRecord
import tw.edu.ntub.imd.birc.rxandmvvm.data.ResponseBody
import tw.edu.ntub.imd.birc.rxandmvvm.data.WaterRecord
import tw.edu.ntub.imd.birc.rxandmvvm.extension.toApiSourceState
import tw.edu.ntub.imd.birc.rxandmvvm.source.DietRecordSource
import tw.edu.ntub.imd.birc.rxandmvvm.source.PoopRecordSource
import tw.edu.ntub.imd.birc.rxandmvvm.source.SourceState
import tw.edu.ntub.imd.birc.rxandmvvm.source.WaterRecordSource


class PoopRecordAPISource(private val poopRecordAPI: PoopRecordAPI) : PoopRecordSource {
    override fun searchAll(): Observable<SourceState<ResponseBody<PoopRecord>>> {
        return poopRecordAPI.searchAll().toApiSourceState()
    }

//    override fun getDetail(@Query("id") id: Int): Observable<SourceState<ResponseBody<DietRecord>>> {
//        return dietRecordAPI.searchAll().toApiSourceState()
//    }

    override fun searchByPoopTime(
        @Query("poopTime") poopTime: String,
        @Query("account") account: String
    ): Observable<SourceState<ResponseBody<PoopRecord>>> {
        return poopRecordAPI.searchByPoopTime(poopTime,account).toApiSourceState()
    }

    override fun searchByPoopTimeRange(
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String,
        @Query("account") account: String
    ): Observable<SourceState<ResponseBody<PoopRecord>>> {
        return poopRecordAPI.searchByPoopTimeRange(startDate, endDate,account).toApiSourceState()
    }

    override fun createPoopRecord(@Body body: RequestBody): Call<PoopRecord> {
        return poopRecordAPI.createPoopRecord(body)
    }

    override fun editPoopRecord(@Body body: RequestBody): Call<PoopRecord> {
        return poopRecordAPI.editPoopRecord(body)
    }

    override fun deletePoopRecord(@Query("id") id: String): Call<PoopRecord> {
        return poopRecordAPI.deletePoopRecord(id)
    }
}


