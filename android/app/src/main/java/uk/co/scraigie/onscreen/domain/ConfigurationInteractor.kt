//package uk.co.scraigie.onscreen.domain
//
//import io.reactivex.Single
//import uk.co.scraigie.onscreen.core.configuration.IConfigurationInteractor
//import uk.co.scraigie.onscreen.data.config.ConfigurationApi
//
//class ConfigurationInteractor(val configurationApi: ConfigurationApi) : IConfigurationInteractor{
//    override val baseImageUrlSingle: Single<String> =
//        configurationApi.get()
//            .map { it.baseImageUrl }
//}