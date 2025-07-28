package com.leng.jingzhekt.empty;

import java.util.concurrent.atomic.AtomicInteger

interface Identifiable<T> {
    val id: T
}
class AutoIncrementIdProvider<T : Number>(private val initialValue: T) {
    private val idGenerator = AtomicInteger(initialValue.toInt())

    fun nextId(): T {
        @Suppress("UNCHECKED_CAST")
        return when (initialValue) {
            is Int -> idGenerator.getAndIncrement() as T
            is Long -> idGenerator.getAndIncrement().toLong() as T
            else -> throw IllegalArgumentException("Unsupported number type")
        }
    }
}
/*abstract class Empty<ID: Number>(initId: ID): Identifiable {
    companion object{
        private val idGenerator = AtomicInteger(1)

        fun create(name:String, iconResId: Int, level: Level):Classify{
            return Classify(idGenerator.getAndIncrement(), name,iconResId, level)
        }
    }
}*/
