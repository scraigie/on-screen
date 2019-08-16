package uk.co.scraigie.onscreen.core.logging

interface ILoggerFactory {
    fun create(tag: String): ILogger
}

interface ILogger {
    fun v(message: String, throwable: Throwable?)

    fun d(message: String, throwable: Throwable?)

    fun i(message: String, throwable: Throwable?)

    fun wtf(message: String, throwable: Throwable?)

    fun w(message: String, throwable: Throwable?)

    fun e(message: String, throwable: Throwable?)

    fun v(message: String) {
        v(message, null)
    }

    fun d(message: String) {
        d(message, null)
    }

    fun i(message: String) {
        i(message, null)
    }

    fun wtf(message: String) {
        wtf(message, null)
    }

    fun w(message: String) {
        w(message, null)
    }

    fun e(message: String) {
        e(message, null)
    }
}
