package com.alpine12.moviegalleryshow.data.model.movie

data class ResponseMovie(
    val page: Int,
    val results: List<Movie>
) {
}