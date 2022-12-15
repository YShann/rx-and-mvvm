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
import tw.edu.ntub.imd.birc.rxandmvvm.data.PoopRecord
import tw.edu.ntub.imd.birc.rxandmvvm.data.ResponseBody
import tw.edu.ntub.imd.birc.rxandmvvm.data.WaterRecord
import tw.edu.ntub.imd.birc.rxandmvvm.model.DietRecordModel
import tw.edu.ntub.imd.birc.rxandmvvm.model.PoopRecordModel
import tw.edu.ntub.imd.birc.rxandmvvm.model.WaterRecordModel
import tw.edu.ntub.imd.birc.rxandmvvm.source.SourceState

class PoopRecordViewModel(private val model: PoopRecordModel) : ViewModel() {
    fun searchAll(): Observable<SourceState<ResponseBody<PoopRecord>>> {
        return model.searchAll()
    }


    fun searchByPoopTime(
        @Query("poopTime") poopTime: String
    ): Observable<SourceState<ResponseBody<PoopRecord>>> {
        return model.searchByPoopTime(poopTime)
    }


    fun searchByPoopTimeRange(
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): Observable<SourceState<ResponseBody<PoopRecord>>> {
        return model.searchByPoopTimeRange(startDate,endDate)
    }


    fun createPoopRecord(@Body body: RequestBody): Call<PoopRecord> {
        return model.createPoopRecord(body)
    }

    fun editPoopRecord(@Body body: RequestBody): Call<PoopRecord> {
        return model.editPoopRecord(body)
    }
    fun deletePoopRecord(@Query("id") id: String): Call<PoopRecord> {
        return model.deletePoopRecord(id)
    }
}
