package tw.edu.ntub.imd.birc.rxandmvvm.exception

class HttpResponseErrorException(val errorCode: String) :
    ProjectException("API回應失敗, 錯誤代碼: $errorCode")