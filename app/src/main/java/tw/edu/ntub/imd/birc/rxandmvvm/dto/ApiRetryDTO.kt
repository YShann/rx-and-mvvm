package tw.edu.ntub.imd.birc.rxandmvvm.dto

data class ApiRetryDTO(
    val times: Int,
    val t: Throwable
)