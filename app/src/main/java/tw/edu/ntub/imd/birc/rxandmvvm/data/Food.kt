package tw.edu.ntub.imd.birc.rxandmvvm.data

import kotlinx.serialization.Serializable

@Serializable
data class Food(
    //val -> value - const
    //var -> variable - 變數
    //Optional<T> - null safe
    //val id: Int?,加問號代表不確定有值
    var name: String?,
    var label: String?,
    var energy: Int?,
    var carbohydrate: Double?,
    var protein: Double?,
    var fat: Double?,
    var saturatedFat: Double?,
    var grains: String?,
    var vegetables: String?,
    var meatsAndProtein: String?,
    var milkAndDairy: String?,
    var fruits: String?,
    var fats: String?,
)

