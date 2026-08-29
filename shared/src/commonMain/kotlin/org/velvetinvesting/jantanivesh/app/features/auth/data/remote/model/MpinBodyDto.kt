package org.velvetinvesting.jantanivesh.app.features.auth.data.remote.model

import kotlinx.serialization.Serializable

/**
 * Body for `PATCH /user/`. The endpoint takes the whole profile, but every field is optional and
 * only what is sent is written — so the MPIN alone is all this flow puts on the wire.
 */
@Serializable
data class UpdateMpinBodyDto(
    val mpin: String
)

/** Body for `POST /user/verify-mpin`. */
@Serializable
data class VerifyMpinBodyDto(
    val mpin: String
)
