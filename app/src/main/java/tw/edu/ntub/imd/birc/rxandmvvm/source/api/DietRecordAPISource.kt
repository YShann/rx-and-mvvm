package tw.edu.ntub.imd.birc.rxandmvvm.source.api

import io.reactivex.rxjava3.core.Observable
import kotlinx.serialization.json.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import tw.edu.ntub.imd.birc.rxandmvvm.data.DietRecord
import tw.edu.ntub.imd.birc.rxandmvvm.data.ResponseBody
import tw.edu.ntub.imd.birc.rxandmvvm.extension.toApiSourceState
import tw.edu.ntub.imd.birc.rxandmvvm.source.DietRecordSource
import tw.edu.ntub.imd.birc.rxandmvvm.source.SourceState


class DietRecordAPISource(private val dietRecordAPI: DietRecordAPI) : DietRecordSource {
    override fun searchAll(): Observable<SourceState<ResponseBody<DietRecord>>> {
        return dietRecordAPI.searchAll().toApiSourceState()
    }

    override fun createDietRecord(@Body body: JsonObject): Call<DietRecord> {
        return dietRecordAPI.createDietRecord(body)
    }
}


