package org.velvetinvesting.jantanivesh.app.features.mutualfund.data.remote.model.bundlecart
import kotlinx.serialization.Serializable
@Serializable
data class AddBundleLumpsumRequest(
    val type: String = "LUMPSUM",
    val bundle_id: String,
    val amount: Long
)
