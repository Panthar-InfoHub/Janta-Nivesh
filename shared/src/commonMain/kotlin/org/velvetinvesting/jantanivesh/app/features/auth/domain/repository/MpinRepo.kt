package org.velvetinvesting.jantanivesh.app.features.auth.domain.repository

import org.velvetinvesting.jantanivesh.app.core.networking.ErrorDomain
import org.velvetinvesting.jantanivesh.app.core.networking.NetworkResponse

interface MpinRepo {

    /** Creates the MPIN the first time and overwrites it after that — the endpoint is the same. */
    suspend fun updateMpin(mpin: String): NetworkResponse<Unit, ErrorDomain>

    /** Succeeds only when [mpin] matches the stored one; a mismatch comes back as a server error. */
    suspend fun verifyMpin(mpin: String): NetworkResponse<Unit, ErrorDomain>
}
