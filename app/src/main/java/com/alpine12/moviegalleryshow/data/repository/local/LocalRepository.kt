package com.alpine12.moviegalleryshow.data.repository.local

import com.alpine12.moviegalleryshow.data.database.MovieDao
import com.alpine12.moviegalleryshow.data.database.MovieEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalRepository @Inject constructor(private val movieDao: MovieDao) {

    suspend fun insertMovie(movie: MovieEntity): Long {
        return movieDao.insert(movie)
    }

    suspend fun deleteMovie(movie: MovieEntity):Int{
        return movieDao.deleteMovie(movie)
    }

    fun getMovie(): Flow<List<MovieEntity>> {
        return movieDao.getMovieSaved()
    }

    fun getSearchMovie(query: String): Flow<List<MovieEntity>> {
        return movieDao.getSearchMovie(query)
    }

    fun getSearchId(id: Int): Flow<Int> {
        return movieDao.getIdMovie(id)
    }
}