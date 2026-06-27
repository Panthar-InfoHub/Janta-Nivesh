package org.velvetinvesting.jantanivesh.app.features.portfolio.data.model.report

import kotlinx.serialization.Serializable

@Serializable
data class ReportExportDto(
    val code: Int,
    val message: String,
    val data: ReportDataDto
)

@Serializable
data class ReportDataDto(
    val code: Int,
    val result: String?= null
)