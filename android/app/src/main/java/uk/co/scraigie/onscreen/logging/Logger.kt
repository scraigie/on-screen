package uk.co.scraigie.onscreen.logging

import uk.co.scraigie.onscreen.core.logging.ILogger
import uk.co.scraigie.onscreen.core.logging.ILoggerFactory
import kotlin.reflect.KClass

class Logger : ILoggerFactory {
    override fun create(tag: String): ILogger {
        return object : ILogger {
            override fun v(message: String, throwable: Throwable?) {
                logIO("V", tag, message, throwable)
            }

            override fun d(message: String, throwable: Throwable?) {
                logIO("D", tag, message, throwable)
            }

            override fun i(message: String, throwable: Throwable?) {
                logIO("I", tag, message, throwable)
            }

            override fun wtf(message: String, throwable: Throwable?) {
                logIO("WTF", tag, message, throwable)
            }

            override fun w(message: String, throwable: Throwable?) {
                logIO("W", tag, message, throwable)
            }

            override fun e(message: String, throwable: Throwable?) {
                logIO("E", tag, message, throwable)
            }
        }
    }

    private fun logIO(level: String, tag: String, message: String, throwable: Throwable?) {
        println("[" + System.currentTimeMillis() + "] " + tag + "/" + level + " " + message)
        throwable?.printStackTrace()
    }

    companion object FACTORY {
        fun create(type: KClass<*>) : ILogger {
            return Logger().create(type.simpleName ?: "DEFAULT_TAG")
        }
    }
}