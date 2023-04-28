package ru.aioki.myapplication.data.domain

import ru.aioki.myapplication.base.BaseDataSource
import ru.aioki.myapplication.utils.Resource

data class LoadingState(
    val status: Resource.Status,
    val errorBody: BaseDataSource.ErrorBody?,
    val actionMessage: String? = null
) {
    companion object {
        fun loading(
            actionMessage: String? = null,
        ): LoadingState {
            return LoadingState(
                status = Resource.Status.LOADING,
                errorBody = null,
                actionMessage = actionMessage,
            )
        }

        fun success(
            actionMessage: String? = null,
        ): LoadingState {
            return LoadingState(
                status = Resource.Status.SUCCESS,
                errorBody = null,
                actionMessage = actionMessage,
            )
        }

        fun error(
            errorBody: BaseDataSource.ErrorBody,
            actionMessage: String? = null,
        ): LoadingState {
            return LoadingState(
                status = Resource.Status.ERROR,
                errorBody = errorBody,
                actionMessage = actionMessage,
            )
        }

        fun error(
            message: String,
            actionMessage: String? = null,
        ): LoadingState {
            return LoadingState(
                status = Resource.Status.ERROR,
                errorBody = BaseDataSource.ErrorBody(
                    message = message,
                ),
                actionMessage = actionMessage,
            )
        }

        fun networkError(
            errorBody: BaseDataSource.ErrorBody,
            actionMessage: String?,
        ): LoadingState {
            return LoadingState(
                status = Resource.Status.NETWORK_ERROR,
                errorBody = errorBody,
                actionMessage = actionMessage,
            )
        }
    }
}
