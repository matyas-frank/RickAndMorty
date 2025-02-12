package cz.frank.rickandmorty.utils

import androidx.annotation.StringRes
import cz.frank.rickandmorty.R


sealed class Result<out T : Any> {

    data class Success<out T : Any>(val data: T) : Result<T>()

    data class Error(val error: ErrorResult) : Result<Nothing>()
}

interface ErrorResult {
    @get:StringRes val localizedMessage: Int
}

sealed class CommonError(@StringRes override val localizedMessage: Int) : ErrorResult {
    data object NoNetworkConnection : CommonError(localizedMessage = R.string.error_no_internet_connection)
    data object Unknown : CommonError(localizedMessage = R.string.unknown_error)
}
