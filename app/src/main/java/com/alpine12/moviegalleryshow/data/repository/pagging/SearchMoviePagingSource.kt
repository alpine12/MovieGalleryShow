package com.alpine12.moviegalleryshow.data.repository.pagging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.alpine12.moviegalleryshow.data.model.movie.Movie
import com.alpine12.moviegalleryshow.data.network.ApiService
import com.alpine12.moviegalleryshow.utils.Constant
import com.bumptech.glide.load.HttpException
import kotlinx.coroutines.delay
import timber.log.Timber
import java.io.IOException


class SearchMoviePagingSource(
    private val movieQuery: String,
    val apiService: ApiService
) : PagingSource<Int, Movie>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val indexPage = params.key ?: Constant.STARTING_PAGE_INDEX
        return try {
            delay(2500L)
            Timber.d("Paging success")
            val response = apiService.getSearchMovie(movieQuery, indexPage)
            val movie = response.body()?.results
            val nextKey =
                if (movie!!.isEmpty()) {
                    null
                } else {
                    indexPage + 1
                }
            Timber.d("Paging success 2")
            LoadResult.Page(
                data = movie,
                prevKey = if (indexPage == Constant.STARTING_PAGE_INDEX) null else indexPage - 1,
                nextKey = nextKey
            )
        } catch (e: IOException) {
            Timber.d("Paging failed ${e.message.toString()}")
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            Timber.d("Paging failed ${e.message.toString()}")
            return LoadResult.Error(e)
        } catch (e: NullPointerException) {
            Timber.d("Paging failed ${e.message.toString()}")
            return LoadResult.Error(e)
        } catch (e: Throwable) {
            Timber.d("Paging failed ${e.message.toString()}")
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int = 1
}