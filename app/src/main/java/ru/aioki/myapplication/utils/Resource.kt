package ru.aioki.myapplication.utils

import ru.aioki.myapplication.base.BaseDataSource.ErrorBody
import ru.aioki.myapplication.data.domain.LoadingState


data class Resource<out T>(
    val status: Status,
    val data: T?,
    val errorBody: ErrorBody?,
    val actionMessage: String? = null
) {

    enum class Status {
        SUCCESS,
        LOADING,
        ERROR,
        NETWORK_ERROR;
    }

    /**
     * Флаг наличия [Status.ERROR] или [Status.NETWORK_ERROR]
     * */
    val hasError: Boolean
        get() = status == Status.ERROR || status == Status.NETWORK_ERROR

    /**
     * Флаг наличия [Status.SUCCESS]
     * */
    val isSuccess: Boolean
        get() = status == Status.SUCCESS

    fun <R> map(block: (T?) -> R?): Resource<R> {
        return Resource(status, block(data), errorBody, actionMessage)
    }

    /**
     * Состояние загрузки без наличия данных
     * */
    fun toLoadingState(): LoadingState {
        return when (status) {
            Status.SUCCESS -> {
                LoadingState.success(
                    actionMessage = actionMessage,
                )
            }

            Status.LOADING -> {
                LoadingState.loading(
                    actionMessage = actionMessage,
                )
            }

            Status.ERROR -> {
                LoadingState.error(
                    errorBody = errorBody!!,
                    actionMessage = actionMessage,
                )
            }

            Status.NETWORK_ERROR -> {
                LoadingState.networkError(
                    errorBody = errorBody!!,
                    actionMessage = actionMessage,
                )
            }
        }
    }

    companion object {
        fun <T> success(
            data: T? = null,
            actionMessage: String? = null
        ): Resource<T> {
            return Resource(Status.SUCCESS, data, null, actionMessage)
        }

        fun <T> error(
            errorBody: ErrorBody,
            data: T? = null,
            actionMessage: String? = null
        ): Resource<T> {
            return Resource(Status.ERROR, data, errorBody, actionMessage)
        }

        fun <T> error(
            message: String,
            data: T? = null,
            actionMessage: String? = null
        ): Resource<T> {
            return error(ErrorBody(message), data, actionMessage)
        }

        fun <T> loading(
            data: T? = null,
            actionMessage: String? = null
        ): Resource<T> {
            return Resource(Status.LOADING, data, null, actionMessage)
        }

        fun <T> networkError(
            errorBody: ErrorBody,
            data: T? = null,
            actionMessage: String? = null
        ): Resource<T> {
            return Resource(Status.NETWORK_ERROR, null, errorBody, actionMessage)
        }

    }
}