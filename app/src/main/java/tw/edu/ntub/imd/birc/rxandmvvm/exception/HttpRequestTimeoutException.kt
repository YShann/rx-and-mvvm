package tw.edu.ntub.imd.birc.rxandmvvm.exception

import java.net.SocketTimeoutException

class HttpRequestTimeoutException(cause: SocketTimeoutException) :
    ProjectException(MESSAGE, cause) {
    companion object {
        const val MESSAGE = "網路連線異常，請檢查您的網路連線是否正確"
    }
}