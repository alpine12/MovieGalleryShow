package com.alpine12.moviegalleryshow.utils

import com.alpine12.moviegalleryshow.data.database.MovieEntity
import com.alpine12.moviegalleryshow.data.model.movie.Movie
import java.text.SimpleDateFormat
import java.util.*

object Utils {

    fun dateFormat(date: String, input: String, output: String): String {
        var format = SimpleDateFormat(input, Locale.getDefault())
        val newDate: Date? = format.parse(date)
        format = SimpleDateFormat(output, Locale.getDefault())
        return format.format(newDate!!)
    }

    fun mapToMovie(movieEntity: MovieEntity): Movie = Movie(
        movieEntity.id,
        movieEntity.title,
        movieEntity.vote_average,
        movieEntity.backdrop_path,
        movieEntity.genre_ids,
        movieEntity.release_date,
        movieEntity.popularity
    )

}