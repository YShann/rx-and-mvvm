package tw.edu.ntub.imd.birc.rxandmvvm.exception

class EmptyResponseBodyException : ProjectException(MESSAGE) {
    companion object {
        const val MESSAGE = "網路請求回應沒有回傳資料"
    }
}