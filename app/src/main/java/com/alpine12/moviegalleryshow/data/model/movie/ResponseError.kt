package com.alpine12.moviegalleryshow.data.model.movie

data class ResponseError(
    val status_code : Int = 0,
    val status_message : String? = null
) {
}