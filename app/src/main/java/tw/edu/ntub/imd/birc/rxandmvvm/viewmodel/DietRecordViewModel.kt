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
import tw.edu.ntub.imd.birc.rxandmvvm.model.DietRecordModel
import tw.edu.ntub.imd.birc.rxandmvvm.source.SourceState

class DietRecordViewModel(private val model: DietRecordModel) : ViewModel() {
    fun searchAll(): Observable<SourceState<ResponseBody<DietRecord>>> {
        return model.searchAll()
    }

    fun searchByMealTime(
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): Observable<SourceState<ResponseBody<DietRecord>>> {
        return model.searchByMealTime(startDate,endDate)
    }

    //
//    fun createDietRecord(@Body body: JsonObject): Call<DietRecord> {
//        return model.createDietRecord(body)
//    }
    fun createDietRecord(
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part imageFile: MultipartBody.Part
    ): Call<ResponseBody<DietRecord>> {
        return model.createDietRecord(params, imageFile)
    }
}
