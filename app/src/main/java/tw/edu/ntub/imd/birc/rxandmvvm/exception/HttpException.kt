package tw.edu.ntub.imd.birc.rxandmvvm.exception

import okhttp3.ResponseBody

class HttpException(val status: Int, val errorBody: ResponseBody?) :
    ProjectException("網路請求失敗, statue = $status)")