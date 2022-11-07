package tw.edu.ntub.imd.birc.rxandmvvm.data

import kotlinx.serialization.Serializable

@Serializable
data class WaterRecord(
    var id: Int?,
    var waterVolume: Int?,
    var waterTime: String?,
)
