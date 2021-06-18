package com.alpine12.moviegalleryshow.data.model.movie

data class DetailMovie(
    val original_title: String,
    val poster_path: String,
    val original_language: String,
    val genres: List<Genre>,
    val overview: String,
    val production_companies: List<Companies>
) {
}