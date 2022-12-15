package tw.edu.ntub.imd.birc.rxandmvvm.source.api

import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Query
import tw.edu.ntub.imd.birc.rxandmvvm.data.DietRecord
import tw.edu.ntub.imd.birc.rxandmvvm.data.Food
import tw.edu.ntub.imd.birc.rxandmvvm.data.ResponseBody
import tw.edu.ntub.imd.birc.rxandmvvm.extension.toApiSourceState
import tw.edu.ntub.imd.birc.rxandmvvm.source.FoodSource
import tw.edu.ntub.imd.birc.rxandmvvm.source.SourceState


class FoodAPISource(private val foodAPI:FoodAPI) : FoodSource {
    override fun searchAll(): Observable<SourceState<ResponseBody<Food>>> {
        return foodAPI.searchAll().toApiSourceState()
    }
    override fun getFood(
        @Query("name") name: String,
    ): Observable<SourceState<ResponseBody<Food>>> {
        return foodAPI.getFood(name).toApiSourceState()
    }
}


