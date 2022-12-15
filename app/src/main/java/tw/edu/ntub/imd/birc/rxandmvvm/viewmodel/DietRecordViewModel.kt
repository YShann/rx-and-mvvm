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
import tw.edu.ntub.imd.birc.rxandmvvm.model.DietRecordModel
import tw.edu.ntub.imd.birc.rxandmvvm.source.SourceState

class DietRecordViewModel(private val model: DietRecordModel) : ViewModel() {
    fun searchAll(): Observable<SourceState<ResponseBody<DietRecord>>> {
        return model.searchAll()
    }
//    fun getDetail(@Query("id") id: Int): Observable<SourceState<ResponseBody<DietRecord>>> {
//        return model.searchAll()
//    }

    fun searchByMealDate(
        @Query("mealDate") mealDate: String,
        @Query("account") account: String
    ): Observable<SourceState<ResponseBody<DietRecord>>> {
        return model.searchByMealDate(mealDate,account)
    }

    fun searchByMealDateRange(
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String,
        @Query("account") account: String
    ): Observable<SourceState<ResponseBody<DietRecord>>> {
        return model.searchByMealDateRange(startDate,endDate,account)
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

    fun editDietRecord(@Body body: RequestBody): Call<DietRecord> {
        return model.editDietRecord(body)
    }
    fun deleteDietRecord(@Query("id") id: String): Call<DietRecord> {
        return model.deleteDietRecord(id)
    }
}
