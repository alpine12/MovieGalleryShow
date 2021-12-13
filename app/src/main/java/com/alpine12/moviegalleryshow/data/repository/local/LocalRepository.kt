package com.alpine12.moviegalleryshow.data.repository.local

import com.alpine12.moviegalleryshow.data.database.MovieDao
import com.alpine12.moviegalleryshow.data.database.MovieEntity
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalRepository @Inject constructor(private val movieDao: MovieDao) {

    suspend fun insertMovie(movie : MovieEntity) : Long{
      return  movieDao.insert(movie)
    }

    fun getMovie(): Flow<List<MovieEntity>> {
       return movieDao.getMovieSaved()
    }

    fun getSearchMovie(query : String): Flow<List<MovieEntity>>{
        return movieDao.getSearchMovie(query)
    }
}