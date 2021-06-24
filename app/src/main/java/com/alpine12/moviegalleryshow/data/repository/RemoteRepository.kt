package com.alpine12.moviegalleryshow.data.repository

import com.alpine12.moviegalleryshow.data.model.Result
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

    fun getPopularMovie(): Flow<Result<ResponseMovie>?> {

        return flow {
            Timber.i("logistik  popular I'm working in thread ${Thread.currentThread().name}")
            emit(Result.loading())
            val result = movieRemoteDataSource.fetchPopularMovies()
            emit(result)
        }.flowOn(IO)
    }

    fun getTopRatedMovie(): Flow<Result<ResponseMovie>?> {
        Timber.i("logistik  rated I'm working in thread ${Thread.currentThread().name}")
        return flow {
            val movie = movieRemoteDataSource.fetchTopRatedMovies()
            emit(movie)
        }.flowOn(IO)
    }

    fun getNowPlayingMovie(): Flow<Result<ResponseMovie>?> {
        Timber.i("logistik playing I'm working in thread ${Thread.currentThread().name}")
        return flow {
            val movie = movieRemoteDataSource.fetchNowPlayingMovies()
            emit(movie)
        }.flowOn(IO)
    }
}