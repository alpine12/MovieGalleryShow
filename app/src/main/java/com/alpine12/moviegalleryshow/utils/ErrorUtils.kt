package com.alpine12.moviegalleryshow.utils

import com.alpine12.moviegalleryshow.data.model.movie.ResponseError
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException

object ErrorUtils {

    fun parseErrors(response: Response<*>, retrofit: Retrofit): ResponseError? {
        val converter = retrofit.responseBodyConverter<ResponseError>(ResponseError::class.java, arrayOfNulls(0))
        return try {
            converter.convert(response.errorBody()!!)
        } catch (e: IOException) {
            ResponseError()
        }
    }

}