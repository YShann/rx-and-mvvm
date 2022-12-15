package tw.edu.ntub.imd.birc.rxandmvvm.data

import kotlinx.serialization.Serializable

@Serializable
data class User(
    var name: String?,
    var gender: String?,
    var height: Double?,
    var weight: Double?,
    var bmi: Double?,
    var birthday: String?,
    var account: String?,
    var password: String?,
    var isEmailLogin: String?
)
