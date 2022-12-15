package tw.edu.ntub.imd.birc.rxandmvvm.data

import kotlinx.serialization.Serializable

@Serializable
data class PoopRecord(
    var id: Int?,
    var poopCount: Int?,
    var poopTime: String?,
    var poopStatus: String?,
)
