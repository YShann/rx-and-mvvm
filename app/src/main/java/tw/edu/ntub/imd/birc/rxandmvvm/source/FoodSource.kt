package tw.edu.ntub.imd.birc.rxandmvvm.source

import io.reactivex.rxjava3.core.Observable
import tw.edu.ntub.imd.birc.rxandmvvm.data.Food
import tw.edu.ntub.imd.birc.rxandmvvm.data.ResponseBody

interface FoodSource {
    fun searchAll(): Observable<SourceState<ResponseBody<Food>>>
}