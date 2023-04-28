package ru.aioki.myapplication.utils

import androidx.databinding.BindingConversion
import java.text.DecimalFormat

class Decimal : Number, Comparable<Decimal> {
    private var mValue: Number? = null

    private val decimalFormat = DecimalFormat("###,###.###")

    private val compareSame: Int = 0
    private val comparePositive: Int = 1
    private val compareNegative: Int = -1

    //----------Конструкторы----------
    constructor(value: Double) {
        mValue = value
    }

    constructor(value: Int) {
        mValue = value
    }
    //----------Конструкторы----------

    //----------Конвертеры (Decimal -> Number)----------
    override fun toByte(): Byte = mValue?.toByte() ?: 0.toByte()

    override fun toChar(): Char = mValue?.toChar() ?: 0.toChar()

    override fun toDouble(): Double = mValue?.toDouble() ?: 0.toDouble()

    override fun toFloat(): Float = mValue?.toFloat() ?: 0.toFloat()

    override fun toInt(): Int = mValue?.toInt() ?: 0

    override fun toLong(): Long = mValue?.toLong() ?: 0.toLong()

    override fun toShort(): Short = mValue?.toShort() ?: 0.toShort()

    override fun toString(): String = decimalFormat.format(mValue)
    //----------Конвертеры (Decimal -> Number)----------

    //----------Математические преобразования----------
    /** Добавляет к этому (this) числу другое (other) число.*/
    operator fun plus(other: Decimal): Decimal {
        return Decimal(this.toDouble() + other.toDouble())
    }

    operator fun plus(other: Double): Decimal {
        return Decimal(this.toDouble() + other)
    }

    /** Вычитает другое (other) число из этого (this) числа. */
    operator fun minus(other: Decimal): Decimal {
        return Decimal(this.toDouble() - other.toDouble())
    }

    /** Умножает это (this) число на другое (other). */
    operator fun times(other: Decimal): Decimal {
        return Decimal(this.toDouble() * other.toDouble())
    }

    /** Делит это (this) число на другое (other). */
    operator fun div(other: Decimal): Decimal {
        return Decimal(this.toDouble() / other.toDouble())
    }
    //----------Математические преобразования----------

    //----------Сравнения----------
    /**
     * Сравнивает this значение с другим (other) указанным числом.
     * @param other число для сравнения
     * @return 0 - если this == other, отрицательное число - если this < other,
     * положительное число - если this > other.
     */
    override operator fun compareTo(other: Decimal): Int =
        this.mValue?.toDouble()?.compareTo(other.toDouble()) ?: compareNegative

    /**
     * Сравнивает this значение с другим (other) указанным числом.
     * @param other число для сравнения
     * @return 0 - если this == other, отрицательное число - если this < other,
     * положительное число - если this > other.
     */
    operator fun compareTo(other: Double): Int =
        this.mValue?.toDouble()?.compareTo(other) ?: compareNegative

    /**
     * Сравнивает this значение с другим (other) указанным числом.
     * @param other число для сравнения
     * @return 0 - если this == other, отрицательное число - если this < other,
     * положительное число - если this > other.
     */
    operator fun compareTo(other: Float): Int =
        this.mValue?.toDouble()?.compareTo(other) ?: compareNegative

    /**
     * Сравнивает this значение с другим (other) указанным числом.
     * @param other число для сравнения
     * @return 0 - если this == other, отрицательное число - если this < other,
     * положительное число - если this > other.
     */
    operator fun compareTo(other: Int): Int =
        this.mValue?.toInt()?.compareTo(other) ?: compareNegative

    /**
     * Сравнивает this значение с другим (other) указанным числом.
     * @param other число для сравнения
     * @return 0 - если this == other, отрицательное число - если this < other,
     * положительное число - если this > other.
     */
    operator fun compareTo(other: Long): Int =
        this.mValue?.toLong()?.compareTo(other) ?: compareNegative

    /**
     * Сравнивает this значение с другим (other) указанным числом.
     * @param other число для сравнения
     * @return 0 - если this == other, отрицательное число - если this < other,
     * положительное число - если this > other.
     */
    operator fun compareTo(other: Short): Int =
        this.mValue?.toShort()?.compareTo(other) ?: compareNegative

    /**
     * Сравнивает this значение с другим (other) указанным числом.
     * @param other число для сравнения
     * @return 0 - если this == other, отрицательное число - если this < other,
     * положительное число - если this > other.
     */
    operator fun compareTo(other: Byte): Int =
        this.mValue?.toByte()?.compareTo(other) ?: compareNegative

    //----------Сравнения----------


    companion object {
        //----------Конвертеры (Number -> Decimal)----------
        fun Double?.toDecimal(): Decimal {
            return this?.let { Decimal(it) } ?: 0.toDecimal()
        }

        fun Int.toDecimal(): Decimal {
            return Decimal(this)
        }
        //----------Конвертеры (Number -> Decimal)----------

        //----------Extension functions----------
        fun Decimal?.toString(): String {
            return this?.toString() ?: Decimal(0).toString()
        }

        fun Decimal?.toDefaultString(): String {
            return this?.toDouble().toString()
        }
        //----------Extension functions----------

    }
}

//Конвертер Decimal для установки в поля TextView
@BindingConversion
fun setTextForDecimal(value: Decimal): String {
    return value.toString()
}