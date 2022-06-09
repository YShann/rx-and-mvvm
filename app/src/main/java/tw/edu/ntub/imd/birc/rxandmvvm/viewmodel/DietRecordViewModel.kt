package tw.edu.ntub.imd.birc.rxandmvvm.viewmodel

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Observable
import kotlinx.serialization.json.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import tw.edu.ntub.imd.birc.rxandmvvm.data.DietRecord
import tw.edu.ntub.imd.birc.rxandmvvm.data.ResponseBody
import tw.edu.ntub.imd.birc.rxandmvvm.model.DietRecordModel
import tw.edu.ntub.imd.birc.rxandmvvm.source.SourceState

class DietRecordViewModel(private val model:DietRecordModel):ViewModel() {
    fun searchAll(): Observable<SourceState<ResponseBody<DietRecord>>> {
        return model.searchAll()
    }

    fun createDietRecord(@Body body: JsonObject): Call<DietRecord>{
        return model.createDietRecord(body)
    }
}