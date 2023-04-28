package ru.aioki.myapplication.data.exceptions

import ru.aioki.myapplication.utils.Resource

class PagingException(data: Resource<Any>) :
    RuntimeException("HttpException: msg=${data.errorBody?.message}, httpCode: ${data.errorBody?.httpCode}")