package com.alpine12.moviegalleryshow.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.alpine12.moviegalleryshow.data.model.movie.Movie
import com.alpine12.moviegalleryshow.data.network.ApiService
import retrofit2.HttpException
import java.io.IOException

private const val MOVIE_STARTING_PAGE_INDEX = 1


class MoviePagingSource(private val apiService: ApiService) :
    PagingSource<Int, Movie>() {
    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        TODO("Not yet implemented")
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        TODO("Not yet implemented")
    }
//    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
//        val pageIndex = params.key ?: MOVIE_STARTING_PAGE_INDEX
//        return try {
//            val response = apiService.getNowPlayingMovie()
//            val movies = response!!.results
//            LoadResult.Page(
//                data = movies,
//                nextKey = if (pageIndex == MOVIE_STARTING_PAGE_INDEX) null else pageIndex - 1,
//                prevKey = if (movies.isEmpty()) null else pageIndex + 1
//            )
//
//        } catch (exception: IOException) {
//            LoadResult.Error(exception)
//        } catch (exception: HttpException) {
//            LoadResult.Error(exception)
//        } catch (e: Exception) {
//            LoadResult.Error(e.fillInStackTrace())
//        }
//    }
}
