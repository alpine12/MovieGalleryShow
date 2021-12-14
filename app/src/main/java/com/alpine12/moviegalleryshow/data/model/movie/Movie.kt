package com.alpine12.moviegalleryshow.data.model.movie

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movie(
    val id: Int,
    val title: String,
    val vote_average: Double,
    val backdrop_path: String? = "",
    val genre_ids: List<Int>?,
    val release_date: String?,
    val popularity: String
) : Parcelable
