package tw.edu.ntub.imd.birc.rxandmvvm.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseBody<T>(
    @SerialName("result")
    val isSuccess: Boolean,
    val errorCode: String,
    val message: String,
    val data: T
)
