package com.alpine12.moviegalleryshow.data.model
import com.alpine12.moviegalleryshow.data.model.movie.ResponseError


data class ResultData<out T>(
    val status: Status,
    val data: T?,
    val error: ResponseError?,
    val message: String?
) {

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    companion object {
        fun <T> success(data: T?): ResultData<T> {
            return ResultData(Status.SUCCESS, data, null, null)
        }

        fun <T> error(message: String, error: ResponseError?): ResultData<T> {
            return ResultData(Status.ERROR, null, error, message)
        }

        fun <T> loading(data: T? = null): ResultData<T> {
            return ResultData(Status.LOADING, data, null, null)
        }
    }

    override fun toString(): String {
        return "Result (status = $status , data=$data, error =$error, message = $message)"
    }

}