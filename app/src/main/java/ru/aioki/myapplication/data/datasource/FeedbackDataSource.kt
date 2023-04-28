package ru.aioki.myapplication.data.datasource

import com.aioki.api.client.api.FeedbackApi
import ru.aioki.myapplication.base.BaseDataSource
import javax.inject.Inject

class FeedbackDataSource @Inject constructor(
    private val feedbackApi: FeedbackApi,
): BaseDataSource() {
}