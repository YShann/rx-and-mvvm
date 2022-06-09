package tw.edu.ntub.imd.birc.rxandmvvm.model

import io.reactivex.rxjava3.core.Observable
import tw.edu.ntub.imd.birc.rxandmvvm.data.Food
import tw.edu.ntub.imd.birc.rxandmvvm.data.ResponseBody
import tw.edu.ntub.imd.birc.rxandmvvm.source.FoodSource
import tw.edu.ntub.imd.birc.rxandmvvm.source.SourceState

class FoodModel(private val apiSource: FoodSource) {
    fun searchAll():Observable<SourceState<ResponseBody<Food>>>{
        return apiSource.searchAll()
    }
}