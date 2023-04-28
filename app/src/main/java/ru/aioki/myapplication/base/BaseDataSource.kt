package ru.aioki.myapplication.base

import android.util.Log
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonClass
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response
import ru.aioki.myapplication.utils.Resource
import java.io.File
import java.io.IOException

abstract class BaseDataSource {

    @JsonClass(generateAdapter = true)
    data class ErrorBody(
        val message: String,
        val type: String? = null,
        val errors: Map<String, Array<String>>? = null,
        var httpCode: Int? = null
    )

    private val adapter: JsonAdapter<ErrorBody> =
        Moshi.Builder().build().adapter(ErrorBody::class.java)

    @Suppress("BlockingMethodInNonBlockingContext")
    protected suspend fun <T> getResult(call: suspend () -> Response<T>): Resource<T> {
        try {
            val response = call()
            return if (response.isSuccessful) {
                val body = response.body()
                Resource.success(body)
            } else {
//                val errorBody = adapter.fromJson(response.errorBody()!!.byteString().utf8())
                val error = response.errorBody()!!.byteString().utf8()
                Log.e("Retrofit", "Url: ${response.raw().request.url} ")
                Log.e("Retrofit", "Method: ${response.raw().request.method} ")
                Log.e("Retrofit", "=============================")
                Log.e("Retrofit", error)
//                var errorString = ""
//                errorBody?.errors?.forEach { (t, u) ->
//                    errorString += "$t: ${u.reduceOrNull { acc, s -> "$acc, $s" }}\n"
//                }
//                Log.e("Retrofit", "Errors: $errorString")
                Log.e("Retrofit", "httpCode: ${response.code()}")
                Log.e("Retrofit", "=============================")
//                errorBody?.httpCode = response.code()
                Resource.error(error)
            }
        } catch (e: JsonDataException) {
            e.printStackTrace()
            return Resource.error(ErrorBody(e.message ?: e.toString())) // сообщение о ошибке
        } catch (e: IOException) {
            e.printStackTrace()
            return Resource.networkError(ErrorBody(e.message ?: e.toString()))
        }
    }


    protected fun createFileBody(img: File, name: String = "file"): MultipartBody.Part {
        val requestFile =
            img.asRequestBody("multipart/form-data".toMediaType())
        return MultipartBody.Part.createFormData(name, img.name, requestFile)
    }

    protected fun createFileBody(files: List<File>): List<MultipartBody.Part> {
        val list = mutableListOf<MultipartBody.Part>()
        files.forEach { f ->
            list.add(createFileBody(f, "files[]"))
        }
        return list
    }

    companion object {
        fun bearerToken(token: String): String = "Bearer $token"
    }

}