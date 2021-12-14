package com.alpine12.moviegalleryshow.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(movie: MovieEntity): Long

    @Query("select * from movie_table order by dateSaved ASC")
    fun getMovieSaved(): Flow<List<MovieEntity>>

    @Query("select * from movie_table where title like '%' || :query || '%' order by dateSaved ASC")
    fun getSearchMovie(query: String): Flow<List<MovieEntity>>

    @Query("select COUNT(id) from movie_table where id = :searchId ")
    fun getIdMovie(searchId: Int): Flow<Int>

    @Delete
    suspend fun deleteMovie(movie: MovieEntity):Int
}