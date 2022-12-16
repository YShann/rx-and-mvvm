package tw.edu.ntub.imd.birc.rxandmvvm.viewmodel

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Observable
import kotlinx.serialization.json.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Query
import tw.edu.ntub.imd.birc.rxandmvvm.data.DietRecord
import tw.edu.ntub.imd.birc.rxandmvvm.data.ResponseBody
import tw.edu.ntub.imd.birc.rxandmvvm.data.WaterRecord
import tw.edu.ntub.imd.birc.rxandmvvm.model.DietRecordModel
import tw.edu.ntub.imd.birc.rxandmvvm.model.WaterRecordModel
import tw.edu.ntub.imd.birc.rxandmvvm.source.SourceState

class WaterRecordViewModel(private val model: WaterRecordModel) : ViewModel() {
    fun searchAll(): Observable<SourceState<ResponseBody<WaterRecord>>> {
        return model.searchAll()
    }
//    fun getDetail(@Query("id") id: Int): Observable<SourceState<ResponseBody<DietRecord>>> {
//        return model.searchAll()
//    }

    fun searchByWaterTime(
        @Query("waterTime") waterTime: String,
        @Query("account") account: String
    ): Observable<SourceState<ResponseBody<WaterRecord>>> {
        return model.searchByWaterTime(waterTime,account)
    }

    fun searchByWaterTimeRange(
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String,
        @Query("account") account: String
    ): Observable<SourceState<ResponseBody<WaterRecord>>> {
        return model.searchByWaterTimeRange(startDate, endDate,account)
    }


    fun createWaterRecord(@Body body: RequestBody): Call<WaterRecord> {
        return model.createWaterRecord(body)
    }

    fun editWaterRecord(@Body body: RequestBody): Call<WaterRecord> {
        return model.editWaterRecord(body)
    }
    fun deleteWaterRecord(@Query("id") id: String): Call<WaterRecord> {
        return model.deleteWaterRecord(id)
    }
}
