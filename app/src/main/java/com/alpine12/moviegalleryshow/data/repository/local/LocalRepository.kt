package com.alpine12.moviegalleryshow.data.repository.local

import com.alpine12.moviegalleryshow.data.database.MovieDao
import com.alpine12.moviegalleryshow.data.database.MovieEntity
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalRepository @Inject constructor(private val movieDao: MovieDao) {

    suspend fun insertMovie(movie : MovieEntity){
        Timber.d("Insert")
        movieDao.insert(movie)
    }

    fun getMovie(): Flow<List<MovieEntity>> {
        Timber.d("get")
       return movieDao.getMovieSaved()
    }
}