package uk.co.scraigie.onscreen.core.framework.contracts.data

import kotlin.reflect.KClass

interface IApiFactory {
    fun <T : Any> create(service: KClass<T>) : T
}