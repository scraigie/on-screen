package uk.co.scraigie.onscreen.data.config

import com.google.gson.annotations.SerializedName
import io.reactivex.Single
import retrofit2.http.GET

interface ConfigurationApi {
    @GET("v1/api/configuration")
    fun get(): Single<ConfigurationDTO>
}

data class ConfigurationDTO(
    @SerializedName("image_base_url")
    val baseImageUrl: String = ""
)