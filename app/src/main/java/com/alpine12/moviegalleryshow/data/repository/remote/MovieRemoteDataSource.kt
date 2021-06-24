package com.alpine12.moviegalleryshow.data.repository.remote

import com.alpine12.moviegalleryshow.data.model.Result
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

    suspend fun fetchPopularMovies(): Result<ResponseMovie> {
        return getResponse (
            request = {apiService.getPopularMovie()},
            defaultErrorMessage = "Error fetching Movie list"
        )
    }

    suspend fun fetchTopRatedMovies(): Result<ResponseMovie> {
        return getResponse (
            request = {apiService.getTopRatedMovie()},
            defaultErrorMessage = "Error fetching Movie list"
        )
    }

    suspend fun fetchNowPlayingMovies(): Result<ResponseMovie> {
        return getResponse (
            request = {apiService.getNowPlayingMovie()},
            defaultErrorMessage = "Error fetching Movie list"
        )
    }
    private suspend fun <T> getResponse(
        request: suspend () -> Response<T>,
        defaultErrorMessage: String
    ): Result<T> {
        return try {
            Timber.i(" I'm working in thread ${Thread.currentThread().name}")
            val result = request.invoke()
            if (result.isSuccessful) {
                return Result.success(result.body())
            } else {
                val errorResponse = ErrorUtils.parseErrors(result, retrofit)
                Result.error(errorResponse?.status_message ?: defaultErrorMessage, errorResponse)
            }
        } catch (e: Throwable) {
            Result.error("Unknown Error", null)
        }
    }

}