package ru.aioki.myapplication.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.aioki.myapplication.data.datasource.EventDataSource
import ru.aioki.myapplication.data.datasource.FeedbackDataSource
import ru.aioki.myapplication.data.datasource.LoginDataSource
import ru.aioki.myapplication.data.repository.EventRepository
import ru.aioki.myapplication.data.repository.FeedbackRepository
import ru.aioki.myapplication.data.repository.LoginRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideLoginRepository(loginDataSource: LoginDataSource) = LoginRepository(loginDataSource)

//    @Provides
//    @Singleton
//    fun provideProfileRepository(profileDataSource: ProfileDataSource) =
//        ProfileRepository(profileDataSource)

    @Provides
    @Singleton
    fun provideFeedbackRepository(feedbackDataSource: FeedbackDataSource) =
        FeedbackRepository(feedbackDataSource)

    @Provides
    @Singleton
    fun provideEventRepository(eventDataSource: EventDataSource) =
        EventRepository(eventDataSource)
}