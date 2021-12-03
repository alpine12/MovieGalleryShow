package com.alpine12.moviegalleryshow.data.model

sealed class ResponseData{

    object LOADING : ResponseData()
    data class SUCCESS<T>(val data : T) :ResponseData()
    data class ERROR(val msg : String) : ResponseData()
}
