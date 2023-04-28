package ru.aioki.myapplication.ui.feedbackStage

import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import ru.aioki.myapplication.R
import ru.aioki.myapplication.data.domain.feedback.*
import ru.aioki.myapplication.data.repository.FeedbackRepository
import ru.aioki.myapplication.utils.Resource
import ru.aioki.myapplication.utils.Resource.Status
import ru.aioki.myapplication.utils.iterators.CTypeListIterator
import javax.inject.Inject

@HiltViewModel
class FeedbackViewModel @Inject constructor(
    private val feedbackRepository: FeedbackRepository,
) : ViewModel() {

    //-----------------------------------GENERAL-----------------------------------
    /**
     * Установить идентификатор заказа
     * */
    fun setOrderId(value: Int) {
        orderId.value = value
    }

    /**
     * Идентификатор заказа
     * */
    private val orderId = MutableLiveData<Int>()

    /**
     * Список шагов в виде ресурса [Resource]
     *
     * (!) Значение меняется при изменении [orderId]
     * */
    //TODO из данной переменной брать статус и ошибку (при наличии)
    private val stepListWithState: LiveData<Resource<List<FeedbackStep>>> =
        orderId.switchMap { orderId ->
            liveData(Dispatchers.IO) {
                emit(Resource.loading())
                emit(feedbackRepository.getFeedbackSteps())
            }
        }

    val loadingState = stepListWithState.map {
        it.toLoadingState()
    }

    /**
     * Список шагов
     *
     * (!) Значение берётся при успешной загрузке и наличии данных в [stepListWithState]
     * */
    private val stepList = MediatorLiveData<List<FeedbackStep>>().apply {
        addSource(stepListWithState) {
            Log.e("QUESTIONS", "$it")

            if (it.status == Status.SUCCESS && it.data != null) {
                stepIterator = CTypeListIterator(it.data)
                value = it.data
            }
        }
    }

    /** Итератор списка шагов */
    private lateinit var stepIterator: CTypeListIterator<FeedbackStep>

    /**
     * Текущий шаг
     *
     * (!) Значение меняется при изменении [stepList] и берётся значение по указанному элементу из
     * списка [CTypeListIterator.startIndex].
     *
     * (!) Если значение будет взято до изменения [stepList], то будет вызвано исключение
     * */
    val currentStep = MediatorLiveData<FeedbackStep>().apply {
        addSource(stepList) {
            value = it.getOrNull(stepIterator.startIndex) ?: run {
                /*
                * Проверка, что в списке шагов есть элемент с индексом, указанным началом при создании
                * stepIterator
                * */
                throw IllegalStateException("No fragment of index ${stepIterator.startIndex} was found in step list ")
            }
        }
    }

    /**
     * Получить индекс шага в списке и размер всего списка
     *
     * @param step шаг, индексы которого необходимы
     * */
    private fun getIndicesByStep(step: FeedbackStep): FeedbackIndicesOfSteps {
        //Список шагов, включенных в последовательность
        val filteredList = stepList.value?.filter {
            it is FeedbackFragment && it.includedInSequence
        }

        //Индекс указанного шага из отфильтрованного списка
        val curIndex = filteredList?.indexOf(step) ?: 0

        //Размер отфильтрованного списка
        val total = filteredList?.size ?: 0

        return FeedbackIndicesOfSteps(
            stepIndex = curIndex,
            total = total,
        )
    }

    /** Выполнить действие для каждого отдельного фрагмента */
    fun executeStepAction() {
        viewModelScope.launch {
            val res = stepAction()
            stepActionState.emit(res)
            if (res.status == Status.SUCCESS) {
                goNext()
            }
        }
    }

    /**
     * Состояние сохранения внесённых данных
     * */
    val stepActionState = MutableSharedFlow<Resource<Unit>>(replay = 1)

    /** Действие, которое должно выполняться для каждого отдельного фрагмента */
    private suspend fun stepAction(): Resource<Unit> {
        return when (currentStep.value) {
            else -> {
                Resource.success()
            }
        }
    }

    /**
     * Переход к следующему экрану, вплоть до закрытия отзыва
     * */
    /*
    * ---Логика работы---
    * Итератор списка инициализирован и в списке есть следующий элемент:
    *   да -> устанавливает элемент текущим шагом    *
    *   нет -> устанавливается флаг, что отзыв должен быть закрыт
    * */
    private fun goNext() {
        //Если не конец списка справа
        if (this::stepIterator.isInitialized && stepIterator.hasNext()) {
            currentStep.value = stepIterator.next()
        }
        //Конец списка
        else {
            goFinish(true)
        }
    }

    /**
     * Переход к предыдущему экрану, вплоть до закрытия отзыва
     * */
    /*
    * ---Логика работы---
    * Итератор списка инициализирован и в списке есть предыдущий элемент:
    *   да -> устанавливает элемент текущим шагом
    *   нет -> устанавливается флаг, что отзыв должен быть закрыт
    * */
    fun goBack() {
        //Если не конец списка слева
        if (this::stepIterator.isInitialized && stepIterator.hasPrevious()) {
            currentStep.value = stepIterator.previous()
        }
        //Конец списка
        else {
            goFinish(true)
        }
    }

    /** Перейти к этапу закрытия отзыва */
    //Устанавливается флаг, что отзыв должен быть закрыт
    fun goFinish(force: Boolean = false) {
        /*
        * Если нужно отображать алерт перед закрытием экрана отзыва, то раскомментировать
        * */
//        if (!force) {
//            showAlert.value = true
//        } else {
//            currentStep.value = FeedbackStep.Finish
//        }

        currentStep.value = FeedbackStep.Finish
    }

    /*
    * Если нужно отображать алерт перед закрытием экрана отзыва, то раскомментировать
    * */
//    val showAlert = MutableLiveData(false)

    /**
     * Текст кнопки внизу экран в зависимости от текущего шага
     *
     * (!) Значение меняется при изменении [currentStep]
     * */
    val btnActionTitle = MediatorLiveData<ActionButtonState>().apply {
        addSource(currentStep) {
            value = when (it) {
                //Финальный экран отзыва - "Спасибо за отзыв"
                FeedbackStep.FinalScreen -> {
                    ActionButtonState.Visible(
                        R.string.close
                    )
                }
                //Шаг закрытия анкеты
                FeedbackStep.Finish -> {
                    ActionButtonState.Idle
                }
                //Во всех остальных случаях - "Далее"
                else -> {
                    ActionButtonState.Visible(
                        R.string.further
                    )
                }
            }
        }
    }

    /**
     * Состояние слайдера шагов отзыва внизу экрана
     *
     * (!) Значение меняется при изменении [currentStep]
     * */
    val stepSliderState = MediatorLiveData<StepSliderState>().apply {
        addSource(currentStep) { step ->
            value = when (step) {
                //При состоянии Finish не менять предыдущее состояние слайдера
                FeedbackStep.Finish -> {
                    StepSliderState.Idle
                }
                //На финальном экране скрывать слайдер
                FeedbackStep.FinalScreen -> {
                    StepSliderState.Gone
                }

                else -> {
                    val indices = getIndicesByStep(step)

                    StepSliderState.Visible(
                        stepIndex = indices.stepIndex,
                        totalSteps = indices.total
                    )
                }
            }
        }
    }

    //-----------------------------------GENERAL-----------------------------------

    fun saveAnswer(answer: FeedbackAnswerModel) {
        stepList.value?.find { step ->
            step == currentStep.value
        }?.let { step ->
            if (step is FeedbackAnswer) {
                step.savedAnswer = answer
            }
        } ?: run {
            Log.e(
                "Saving answer",
                "No fragment of index ${stepIterator.startIndex} was found in step list "
            )
        }
    }

    companion object {
        //-----------------------------------GENERAL-----------------------------------
        const val WRONG_EXPECTED_TYPE = "This type wasn't expected in this fragment"

        /**
         * Состояние кнопки действия ("Далее") в активити
         * */
        sealed class ActionButtonState {
            /** Отображать кнопку */
            class Visible(
                @StringRes val text: Int,
            ) : ActionButtonState()

            /** Скрыть кнопку (INVISIBLE) */
            object Invisible : ActionButtonState()

            /** Скрыть кнопку (GONE) */
            object Gone : ActionButtonState()

            /** Оставить состояние кнопки без изменений */
            object Idle : ActionButtonState()
        }

        /**
         * Состояние слайдера с номером шага
         * */
        sealed class StepSliderState {
            /**
             * Отображать слайдер
             *
             * @param stepIndex индекс текущего шага последовательности
             * @param totalSteps кол-во шагов последовательности
             * */
            class Visible(
                val stepIndex: Int,
                val totalSteps: Int,
            ) : StepSliderState()

            /** Скрыть слайдер (INVISIBLE) */
            object Invisible : StepSliderState()

            /** Скрыть слайдер (GONE) */
            object Gone : StepSliderState()

            /** Оставить состояния слайдера без изменений */
            object Idle : StepSliderState()
        }
        //-----------------------------------GENERAL-----------------------------------
    }
}