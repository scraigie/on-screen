package uk.co.scraigie.onscreen.core.framework.contracts.configuration

import io.reactivex.Single

interface IConfigurationInteractor {
    val baseImageUrlSingle: Single<String>
}