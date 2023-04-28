package ru.aioki.myapplication.ui.mainStage

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.databinding.BindableItem
import dagger.hilt.android.AndroidEntryPoint
import ru.aioki.myapplication.R
import ru.aioki.myapplication.base.BaseFragment
import ru.aioki.myapplication.data.domain.Event
import ru.aioki.myapplication.data.repository.LoginRepository
import ru.aioki.myapplication.databinding.CalendarDayLayoutBinding
import ru.aioki.myapplication.databinding.CalendarHeaderBinding
import ru.aioki.myapplication.databinding.FragmentEventsBinding
import ru.aioki.myapplication.databinding.ItemEventBinding
import ru.aioki.myapplication.ui.feedbackStage.FeedbackActivity
import ru.aioki.myapplication.utils.daysOfWeekFromLocale
import ru.aioki.myapplication.utils.setSupportTextAppearance
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentEventsBinding>() {

    override fun getLayoutID() = R.layout.fragment_events

    private val today = LocalDate.now() //Сегодня
    private var startDate: LocalDate = LocalDate.now() //Период с
    private var endDate: LocalDate? = null //Период до
    private val daysOfWeek = daysOfWeekFromLocale()//Дни недели
    private val footerDateFormatter = DateTimeFormatter.ofPattern("dd.MM.yy")
    private val buttonDateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
    private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private val formatterSecond: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    private var dealDates = mutableListOf<LocalDate>().apply {
        add(LocalDate.parse("2023-09-01", formatter))
        add(LocalDate.parse("2023-04-26", formatter))
        add(LocalDate.parse("2023-05-09", formatter))
        add(LocalDate.parse("2023-06-01", formatter))
    }

    private val adapter = GroupieAdapter()

    private val startBackground: GradientDrawable by lazy {
        ResourcesCompat.getDrawable(
            resources,
            R.drawable.continuous_selected_bg_start,
            null
        ) as GradientDrawable
    }

    private val endBackground: GradientDrawable by lazy {
        ResourcesCompat.getDrawable(
            resources,
            R.drawable.continuous_selected_bg_end,
            null
        ) as GradientDrawable
    }


    private var isFirstOpen = true


    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var loginRepository: LoginRepository

    override fun setUpViews() {
        super.setUpViews()

        binding.apply {
            BottomSheetBehavior.from(sheetBottom).apply {
                peekHeight = 0
                halfExpandedRatio = 0.5F
                isHideable = true
                skipCollapsed = true
                this.state = BottomSheetBehavior.STATE_HIDDEN
            }

            //Выбор сегоднящнего дня
            setUpDate(startDate, endDate)

            //Биндер -  Дни
            calendarView.dayBinder = object : DayBinder<DayViewContainer> {
                // Вызывается только тогда, когда нужен новый контейнер
                override fun create(view: View) = DayViewContainer(view)

                // Вызывается каждый раз, когда нужно использовать контейнер
                override fun bind(container: DayViewContainer, day: CalendarDay) {
                    container.day = day
                    val textView = container.binding.calendarDayText
                    val bgView = container.binding.bgView

                    textView.text = null
                    textView.background = null
                    bgView.visibility = View.INVISIBLE

                    val startDate = startDate
                    val endDate = endDate

                    when (day.owner) {
                        DayOwner.THIS_MONTH -> {
                            textView.text = day.day.toString()
                            when {
                                startDate == day.date && endDate == null -> { //Выбрана только одна дата
                                    bgView.visibility = View.VISIBLE
                                    bgView.setBackgroundResource(R.drawable.bg_calendar_selected)
                                }
                                day.date == startDate -> { //Период: Начальная дата
                                    textView.background = startBackground
                                }
                                endDate != null && (day.date > startDate && day.date < endDate) -> { //Период: Даты между начальной и конечной
                                    textView.setBackgroundResource(R.drawable.continuous_selected_bg_middle)
                                }
                                day.date == endDate -> { //Период: Конечная дата
                                    textView.background = endBackground
                                }
                                dealDates.contains(day.date) -> { //Задача
                                    bgView.visibility = View.VISIBLE
                                    bgView.setBackgroundResource(R.drawable.bg_calendar_deal)
                                }
                            }
                            if (day.date == today) { //Сегодняшняя дата
                                textView.setTextColor(
                                    ResourcesCompat.getColor(
                                        resources,
                                        R.color.blue1,
                                        null
                                    )
                                )
                            } else {
                                textView.setTextColor( //Отсальные даты
                                    ResourcesCompat.getColor(
                                        resources,
                                        R.color.white,
                                        null
                                    )
                                )
                            }
                        }
                        // Разрывы между месяцами.
                        DayOwner.PREVIOUS_MONTH, DayOwner.NEXT_MONTH -> {
                        }
                    }
                }
            }

            //Биндер - Заголовок месяца
            calendarView.monthHeaderBinder = object :
                MonthHeaderFooterBinder<MonthViewContainer> {
                override fun create(view: View) = MonthViewContainer(view)

                override fun bind(
                    container: MonthViewContainer,
                    month: CalendarMonth
                ) {
                    //Состояние заголовки месяца
                    @SuppressLint("SetTextI18n")
                    if (month.month == today.monthValue) {
                        container.textView.apply {
                            text = "${
                                month.yearMonth.month.getDisplayName(
                                    TextStyle.FULL_STANDALONE,
                                    Locale.getDefault()
                                )
                                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                            } ${month.year}"

                            setSupportTextAppearance(
                                requireContext(),
                                R.style.TextAppearance_JBMono_Bold_S16
                            )
                            setTextColor(
                                ResourcesCompat.getColor(
                                    resources,
                                    R.color.white,
                                    null
                                )
                            )
                        }
                    } else {
                        container.textView.apply {
                            text = "${
                                month.yearMonth.month.getDisplayName(
                                    TextStyle.FULL_STANDALONE,
                                    Locale.getDefault()
                                )
                                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                            } ${month.year}"
                            setSupportTextAppearance(
                                requireContext(),
                                R.style.TextAppearance_JBMono_Bold_S14
                            )
                            setTextColor(
                                ResourcesCompat.getColor(
                                    resources,
                                    R.color.white,
                                    null
                                )
                            )
                        }
                    }
                }
            }

        }

        //Передача дат в calendarView
        binding.calendarView.setup(
            YearMonth.now().minusMonths(1),
            YearMonth.now().plusMonths(1),
            daysOfWeek.first()
        )
        startDate = LocalDate.of(
            YearMonth.now().minusMonths(1).year,
            YearMonth.now().minusMonths(1).month,
            1
        )
        endDate = LocalDate.of(
            YearMonth.now().plusMonths(1).year,
            YearMonth.now().plusMonths(1).month,
            YearMonth.now().plusMonths(1).lengthOfMonth()
        )
        if (isFirstOpen) {
            setUpDate(startDate, endDate)
            isFirstOpen = false
        }
        bindSummaryViews()

        binding.calendarView.scrollToMonth(YearMonth.now())

        binding.rvEventList.layoutManager = LinearLayoutManager(
            requireContext(), RecyclerView.VERTICAL, false
        )
        binding.rvEventList.adapter = adapter

    }

    override fun observeView() {
        super.observeView()
        binding.rlSelectDate.setOnClickListener {
            val rotation = if (binding.icArrow.rotation == 0f) 180f else 0f
            binding.icArrow.animate().rotation(rotation).setDuration(200).start()
            binding.icArrow.rotation = ((binding.icArrow.rotation + 180f) % 360f)
            BottomSheetBehavior.from(binding.sheetBottom).apply {
                this.state =
                    if (rotation != 0f) BottomSheetBehavior.STATE_EXPANDED else BottomSheetBehavior.STATE_HIDDEN
            }
        }
        //Калбек состоянии bottomSheet
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.sheetBottom)
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // handle onSlide
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> { //Открыт
                        binding.cvPeriod.visibility = View.VISIBLE
                        binding.icArrow.rotation = 180f
                    }
                    BottomSheetBehavior.STATE_HIDDEN -> { //Закрыт
                        binding.cvPeriod.visibility = View.GONE
                        binding.icArrow.rotation = 0f
                    }

                    BottomSheetBehavior.STATE_SETTLING -> { //Закрывается (открывается)
                        binding.cvPeriod.visibility = View.GONE
                    }
                    else -> {
                    }
                }
            }
        })
        //Кнопка - Выбрать дату (или период)
        binding.cvPeriod.setOnClickListener {
            setUpDate(startDate, endDate)
            viewModel.setDate(startDate, endDate)
            BottomSheetBehavior.from(this@MainFragment.binding.sheetBottom).apply {
                this.state = BottomSheetBehavior.STATE_HIDDEN
            }
        }
    }

    override fun observeData() {
        super.observeData()
        viewModel.newEvents.observe(viewLifecycleOwner) {
            adapter.clear()
            it.map { i -> EventBindableItem(i) }?.let { it1 -> adapter.addAll(it1) }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setUpDate(startDate: LocalDate, endDate: LocalDate?) {
        //Начало
        val startDateString = startDate.atStartOfDay().format(formatter)
        //Фильрация
        if (endDate != null) {
            //Конец
            binding.tvDate.text =
                "${footerDateFormatter.format(startDate)} - ${footerDateFormatter.format(endDate)}"
            val endDateString = endDate.atStartOfDay().format(formatter)
            when (startDate.month) {
                endDate.month -> {
                    binding.tvInfoDate.text =
                        "Задания с ${startDate.dayOfMonth} по ${endDate.dayOfMonth} ${
                            startDate.month.getDisplayName(
                                TextStyle.FULL,
                                Locale.getDefault()
                            )
                        }"
                }
                else -> {
                    binding.tvInfoDate.text =
                        "Задания с ${startDate.dayOfMonth} ${
                            startDate.month.getDisplayName(
                                TextStyle.FULL,
                                Locale.getDefault()
                            )
                        } по ${endDate.dayOfMonth} ${
                            endDate.month.getDisplayName(
                                TextStyle.FULL,
                                Locale.getDefault()
                            )
                        }"
                }
            }

        } else {
            when (startDate) {
                LocalDate.now() -> {
                    binding.tvDate.text = getString(R.string.Today)
                }
                LocalDate.now().plusDays(1) -> {
                    binding.tvDate.text = getString(R.string.Tomorrow)
                }
                LocalDate.now().minusDays(1) -> {
                    binding.tvDate.text = getString(R.string.Yesterday)
                }
                else -> {
                    binding.tvDate.text = buttonDateFormatter.format(startDate)
                }
            }


            binding.tvInfoDate.text =
                "Задания на ${startDate.dayOfMonth} ${
                    startDate.month.getDisplayName(
                        TextStyle.FULL,
                        Locale.getDefault()
                    )
                }"
        }
    }


    inner class DayViewContainer(view: View) : ViewContainer(view) {
        lateinit var day: CalendarDay // Will be set when this container is bound.
        val binding = CalendarDayLayoutBinding.bind(view)

        init {
            view.setOnClickListener {
                if (day.owner == DayOwner.THIS_MONTH && (day.date == today || day.date.isAfter(
                        today
                    ) || day.date.isBefore(
                        today
                    ))
                ) {
                    val date = day.date
                    if (date < startDate && endDate == null) {
                        endDate = startDate
                        startDate = date
                    } else if (date < startDate || endDate != null) {
                        startDate = date
                        endDate = null
                    } else if (date != startDate) {
                        endDate = date
                    }
                    this@MainFragment.binding.calendarView.notifyCalendarChanged()
                    bindSummaryViews()
                }
            }
        }
    }

    //Заголовки месяцев
    inner class MonthViewContainer(view: View) : ViewContainer(view) {
        val textView = CalendarHeaderBinding.bind(view).calendarHeaderText
    }

    @SuppressLint("SetTextI18n")
    private fun bindSummaryViews() { //При выборе даты
        when { //При выборе даты
            endDate != null -> {
                binding.tvFooterDate.text =
                    "${footerDateFormatter.format(startDate)} - ${
                        footerDateFormatter.format(
                            endDate
                        )
                    }"
            }
            else -> {
                binding.tvFooterDate.text = footerDateFormatter.format(startDate)
            }
        }
    }

    inner class EventBindableItem(private val event: Event) : BindableItem<ItemEventBinding>() {
        override fun bind(viewBinding: ItemEventBinding, position: Int) {
            viewBinding.tvTitle.text = event.name
            viewBinding.tvDesc.text = event.desc
            viewBinding.tvDate.text =
                "${backendISOToString(event.startDate)} - ${backendISOToString(event.endDate)}"
            viewBinding.address.text = event.address
            if (position == 0) {
                viewBinding.btnAbout.text = "Оставьте отзыв"
                viewBinding.btnAbout.setOnClickListener {
                    activity?.startActivity(
                        Intent(
                            viewBinding.root.context,
                            FeedbackActivity::class.java
                        ).apply {
                            putExtras(FeedbackActivity.getBundle(1))
                        })
                }
            } else {
                viewBinding.btnAbout.setOnClickListener {
                }
            }

        }

        override fun getLayout() = R.layout.item_event


    }


    companion object {
        private val formatterISO: DateTimeFormatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSz")

        fun backendISOToString(date: String?): String? {
            if (date.isNullOrBlank()) return null
            val convertDate = LocalDateTime.ofInstant(
                LocalDateTime.parse(date, formatterISO).toInstant(
                    ZoneOffset.UTC
                ), ZoneId.of("GMT+0")
            )
            return convertDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
        }
    }

}