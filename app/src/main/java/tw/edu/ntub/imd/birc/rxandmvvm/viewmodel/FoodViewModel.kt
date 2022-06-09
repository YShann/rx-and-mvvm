package tw.edu.ntub.imd.birc.rxandmvvm.viewmodel

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.Observable
import tw.edu.ntub.imd.birc.rxandmvvm.data.Food
import tw.edu.ntub.imd.birc.rxandmvvm.data.ResponseBody
import tw.edu.ntub.imd.birc.rxandmvvm.model.FoodModel
import tw.edu.ntub.imd.birc.rxandmvvm.source.SourceState

class FoodViewModel(private val model: FoodModel):ViewModel() {
    fun searchAll(): Observable<SourceState<ResponseBody<Food>>> {
        return model.searchAll()
    }
}