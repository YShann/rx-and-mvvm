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

//    override fun getDetail(@Query("id") id: Int): Observable<SourceState<ResponseBody<DietRecord>>> {
//        return dietRecordAPI.searchAll().toApiSourceState()
//    }

    override fun searchByWaterTime(
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): Observable<SourceState<ResponseBody<WaterRecord>>> {
        return waterRecordAPI.searchByWaterTime(startDate, endDate).toApiSourceState()
    }

    override fun createWaterRecord(@Body body: RequestBody): Call<WaterRecord> {
        return waterRecordAPI.createWaterRecord(body)
    }

//    override fun createWaterRecord(
//        @PartMap params : Map<String,@JvmSuppressWildcards RequestBody> ,
//        @Part imageFile: MultipartBody.Part
//    ): Call<ResponseBody<DietRecord>> {
//        return dietRecordAPI.createDietRecord(params,imageFile)
//    }
}


