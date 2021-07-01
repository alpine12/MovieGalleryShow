package com.alpine12.moviegalleryshow.data.network

import com.alpine12.moviegalleryshow.data.model.movie.DetailMovie
import com.alpine12.moviegalleryshow.data.model.movie.ResponseMovie
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {


    @GET("movie/popular")
    suspend fun getPopularMovie(): Response<ResponseMovie>

    @GET("movie/top_rated")
    suspend fun getTopRatedMovie(): Response<ResponseMovie>

    @GET("movie/upcoming")
    suspend fun getNowUpComing(): Response<ResponseMovie>

    @GET("movie/{movieType}")
    suspend fun getAllMovie(
        @Path("movieType") movieType: String,
        @Query("page") page: Int
    ): Response<ResponseMovie>

    @GET("movie/{movieId}")
    suspend fun getDetailMovie(@Path("movieId") movieId: Int): Response<DetailMovie>
}