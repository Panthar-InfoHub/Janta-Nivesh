package org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.mutualfundcombined

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val bundle_funds: BundleFunds,
    val normal_funds: NormalFunds
)