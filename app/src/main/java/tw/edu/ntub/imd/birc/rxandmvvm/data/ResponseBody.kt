package tw.edu.ntub.imd.birc.rxandmvvm.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseBody<T>(
    @SerialName("result")
    val isSuccess: Boolean,
    @SerialName("errorCode")
    val errorCode: String,
    @SerialName("message")
    val message: String,
    @SerialName("data")
    val data: List<T>
)
