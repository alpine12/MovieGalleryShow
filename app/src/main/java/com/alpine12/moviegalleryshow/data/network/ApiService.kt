package com.alpine12.moviegalleryshow.data.network

import com.alpine12.moviegalleryshow.data.model.detailmovie.DetailMovie
import com.alpine12.moviegalleryshow.data.model.detailmovie.ResponseVideos
import com.alpine12.moviegalleryshow.data.model.movie.ResponseGenres
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

    @GET("movie/{movie_id}/videos")
    suspend fun getVideos(@Path("movie_id") movieId: Int): Response<ResponseVideos>

    @GET("genre/movie/list")
    suspend fun getGenres(): Response<ResponseGenres>

    @GET("search/movie")
    suspend fun getSearchMovie(
        @Query("query") query: String,
        @Query("page") page: Int
    ): Response<ResponseMovie>
}