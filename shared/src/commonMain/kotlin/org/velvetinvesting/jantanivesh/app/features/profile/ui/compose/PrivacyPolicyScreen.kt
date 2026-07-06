package org.velvetinvesting.jantanivesh.app.features.profile.ui.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.tooling.preview.Preview
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.*
import org.velvetinvesting.jantanivesh.app.core.theme.Black
import org.velvetinvesting.jantanivesh.app.core.theme.Gray
import org.velvetinvesting.jantanivesh.app.core.theme.JantaNiveshTheme
import org.velvetinvesting.jantanivesh.app.core.theme.Spacing
import org.velvetinvesting.jantanivesh.app.features.core.ui.composables.BackHeader

@Composable
fun PrivacyPolicyScreen(
    onBack: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        BackHeader(
            title = stringResource(Res.string.privacy_policy_title),
            showBack = true,
            onBack = onBack,
            modifier = Modifier.padding(horizontal = Spacing.dp16)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(Spacing.dp16)
        ) {
            item {
                Text(
                    text = stringResource(Res.string.privacy_policy_header),
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold, fontSize = 28.sp, lineHeight = 34.sp)
                )
                Spacer(modifier = Modifier.height(Spacing.dp16))
                Text(
                    text = stringResource(Res.string.privacy_policy_intro)+ "\n" + stringResource(Res.string.privacy_policy_consent),
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(Spacing.dp24))
            }

            item {
                SectionTitle(stringResource(Res.string.pp_section_1_title))
                SubSection(
                    title = stringResource(Res.string.pp_section_1_a_title),
                    content = stringResource(Res.string.pp_section_1_a_content)
                )
                SubSection(
                    title = stringResource(Res.string.pp_section_1_b_title),
                    content = stringResource(Res.string.pp_section_1_b_content)
                )
                SubSection(
                    title = stringResource(Res.string.pp_section_1_c_title),
                    content = stringResource(Res.string.pp_section_1_c_content)
                )
                Spacer(modifier = Modifier.height(Spacing.dp24))
            }

            item {
                SectionTitle(stringResource(Res.string.pp_section_2_title))
                Text(
                    text = stringResource(Res.string.pp_section_2_intro),
                    fontSize = 14.sp,
                    color = Gray,
                    modifier = Modifier.padding(bottom = Spacing.dp8)
                )
                val items = listOf(
                    Res.string.pp_use_item_1, Res.string.pp_use_item_2,
                    Res.string.pp_use_item_3, Res.string.pp_use_item_4,
                    Res.string.pp_use_item_5, Res.string.pp_use_item_6,
                    Res.string.pp_use_item_7, Res.string.pp_use_item_8
                )
                items.forEach { BulletItem(stringResource(it)) }
                Spacer(modifier = Modifier.height(Spacing.dp24))
            }

            item {
                SectionTitle(stringResource(Res.string.pp_section_3_title))
                Text(
                    text = stringResource(Res.string.pp_section_3_content),
                    fontSize = 14.sp,
                    color = Gray,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(Spacing.dp24))
            }

            item {
                SectionTitle(stringResource(Res.string.pp_section_4_title))
                Text(
                    text = stringResource(Res.string.pp_section_4_intro),
                    fontSize = 14.sp,
                    color = Gray,
                    modifier = Modifier.padding(bottom = Spacing.dp8)
                )
                val shareItems = listOf(
                    Res.string.pp_share_item_1, Res.string.pp_share_item_2,
                    Res.string.pp_share_item_3, Res.string.pp_share_item_4,
                    Res.string.pp_share_item_5
                )
                shareItems.forEach { BulletItem(stringResource(it)) }
                Spacer(modifier = Modifier.height(Spacing.dp24))
            }

            item {
                SectionTitle(stringResource(Res.string.pp_section_5_title))
                Text(
                    text = stringResource(Res.string.pp_section_5_content),
                    fontSize = 14.sp,
                    color = Gray,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(Spacing.dp24))
            }

            item {
                SectionTitle(stringResource(Res.string.pp_section_6_title))
                Text(
                    text = stringResource(Res.string.pp_section_6_content),
                    fontSize = 14.sp,
                    color = Gray,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(Spacing.dp24))
            }

            item {
                SectionTitle(stringResource(Res.string.pp_section_7_title))
                Text(
                    text = stringResource(Res.string.pp_section_7_content),
                    fontSize = 14.sp,
                    color = Gray,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(Spacing.dp24))
            }

            item {
                SectionTitle(stringResource(Res.string.pp_section_8_title))
                Text(
                    text = stringResource(Res.string.pp_section_8_content),
                    fontSize = 14.sp,
                    color = Gray,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(Spacing.dp24))
            }

            item {
                SectionTitle(stringResource(Res.string.pp_section_9_title))
                Text(
                    text = stringResource(Res.string.pp_section_9_content),
                    fontSize = 14.sp,
                    color = Gray,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(Spacing.dp24))
            }

            item {
                SectionTitle(stringResource(Res.string.pp_section_10_title))
                Text(
                    text = stringResource(Res.string.pp_section_10_content),
                    fontSize = 14.sp,
                    color = Gray,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(Spacing.dp24))
            }

            item {
                SectionTitle(stringResource(Res.string.pp_section_11_title))
                Text(
                    text = stringResource(Res.string.pp_section_11_content),
                    fontSize = 14.sp,
                    color = Gray,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(Spacing.dp24))
            }

            item {
                SectionTitle(stringResource(Res.string.pp_section_12_title))
                Text(
                    text = stringResource(Res.string.pp_contact_name),
                    fontSize = 14.sp,
                    color = Gray,
                    modifier = Modifier.padding(bottom = Spacing.dp12)
                )
                ContactItem(
                    label = stringResource(Res.string.pp_contact_email_label),
                    value = stringResource(Res.string.pp_contact_email)
                )
                ContactItem(
                    label = stringResource(Res.string.pp_contact_phone_label),
                    value = stringResource(Res.string.pp_contact_phone)
                )
                ContactItem(
                    label = stringResource(Res.string.pp_contact_office_label),
                    value = stringResource(Res.string.pp_contact_office)
                )
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold,
        color = Black,
        modifier = Modifier.padding(bottom = Spacing.dp12)
    )
}

@Composable
private fun SubSection(title: String, content: String) {
    Text(
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append(title)
            }
            withStyle(style = SpanStyle(fontWeight = FontWeight.Normal, color =Color(0xff3D4B37) )) {
                append(content)
            }
        },
        fontSize = 14.sp,
        lineHeight = 20.sp,
        modifier = Modifier.padding(bottom = Spacing.dp8)
    )
}

@Composable
private fun BulletItem(text: String) {
    Row(modifier = Modifier.padding(bottom = Spacing.dp6, start = Spacing.dp8)) {
        Text(
            text = "•",
            fontSize = 14.sp,
            color = Gray,
            modifier = Modifier.width(Spacing.dp16)
        )
        Text(
            text = text,
            fontSize = 14.sp,
            color = Gray,
            lineHeight = 20.sp
        )
    }
}

@Composable
private fun ContactItem(label: String, value: String) {
    Column(modifier = Modifier.padding(bottom = Spacing.dp12, start = Spacing.dp16)) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Gray
        )
        Text(
            text = value,
            fontSize = 14.sp,
            color = Gray,
            lineHeight = 20.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PrivacyPolicyScreenPreview() {
    JantaNiveshTheme {
        PrivacyPolicyScreen()
    }
}