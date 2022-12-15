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
import tw.edu.ntub.imd.birc.rxandmvvm.extension.toApiSourceState
import tw.edu.ntub.imd.birc.rxandmvvm.source.DietRecordSource
import tw.edu.ntub.imd.birc.rxandmvvm.source.SourceState


class DietRecordAPISource(private val dietRecordAPI: DietRecordAPI) : DietRecordSource {
    override fun searchAll(): Observable<SourceState<ResponseBody<DietRecord>>> {
        return dietRecordAPI.searchAll().toApiSourceState()
    }

//    override fun getDetail(@Query("id") id: Int): Observable<SourceState<ResponseBody<DietRecord>>> {
//        return dietRecordAPI.searchAll().toApiSourceState()
//    }

    override fun searchByMealDate(
        @Query("mealDate") mealDate: String,
        @Query("account") account: String
    ): Observable<SourceState<ResponseBody<DietRecord>>> {
        return dietRecordAPI.searchByMealDate(mealDate,account).toApiSourceState()
    }

    override fun searchByMealDateRange(
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String,
        @Query("account") account: String
    ): Observable<SourceState<ResponseBody<DietRecord>>> {
        return dietRecordAPI.searchByMealDateRange(startDate, endDate,account).toApiSourceState()
    }

    //    override fun createDietRecord(@Body body: JsonObject): Call<DietRecord> {
//        return dietRecordAPI.createDietRecord(body)
//    }

    override fun createDietRecord(
        @PartMap params : Map<String,@JvmSuppressWildcards RequestBody> ,
        @Part imageFile: MultipartBody.Part
    ): Call<ResponseBody<DietRecord>> {
        return dietRecordAPI.createDietRecord(params,imageFile)
    }

    override fun editDietRecord(@Body body: RequestBody): Call<DietRecord> {
        return dietRecordAPI.editDietRecord(body)
    }

    override fun deleteDietRecord(@Query("id") id: String): Call<DietRecord> {
        return dietRecordAPI.deleteDietRecord(id)
    }

}


