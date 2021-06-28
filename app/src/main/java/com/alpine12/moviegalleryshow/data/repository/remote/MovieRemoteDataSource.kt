package com.alpine12.moviegalleryshow.data.repository.remote

import com.alpine12.moviegalleryshow.data.model.ResultData
import com.alpine12.moviegalleryshow.data.model.movie.ResponseMovie
import com.alpine12.moviegalleryshow.data.network.ApiService
import com.alpine12.moviegalleryshow.utils.ErrorUtils
import retrofit2.Response
import retrofit2.Retrofit
import timber.log.Timber
import javax.inject.Inject

class MovieRemoteDataSource @Inject constructor(
    private val apiService: ApiService,
    private val retrofit: Retrofit
) {

    suspend fun fetchPopularMovies(): ResultData<ResponseMovie> {
        return getResponse (
            request = {apiService.getPopularMovie()},
            defaultErrorMessage = "Error fetching Movie list"
        )
    }

    suspend fun fetchTopRatedMovies(): ResultData<ResponseMovie> {
        return getResponse (
            request = {apiService.getTopRatedMovie()},
            defaultErrorMessage = "Error fetching Movie list"
        )
    }

    suspend fun fetchUpComingMovies(): ResultData<ResponseMovie> {
        return getResponse (
            request = {apiService.getNowUpComing()},
            defaultErrorMessage = "Error fetching Movie list"
        )
    }
    private suspend fun <T> getResponse(
        request: suspend () -> Response<T>,
        defaultErrorMessage: String
    ): ResultData<T> {
        return try {
            Timber.i(" I'm working in thread ${Thread.currentThread().name}")
            val result = request.invoke()
            if (result.isSuccessful) {
                return ResultData.success(result.body())
            } else {
                val errorResponse = ErrorUtils.parseErrors(result, retrofit)
                ResultData.error(errorResponse?.status_message ?: defaultErrorMessage, errorResponse)
            }
        } catch (e: Throwable) {
            ResultData.error("Unknown Error", null)
        }
    }

}