package com.alpine12.moviegalleryshow.data.repository

import com.alpine12.moviegalleryshow.data.model.ResultData
import com.alpine12.moviegalleryshow.data.model.movie.ResponseMovie
import com.alpine12.moviegalleryshow.data.network.ApiService
import com.alpine12.moviegalleryshow.data.repository.remote.MovieRemoteDataSource
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteRepository @Inject constructor(
    private val movieRemoteDataSource: MovieRemoteDataSource,
    val apiService: ApiService
) {

    fun getPopularMovie(): Flow<ResultData<ResponseMovie>?> {

        return flow {
            Timber.i("logistik  popular I'm working in thread ${Thread.currentThread().name}")
            emit(ResultData.loading())
            val result = movieRemoteDataSource.fetchPopularMovies()
            emit(result)
        }.flowOn(IO)
    }

    fun getTopRatedMovie(): Flow<ResultData<ResponseMovie>?> {
        Timber.i("logistik  rated I'm working in thread ${Thread.currentThread().name}")
        return flow {
            val movie = movieRemoteDataSource.fetchTopRatedMovies()
            emit(movie)
        }.flowOn(IO)
    }

    fun getNowUpComingMovie(): Flow<ResultData<ResponseMovie>?> {
        Timber.i("logistik playing I'm working in thread ${Thread.currentThread().name}")
        return flow {
            val movie = movieRemoteDataSource.fetchUpComingMovies()
            emit(movie)
        }.flowOn(IO)
    }
}