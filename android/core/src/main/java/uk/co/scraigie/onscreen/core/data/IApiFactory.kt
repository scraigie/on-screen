package uk.co.scraigie.onscreen.core.data

import kotlin.reflect.KClass

interface IApiFactory {
    fun <T : Any> create(service: KClass<T>) : T
}