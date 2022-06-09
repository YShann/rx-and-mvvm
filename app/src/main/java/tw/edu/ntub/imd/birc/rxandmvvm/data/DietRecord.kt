package tw.edu.ntub.imd.birc.rxandmvvm.data

import kotlinx.serialization.Serializable

@Serializable
data class DietRecord(
    var foodName: String?,
    var portionSize: String?,
    var mealTime: String?,
    var note: String?,
    var energy: Int?,
    var fat: Double?,
    var saturatedFat: Double?,
    var carbohydrate: Double?,
    var protein: Double?,
    var grains: String?,
    var vegetables: String?,
    var meatsAndProtein: String?,
    var milkAndDairy: String?,
    var fruits: String?,
    var fats: String?,
    var imageUrl:String?
)
