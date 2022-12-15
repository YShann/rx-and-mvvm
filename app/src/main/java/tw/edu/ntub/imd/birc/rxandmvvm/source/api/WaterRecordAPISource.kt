package tw.edu.ntub.imd.birc.rxandmvvm.source.api

import io.reactivex.rxjava3.core.Observable
import kotlinx.serialization.json.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Query
import tw.edu.ntub.imd.birc.rxandmvvm.data.DietRecord
import tw.edu.ntub.imd.birc.rxandmvvm.data.ResponseBody
import tw.edu.ntub.imd.birc.rxandmvvm.data.WaterRecord
import tw.edu.ntub.imd.birc.rxandmvvm.extension.toApiSourceState
import tw.edu.ntub.imd.birc.rxandmvvm.source.DietRecordSource
import tw.edu.ntub.imd.birc.rxandmvvm.source.SourceState
import tw.edu.ntub.imd.birc.rxandmvvm.source.WaterRecordSource


class WaterRecordAPISource(private val waterRecordAPI: WaterRecordAPI) : WaterRecordSource {
    override fun searchAll(): Observable<SourceState<ResponseBody<WaterRecord>>> {
        return waterRecordAPI.searchAll().toApiSourceState()
    }

    override fun searchByWaterTime(
        @Query("waterTime") waterTime: String,
        @Query("account") account: String
    ): Observable<SourceState<ResponseBody<WaterRecord>>> {
        return waterRecordAPI.searchByWaterTime(waterTime,account).toApiSourceState()
    }

    override fun searchByWaterTimeRange(
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String,
        @Query("account") account: String
    ): Observable<SourceState<ResponseBody<WaterRecord>>> {
        return waterRecordAPI.searchByWaterTimeRange(startDate,endDate,account).toApiSourceState()
    }

    override fun createWaterRecord(@Body body: RequestBody): Call<WaterRecord> {
        return waterRecordAPI.createWaterRecord(body)
    }

    override fun editWaterRecord(@Body body: RequestBody): Call<WaterRecord> {
        return waterRecordAPI.editWaterRecord(body)
    }

    override fun deleteWaterRecord(@Query("id") id: String): Call<WaterRecord> {
        return waterRecordAPI.deleteWaterRecord(id)
    }
}


