package org.velvetinvesting.jantanivesh.app.features.core.data.remote.model.userdata

import kotlinx.serialization.Serializable

@Serializable
data class MetaData(
    val is_onboarding_completed: Boolean,
    val onboarding_stage: Int
)