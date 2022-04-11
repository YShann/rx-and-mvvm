package tw.edu.ntub.imd.birc.rxandmvvm.exception

class UnknownHttpRequestException(cause: Throwable) : ProjectException(MESSAGE, cause) {
    companion object {
        const val MESSAGE = "未知網路請求錯誤，請聯繫系統廠商"
    }
}