package tw.edu.ntub.imd.birc.rxandmvvm.data

import kotlinx.serialization.Serializable

@Serializable
data class User(
    //val -> value - const
    //var -> variable - 變數
    //Optional<T> - null safe
    //val id: Int?,加問號代表不確定有值
    var id: Int?,
    var name: String?,
    var username: String?,
    var email: String?,
    var phone: String?,
    var website: String?,
)
