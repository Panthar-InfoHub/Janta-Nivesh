package org.velvetinvesting.jantanivesh.app.features.core.data.local.mapper

import org.velvetinvesting.jantanivesh.app.features.core.data.remote.model.userdata.UserDataDto
import org.velvetinvesting.jantanivesh.app.features.core.domain.models.UserDataDomain

fun UserDataDto.toDomain(): UserDataDomain = UserDataDomain(
    name = this.data.full_name,
    email = this.data.email,
    mobile = this.data.phone_no
)