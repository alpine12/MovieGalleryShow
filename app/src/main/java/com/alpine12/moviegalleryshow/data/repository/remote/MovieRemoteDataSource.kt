package com.alpine12.moviegalleryshow.data.repository.remote

import com.alpine12.moviegalleryshow.data.model.ResultData
import com.alpine12.moviegalleryshow.data.model.detailmovie.DetailMovie
import com.alpine12.moviegalleryshow.data.model.detailmovie.ResponseVideos
import com.alpine12.moviegalleryshow.data.model.movie.ResponseGenres
import com.alpine12.moviegalleryshow.data.model.movie.ResponseMovie
import com.alpine12.moviegalleryshow.data.network.ApiService
import com.alpine12.moviegalleryshow.utils.ErrorUtils
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

class MovieRemoteDataSource @Inject constructor(
    private val apiService: ApiService,
    private val retrofit: Retrofit
) {
    private val defaultError = "Error fetching Movie list"
    suspend fun fetchAllMovie(movie: String, page: Int): ResultData<ResponseMovie> {
        return getResponse(
            request = { apiService.getAllMovie(movie, page) },
            defaultErrorMessage = defaultError
        )
    }

    suspend fun fetchPopularMovies(): ResultData<ResponseMovie> {
        return getResponse(
            request = { apiService.getPopularMovie() },
            defaultErrorMessage = defaultError
        )
    }

    suspend fun fetchTopRatedMovies(): ResultData<ResponseMovie> {
        return getResponse(
            request = { apiService.getTopRatedMovie() },
            defaultErrorMessage = defaultError
        )
    }

    suspend fun fetchUpComingMovies(): ResultData<ResponseMovie> {
        return getResponse(
            request = { apiService.getNowUpComing() },
            defaultErrorMessage = defaultError
        )
    }

    suspend fun fetchDetailMovie(idMovie: Int): ResultData<DetailMovie> {
        return getResponse(
            request = { apiService.getDetailMovie(idMovie) },
            defaultErrorMessage = defaultError
        )
    }

    suspend fun fetchVideos(idMovie: Int): ResultData<ResponseVideos> =
        getResponse(request = { apiService.getVideos(idMovie) }, defaultErrorMessage = defaultError)

    suspend fun fetchGenres(): ResultData<ResponseGenres> =
        getResponse(request = { apiService.getGenres() }, defaultError)

    private suspend fun <T> getResponse(
        request: suspend () -> Response<T>,
        defaultErrorMessage: String
    ): ResultData<T> {
        return try {
            val result = request.invoke()
            if (result.isSuccessful) {
                return ResultData.success(result.body())
            } else {
                val errorResponse = ErrorUtils.parseErrors(result, retrofit)
                ResultData.error(
                    errorResponse?.status_message ?: defaultErrorMessage,
                    errorResponse
                )
            }
        } catch (e: HttpException) {
            ResultData.error("Error : ${e.message()}", null)
        } catch (e: Throwable) {
            ResultData.error(
                "Unknown Error throwable ${e.message} and ${e.stackTrace.toString()}",
                null
            )
        } catch (e: NullPointerException) {
            ResultData.error("Unknown Error null ${e.message} and ${e.stackTrace}", null)
        }
    }

}