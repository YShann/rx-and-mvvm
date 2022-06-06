package tw.edu.ntub.imd.birc.rxandmvvm.source.api

import android.util.Log
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableSource
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Body
import tw.edu.ntub.imd.birc.rxandmvvm.data.DietRecord
import tw.edu.ntub.imd.birc.rxandmvvm.data.ResponseBody
import tw.edu.ntub.imd.birc.rxandmvvm.data.User
import tw.edu.ntub.imd.birc.rxandmvvm.extension.toApiSourceState
import tw.edu.ntub.imd.birc.rxandmvvm.extension.toSourceState
import tw.edu.ntub.imd.birc.rxandmvvm.source.DietRecordSource
import tw.edu.ntub.imd.birc.rxandmvvm.source.SourceState
import tw.edu.ntub.imd.birc.rxandmvvm.source.UserSource


class DietRecordAPISource(private val dietRecordAPI: DietRecordAPI) : DietRecordSource {
    override fun searchAll(): Observable<SourceState<ResponseBody<DietRecord>>> {
        return dietRecordAPI.searchAll().toApiSourceState()
    }

    override fun createDietRecord(@Body body: JsonObject): Call<DietRecord> {
        return dietRecordAPI.createDietRecord(body)
    }
}


