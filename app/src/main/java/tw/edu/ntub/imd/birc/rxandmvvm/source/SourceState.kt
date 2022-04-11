package tw.edu.ntub.imd.birc.rxandmvvm.source

import android.util.Log
import tw.edu.ntub.imd.birc.rxandmvvm.exception.ProjectException

sealed class SourceState<D> {
    open class Success<D>(val data: D) : SourceState<D>()

    class Error<D>(val message: String, val e: ProjectException) : SourceState<D>() {
        fun log() {
            Log.e("SourceState.Error", message, e)
        }
    }

    class Backup<D>(val preErrorState: Error<*>, val backUpState: SourceState<D>) : SourceState<D>()

    companion object {
        fun <D> success(data: D): SourceState<D> = Success(data)

        fun <D> error(message: String, e: ProjectException): SourceState<D> = Error(message, e)

        fun <D> backup(preErrorState: Error<*>, backUpState: SourceState<D>): SourceState<D> =
            Backup(preErrorState, backUpState)
    }

    fun <R> map(mapper: (data: D) -> R): SourceState<R> {
        return when (this) {
            is Success -> Success(mapper(data))
            is Error -> Error(message, e)
            is Backup -> Backup(preErrorState, backUpState.map(mapper))
        }
    }
}