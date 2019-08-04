package uk.co.scraigie.onscreen.data

import android.content.Context
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import uk.co.scraigie.onscreen.core.framework.contracts.data.IApiFactory
import java.io.IOException
import kotlin.reflect.KClass

class ApiFactory(context: Context, apiUrl: String) : IApiFactory {

    override fun <T : Any> create(service: KClass<T>) : T = apiBuilder.create(service.java)

    private val cache = Cache(context.cacheDir, MAX_CACHE_SIZE_BYTES)

    init {
        try {
            cache.evictAll()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private val cachedClient: OkHttpClient = OkHttpClient.Builder()
        .cache(cache)
        .addInterceptor(HttpLoggingInterceptor())
        .build()

    private val apiBuilder by lazy {
        Retrofit.Builder()
            .baseUrl(apiUrl)
            .client(cachedClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    companion object {
        const val MAX_CACHE_SIZE_BYTES = (8 * 1024 * 1024).toLong()
    }
}