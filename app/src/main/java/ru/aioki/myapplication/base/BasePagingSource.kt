package ru.aioki.myapplication.base

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.*
import com.aioki.api.client.models.BasePagingResponse
import ru.aioki.myapplication.data.exceptions.PagingException
import ru.aioki.myapplication.utils.Resource
import ru.aioki.myapplication.utils.Resource.Status.*

abstract class BasePagingSource<Value : Any, PagingResponse : BasePagingResponse> :
    PagingSource<Int, Value>() {

    override fun getRefreshKey(state: PagingState<Int, Value>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val anchorPage = state.closestPageToPosition(anchorPosition) ?: return null
        return anchorPage.prevKey?.plus(1) ?: anchorPage.nextKey?.minus(1)
    }

    abstract suspend fun onLoad(pageNumber: Int, pageSize: Int): Resource<PagingResponse>

    abstract fun convert(response: PagingResponse): List<Value>

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Value> {
        val pageNumber = params.key ?: 1
        val pageSize = params.loadSize.coerceAtMost(MAX_PAGE_SIZE)

        val res = onLoad(pageNumber, pageSize)


        val data = when (res.status) {
            SUCCESS -> {
                val meta =
                    res.data?.meta ?: throw IllegalStateException("Paging metadata not found")

                val nextPageNumber =
                    if (meta.currentPage >= meta.totalPages) null else pageNumber + 1
                val prevPageNumber = if (pageNumber > 1) pageNumber - 1 else null
                LoadResult.Page(convert(res.data), prevPageNumber, nextPageNumber)

            }
            LOADING -> {
                throw IllegalStateException("Status LOADING in Resource")
            }
            ERROR, NETWORK_ERROR -> {
                LoadResult.Error(PagingException(res))
            }
        }

        Log.d("Source", "Load result: $data")
        return data
    }

    open fun getPageConfig(): PagingConfig {
        return PagingConfig(
            pageSize = 5,
            enablePlaceholders = true
        )
    }

    open fun getPagerLiveData(): LiveData<PagingData<Value>> {
        return Pager(
            config = getPageConfig(),
            pagingSourceFactory = {
                this
            }
        ).liveData
    }

    companion object {
        const val MAX_PAGE_SIZE = 5
    }

}