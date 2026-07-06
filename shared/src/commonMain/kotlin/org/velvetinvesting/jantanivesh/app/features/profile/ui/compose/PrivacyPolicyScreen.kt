package org.velvetinvesting.jantanivesh.app.features.profile.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import jantanivesh.shared.generated.resources.Res
import jantanivesh.shared.generated.resources.pp_contact_email
import jantanivesh.shared.generated.resources.pp_contact_email_label
import jantanivesh.shared.generated.resources.pp_contact_name
import jantanivesh.shared.generated.resources.pp_contact_office
import jantanivesh.shared.generated.resources.pp_contact_office_label
import jantanivesh.shared.generated.resources.pp_contact_phone
import jantanivesh.shared.generated.resources.pp_contact_phone_label
import jantanivesh.shared.generated.resources.pp_section_10_content
import jantanivesh.shared.generated.resources.pp_section_10_title
import jantanivesh.shared.generated.resources.pp_section_11_content
import jantanivesh.shared.generated.resources.pp_section_11_title
import jantanivesh.shared.generated.resources.pp_section_12_title
import jantanivesh.shared.generated.resources.pp_section_1_a_content
import jantanivesh.shared.generated.resources.pp_section_1_a_title
import jantanivesh.shared.generated.resources.pp_section_1_b_content
import jantanivesh.shared.generated.resources.pp_section_1_b_title
import jantanivesh.shared.generated.resources.pp_section_1_c_content
import jantanivesh.shared.generated.resources.pp_section_1_c_title
import jantanivesh.shared.generated.resources.pp_section_1_title
import jantanivesh.shared.generated.resources.pp_section_2_intro
import jantanivesh.shared.generated.resources.pp_section_2_title
import jantanivesh.shared.generated.resources.pp_section_3_content
import jantanivesh.shared.generated.resources.pp_section_3_title
import jantanivesh.shared.generated.resources.pp_section_4_intro
import jantanivesh.shared.generated.resources.pp_section_4_title
import jantanivesh.shared.generated.resources.pp_section_5_content
import jantanivesh.shared.generated.resources.pp_section_5_title
import jantanivesh.shared.generated.resources.pp_section_6_content
import jantanivesh.shared.generated.resources.pp_section_6_title
import jantanivesh.shared.generated.resources.pp_section_7_content
import jantanivesh.shared.generated.resources.pp_section_7_title
import jantanivesh.shared.generated.resources.pp_section_8_content
import jantanivesh.shared.generated.resources.pp_section_8_title
import jantanivesh.shared.generated.resources.pp_section_9_content
import jantanivesh.shared.generated.resources.pp_section_9_title
import jantanivesh.shared.generated.resources.pp_share_item_1
import jantanivesh.shared.generated.resources.pp_share_item_2
import jantanivesh.shared.generated.resources.pp_share_item_3
import jantanivesh.shared.generated.resources.pp_share_item_4
import jantanivesh.shared.generated.resources.pp_share_item_5
import jantanivesh.shared.generated.resources.pp_use_item_1
import jantanivesh.shared.generated.resources.pp_use_item_2
import jantanivesh.shared.generated.resources.pp_use_item_3
import jantanivesh.shared.generated.resources.pp_use_item_4
import jantanivesh.shared.generated.resources.pp_use_item_5
import jantanivesh.shared.generated.resources.pp_use_item_6
import jantanivesh.shared.generated.resources.pp_use_item_7
import jantanivesh.shared.generated.resources.pp_use_item_8
import jantanivesh.shared.generated.resources.privacy_policy_consent
import jantanivesh.shared.generated.resources.privacy_policy_header
import jantanivesh.shared.generated.resources.privacy_policy_intro
import jantanivesh.shared.generated.resources.privacy_policy_title
import org.jetbrains.compose.resources.stringResource
import org.velvetinvesting.jantanivesh.app.core.theme.InterFontFamily
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
            .background(Color.White)
    ) {
        BackHeader(
            title = stringResource(
                Res.string
                    .privacy_policy_title),
            showBack = true,
            onBack = onBack,
            modifier = Modifier.padding(horizontal = Spacing.dp16)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                Text(
                    text = stringResource(Res.string.privacy_policy_header),
                    fontSize = 28.sp,
                    lineHeight = 32.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = InterFontFamily,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(Res.string.privacy_policy_intro)+ "\n" + stringResource(Res.string.privacy_policy_consent),
                    fontSize = 14.sp,
                    fontFamily = InterFontFamily,
                    color = Color(0xff3D4B37),
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(24.dp))
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
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                SectionTitle(stringResource(Res.string.pp_section_2_title))
                Text(
                    text = stringResource(Res.string.pp_section_2_intro),
                    fontSize = 14.sp,
                    fontFamily = InterFontFamily,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                val items = listOf(
                    Res.string.pp_use_item_1, Res.string.pp_use_item_2,
                    Res.string.pp_use_item_3, Res.string.pp_use_item_4,
                    Res.string.pp_use_item_5, Res.string.pp_use_item_6,
                    Res.string.pp_use_item_7, Res.string.pp_use_item_8
                )
                items.forEach { BulletItem(stringResource(it)) }
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                SectionTitle(stringResource(Res.string.pp_section_3_title))
                Text(
                    text = stringResource(Res.string.pp_section_3_content),
                    fontSize = 14.sp,
                    fontFamily = InterFontFamily,
                    color = Color.Gray,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                SectionTitle(stringResource(Res.string.pp_section_4_title))
                Text(
                    text = stringResource(Res.string.pp_section_4_intro),
                    fontSize = 14.sp,
                    fontFamily = InterFontFamily,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                val shareItems = listOf(
                    Res.string.pp_share_item_1, Res.string.pp_share_item_2,
                    Res.string.pp_share_item_3, Res.string.pp_share_item_4,
                    Res.string.pp_share_item_5
                )
                shareItems.forEach { BulletItem(stringResource(it)) }
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                SectionTitle(stringResource(Res.string.pp_section_5_title))
                Text(
                    text = stringResource(Res.string.pp_section_5_content),
                    fontSize = 14.sp,
                    fontFamily = InterFontFamily,
                    color = Color.Gray,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                SectionTitle(stringResource(Res.string.pp_section_6_title))
                Text(
                    text = stringResource(Res.string.pp_section_6_content),
                    fontSize = 14.sp,
                    fontFamily = InterFontFamily,
                    color = Color.Gray,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                SectionTitle(stringResource(Res.string.pp_section_7_title))
                Text(
                    text = stringResource(Res.string.pp_section_7_content),
                    fontSize = 14.sp,
                    fontFamily = InterFontFamily,
                    color = Color.Gray,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                SectionTitle(stringResource(Res.string.pp_section_8_title))
                Text(
                    text = stringResource(Res.string.pp_section_8_content),
                    fontSize = 14.sp,
                    fontFamily = InterFontFamily,
                    color = Color.Gray,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                SectionTitle(stringResource(Res.string.pp_section_9_title))
                Text(
                    text = stringResource(Res.string.pp_section_9_content),
                    fontSize = 14.sp,
                    fontFamily = InterFontFamily,
                    color = Color.Gray,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                SectionTitle(stringResource(Res.string.pp_section_10_title))
                Text(
                    text = stringResource(Res.string.pp_section_10_content),
                    fontSize = 14.sp,
                    fontFamily = InterFontFamily,
                    color = Color.Gray,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                SectionTitle(stringResource(Res.string.pp_section_11_title))
                Text(
                    text = stringResource(Res.string.pp_section_11_content),
                    fontSize = 14.sp,
                    fontFamily = InterFontFamily,
                    color = Color.Gray,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                SectionTitle(stringResource(Res.string.pp_section_12_title))
                Text(
                    text = stringResource(Res.string.pp_contact_name),
                    fontSize = 14.sp,
                    fontFamily = InterFontFamily,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 12.dp)
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

private @Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold,
        fontFamily = InterFontFamily,
        color = Color.Black,
        modifier = Modifier.padding(bottom = 12.dp)
    )
}

private @Composable
fun SubSection(title: String, content: String) {
    Text(
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold,)) {
                append(title)
            }
            withStyle(style = SpanStyle(fontWeight = FontWeight.Normal, color =Color(0xff3D4B37) )) {
                append(content)
            }
        },
        fontSize = 14.sp,
        fontFamily = InterFontFamily,
        lineHeight = 20.sp,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

private @Composable
fun BulletItem(text: String) {
    Row(modifier = Modifier.padding(bottom = 6.dp, start = 8.dp)) {
        Text(
            text = "•",
            fontSize = 14.sp,
            fontFamily = InterFontFamily,
            color = Color.Gray,
            modifier = Modifier.width(16.dp)
        )
        Text(
            text = text,
            fontSize = 14.sp,
            fontFamily = InterFontFamily,
            color = Color.Gray,
            lineHeight = 20.sp
        )
    }
}

private @Composable
fun ContactItem(label: String, value: String) {
    Column(modifier = Modifier.padding(bottom = 12.dp, start = 16.dp)) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = InterFontFamily,
            color = Color.Gray
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontFamily = InterFontFamily,
            color = Color.Gray,
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
