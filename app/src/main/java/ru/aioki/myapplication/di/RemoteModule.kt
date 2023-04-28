package ru.aioki.myapplication.di

import android.content.Context
import com.aioki.api.client.ApiClient
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.aioki.myapplication.data.datasource.EventDataSource
import ru.aioki.myapplication.data.datasource.FeedbackDataSource
import ru.aioki.myapplication.data.datasource.LoginDataSource
import ru.aioki.myapplication.utils.API_URL
import java.util.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {

    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(Date().javaClass, Rfc3339DateJsonAdapter())
        .addLast(KotlinJsonAdapterFactory())
        .build()

    // ========== Services ==============================================================


    @Singleton
    @Provides
    fun provideApiClient(@ApplicationContext context: Context, moshi: Moshi) = ApiClient(
        context,
        moshi,
        apiUrl = API_URL,
        versionName = "v1"
    )

    // ========== DataSource ============================================================

    @Singleton
    @Provides
    fun provideLoginDataSource(apiClient: ApiClient) = LoginDataSource(apiClient)

//    @Singleton
//    @Provides
//    fun provideProfileDataSource(apiClient: ApiClient) = ProfileDataSource()

    @Singleton
    @Provides
    fun provideFeedbackDataSource(apiClient: ApiClient) = FeedbackDataSource(apiClient.feedbackApi)

    @Singleton
    @Provides
    fun provideEventDataSource(apiClient: ApiClient) = EventDataSource(apiClient.eventApi)


}