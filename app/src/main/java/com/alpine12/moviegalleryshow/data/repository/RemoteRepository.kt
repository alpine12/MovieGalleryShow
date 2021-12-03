package com.alpine12.moviegalleryshow.data.repository

import com.alpine12.moviegalleryshow.data.model.ResultData
import com.alpine12.moviegalleryshow.data.model.detailmovie.DetailMovie
import com.alpine12.moviegalleryshow.data.model.detailmovie.ResponseVideos
import com.alpine12.moviegalleryshow.data.model.movie.ResponseGenres
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

    fun getAllMovie(movieType: String, page: Int): Flow<ResultData<ResponseMovie>?> = flow {
        emit(ResultData.loading())
        val result = movieRemoteDataSource.fetchAllMovie(movieType, page)
        emit(result)
    }.flowOn(IO)

    fun getPopularMovie(): Flow<ResultData<ResponseMovie>?> {
        return flow {
            emit(ResultData.loading())
            val result = movieRemoteDataSource.fetchPopularMovies()
            emit(result)
        }.flowOn(IO)
    }

    fun getTopRatedMovie(): Flow<ResultData<ResponseMovie>?> {
        return flow {
            val movie = movieRemoteDataSource.fetchTopRatedMovies()
            emit(movie)
        }.flowOn(IO)
    }

    fun getNowUpComingMovie(): Flow<ResultData<ResponseMovie>?> {
        return flow {
            val movie = movieRemoteDataSource.fetchUpComingMovies()
            emit(movie)
        }.flowOn(IO)
    }

    fun getDetailMovie(idMovie: Int): Flow<ResultData<DetailMovie>?> =
        flow {
            val detailMovie = movieRemoteDataSource.fetchDetailMovie(idMovie)
            emit(detailMovie)
        }.flowOn(IO)

    fun getVideos(idMovie: Int): Flow<ResultData<ResponseVideos>?> =
        flow {
            val videos = movieRemoteDataSource.fetchVideos(idMovie)
            Timber.d(videos.data.toString())
            emit(videos)
        }.flowOn(IO)

    fun getGenres(): Flow<ResultData<ResponseGenres>?> =
        flow {
            val genres = movieRemoteDataSource.fetchGenres()
            Timber.d(genres.status.toString())
            emit(genres)
        }.flowOn(IO)

    fun getSearchMovie(query: String, page: Int): Flow<ResultData<ResponseMovie>?> =
        flow {
            val movie = movieRemoteDataSource.fetchSearchMovie(query, page)
            Timber.d(movie.data.toString())
            emit(movie)
        }.flowOn(IO)
}