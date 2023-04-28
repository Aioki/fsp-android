package ru.aioki.myapplication.utils.iterators

/**
 * "C" подобный итератор списка
 *
 * @param list входной список
 * @param startIndex начальный индекс указателя
 * */
class CTypeListIterator<T>(private val list: List<T>, val startIndex: Int = 0) :
    ListIterator<T> {

    init {
        if (startIndex !in list.indices) throw IndexOutOfBoundsException()
    }

    private var currentIndex: Int = startIndex

    override fun hasNext(): Boolean {
        if (currentIndex == -1) return false
        return (currentIndex + 1) < list.size
    }

    override fun next(): T {
        return list[++currentIndex]
    }

    override fun hasPrevious(): Boolean {
        if (list.isEmpty()) return false
        return (currentIndex - 1) >= 0
    }

    override fun nextIndex(): Int {
        return currentIndex + 1
    }

    override fun previous(): T {
        return list[--currentIndex]
    }

    override fun previousIndex(): Int {
        return currentIndex - 1
    }
}