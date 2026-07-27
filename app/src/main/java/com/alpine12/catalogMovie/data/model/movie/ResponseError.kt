package com.alpine12.catalogMovie.data.model.movie

data class ResponseError(
    val status_code : Int = 0,
    val status_message : String? = null
) {
}