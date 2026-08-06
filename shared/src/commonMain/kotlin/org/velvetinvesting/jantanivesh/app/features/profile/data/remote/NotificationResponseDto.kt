package org.velvetinvesting.jantanivesh.app.features.profile.data.remote

import kotlinx.serialization.Serializable
import org.velvetinvesting.jantanivesh.app.features.profile.domain.model.NotificationDomain
import org.velvetinvesting.jantanivesh.app.features.profile.domain.model.NotificationSubType

@Serializable
data class NotificationResponseDto(
    val success: Boolean,
    val message: String,
    val data: NotificationDataDto
)

@Serializable
data class NotificationDataDto(
    val total: Int,
    val page: Int,
    val limit: Int,
    val notifications: List<NotificationDto>
)

@Serializable
data class NotificationDto(
    val id: String,
    val user_id: String,
    val type: String,
    val title: String,
    val body: String,
    val payload: NotificationPayloadDto,
    val is_read: Boolean,
    val createdAt: String,
    val readAt: String?
)

@Serializable
data class NotificationPayloadDto(
    val txn: String? = null,
    val sub_type: String? = null
)

@Serializable
data class UnreadStatusResponseDto(
    val success: Boolean,
    val message: String,
    val data: UnreadStatusDataDto
)

@Serializable
data class UnreadStatusDataDto(
    val has_unread: Boolean
)

fun NotificationResponseDto.toDomain(): List<NotificationDomain> {
    return data.notifications.map { it.toDomain() }
}

fun NotificationDto.toDomain(): NotificationDomain {
    return NotificationDomain(
        id = id,
        title = title,
        body = body,
        subType = when (payload.sub_type) {
            "ALERT" -> NotificationSubType.ALERT
            "FUND_INC" -> NotificationSubType.FUND_INC
            "FUND_DEC" -> NotificationSubType.FUND_DEC
            "REMINDER" -> NotificationSubType.REMINDER
            "NOTIFICATION" -> NotificationSubType.NOTIFICATION
            else -> NotificationSubType.UNKNOWN
        },
        createdAt = createdAt,
        isRead = is_read
    )
}