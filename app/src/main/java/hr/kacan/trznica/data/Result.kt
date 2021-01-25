package hr.kacan.trznica.data

import hr.kacan.trznica.models.Korisnik

/**
 * A generic class that holds a result success w/ data or an error exception.
 */
open class Result<T>
protected constructor() {
    override fun toString(): String {
        if (this is Success<*>) {
            val success = this as Success<*>
            return "Success[data=" + success.getData().toString() + "]"
        } else if (this is Error<*>) {
            val error = this as Error<*>
            return "Error[exception=" + error.getError().toString() + "]"
        }
        return ""
    }

    // Success sub-class
    class Success<T>(private val data: Korisnik?) : Result<T>() {
        fun getData(): Korisnik? {
            return data
        }
    }

    // Error sub-class
    class Error<T>(private val error: Exception) : Result<T>() {
        fun getError(): Exception? {
            return error
        }
    }
}