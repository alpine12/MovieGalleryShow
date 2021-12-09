package com.alpine12.moviegalleryshow.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "movie_table")
data class MovieEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val vote_average: Double,
    val backdrop_path: String,
    val genre_ids: List<Int>?,
    val release_date: String?,
    val popularity: String,
    val dateSaved : Date = Date()
) {

}