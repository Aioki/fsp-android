package com.aioki.api.client.interceptors

import okhttp3.Request
import retrofit2.Invocation
import kotlin.reflect.KClass

internal fun Request.isAnnotationPresent(annotation: KClass<out Annotation>): Boolean{
    return tag(Invocation::class.java)
        ?.method()
        ?.isAnnotationPresent(annotation.java) ?: false
}