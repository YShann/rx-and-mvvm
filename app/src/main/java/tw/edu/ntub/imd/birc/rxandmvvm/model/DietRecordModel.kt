package tw.edu.ntub.imd.birc.rxandmvvm.model

import io.reactivex.rxjava3.core.Observable
import kotlinx.serialization.json.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import tw.edu.ntub.imd.birc.rxandmvvm.data.DietRecord
import tw.edu.ntub.imd.birc.rxandmvvm.data.ResponseBody
import tw.edu.ntub.imd.birc.rxandmvvm.source.DietRecordSource
import tw.edu.ntub.imd.birc.rxandmvvm.source.SourceState

class DietRecordModel(private val apiSource: DietRecordSource) {
    fun searchAll():Observable<SourceState<ResponseBody<DietRecord>>>{
        return apiSource.searchAll()
    }

    fun createDietRecord(@Body body: JsonObject): Call<DietRecord> {
        return apiSource.createDietRecord(body)
    }
}