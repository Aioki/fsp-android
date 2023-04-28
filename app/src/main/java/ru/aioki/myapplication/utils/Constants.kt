package ru.aioki.myapplication.utils



const val HOST = "todo.ru"
const val API_VERSION = "v1"
const val API_URL = "https://five-heads.visdom.tech/api/fiveheadsapp-api/v1/"
const val URL = "https://$HOST/"

/**
 * Количество миллисекунд в секунде
 */
const val MS_IN_SEC: Long = 1000

/**
 * Количество метров в километре
 */
const val METERS_IN_KM = 1000

/**
 * Базовый URL ссылок
 */
const val BASE_URL = "https://confetkibaranochki.ru/"

/**
 * TODO: Ссылка на страницу поддержки
 */
const val SUPPORT_URL = "${BASE_URL}support/"

const val DAY_AND_MONTH_DATE_FORMAT = "dd MMMM"
const val LOCAL_DATE_FORMAT = "dd.MM.yyyy"
const val REMOTE_DATE_FORMAT = "yyyy-MM-dd"
const val UTC_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssz"
const val DATE_FORMAT_TO_CART = "E, dd MMMM"
const val HOURS_MINUTES_FORMAT = "HH:mm"

const val REQUEST_EXIT_CODE = 9


const val SUGGESTION_API_URL = "https://suggestions.dadata.ru/suggestions/api/4_1/rs/"
const val ADDRESS_ENDPOINT = "suggest/address"
const val PAYMENT_ENDPOINT = "suggest/bank"
const val FMS_ENDPOINT = "suggest/fms_unit"
const val REVERSE_GEOCODING_ENDPOINT = "geolocate/address"

/**
 * Пользовательское соглашение
 * TODO исправить
 */
const val USER_AGREEMENT = "${BASE_URL}agreement.docx"

/**
 * Политика конфиденциальности
 * TODO исправить
 */
const val PRIVACY_POLICY = "${BASE_URL}politics.docx"


const val DATASTORE_NAME: String = "konfetki.datastore"

/**
 * Задержка для избежания множественного клика в ms
 */
const val THROTTLE_TIME_MS = 1000
