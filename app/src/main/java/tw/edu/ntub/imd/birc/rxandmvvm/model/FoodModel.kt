package tw.edu.ntub.imd.birc.rxandmvvm.model

import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Query
import tw.edu.ntub.imd.birc.rxandmvvm.data.DietRecord
import tw.edu.ntub.imd.birc.rxandmvvm.data.Food
import tw.edu.ntub.imd.birc.rxandmvvm.data.ResponseBody
import tw.edu.ntub.imd.birc.rxandmvvm.source.FoodSource
import tw.edu.ntub.imd.birc.rxandmvvm.source.SourceState

class FoodModel(private val apiSource: FoodSource) {
    fun searchAll():Observable<SourceState<ResponseBody<Food>>>{
        return apiSource.searchAll()
    }

    fun getFood(
        @Query("name") name: String,
    ): Observable<SourceState<ResponseBody<Food>>> {
        return apiSource.getFood(name)
    }
}