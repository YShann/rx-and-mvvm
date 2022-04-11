package tw.edu.ntub.imd.birc.rxandmvvm.exception

class UnknownDAOException(e: Throwable) : ProjectException(MESSAGE, e) {
    companion object {
        const val MESSAGE = "SQLite操作發生異常"
    }
}