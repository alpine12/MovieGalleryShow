package com.alpine12.moviegalleryshow.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.alpine12.moviegalleryshow.data.model.movie.Movie
import com.alpine12.moviegalleryshow.data.network.ApiService
import com.alpine12.moviegalleryshow.utils.Constant
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject


class MoviePagingSource (
    private val movieType: String,
    val apiService: ApiService
) : PagingSource<Int, Movie>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val indexPage = params.key ?: Constant.STARTING_PAGE_INDEX

        return try {
            val response = apiService.getAllMovie(movieType, indexPage)
            val movie = response.body()?.results
            val nextKey =
                if (movie!!.isEmpty()) {
                    null
                } else {
                   indexPage +1
                }
            Timber.d("paging call data remote $indexPage , next page $nextKey ")
//            LoadResult.Page(
//                data = movie,
//                prevKey = if (indexPage == Constant.STARTING_PAGE_INDEX) null else indexPage,
//                nextKey = nextKey
//            )

            LoadResult.Page(
                data = movie,
                prevKey = if (indexPage == Constant.STARTING_PAGE_INDEX) null else indexPage-1,
                nextKey = nextKey
            )


        } catch (e: IOException) {
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            return LoadResult.Error(e)
        }

    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}