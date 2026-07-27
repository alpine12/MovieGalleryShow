package com.alpine12.catalogMovie.data.model.movie

data class ResponseMovie(
    val page: Int,
    val results: List<Movie>
) {
}