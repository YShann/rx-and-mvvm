package tw.edu.ntub.imd.birc.rxandmvvm.source

import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Query
import tw.edu.ntub.imd.birc.rxandmvvm.data.DietRecord
import tw.edu.ntub.imd.birc.rxandmvvm.data.Food
import tw.edu.ntub.imd.birc.rxandmvvm.data.ResponseBody

interface FoodSource {
    fun searchAll(): Observable<SourceState<ResponseBody<Food>>>

    fun getFood(
        @Query("name") name: String
    ): Observable<SourceState<ResponseBody<Food>>>

}