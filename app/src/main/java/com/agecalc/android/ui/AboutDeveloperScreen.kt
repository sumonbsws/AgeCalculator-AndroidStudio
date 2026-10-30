package com.agecalc.android.ui

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.agecalc.android.R
import com.agecalc.android.ui.theme.LocalAppThemeColors

/**
 * Launches the native Android phone dialer with the contact number prefilled
 * using [Intent.ACTION_DIAL] (does NOT place a call automatically and requires
 * zero runtime permissions).
 */
fun launchPhoneDialer(context: Context, phoneNumber: String) {
    val sanitizedNumber = phoneNumber.trim()
    val dialIntent = Intent(Intent.ACTION_DIAL).apply {
        data = Uri.parse("tel:$sanitizedNumber")
    }
    try {
        context.startActivity(dialIntent)
    } catch (_: ActivityNotFoundException) {
        // Safely ignore on devices without telephony/dialer support (e.g., certain tablets/TVs)
    }
}

/**
 * Professionally styled Material 3 'About Developer' ModalBottomSheet component.
 * Displays:
 * - Developer Portrait: R.drawable.developer_photo (res/drawable-nodpi/developer_photo.jpg)
 * - Developer Name: Md. Zahidul Hasan - Sumon
 * - Designation: Computer Engineer
 * - Organization: Oni IT
 * - Address: 502 Alpona Plaza, Level-05, 51 New Elephant Road, Dhaka-1205
 * - Contact: 01841198225 (tappable Intent.ACTION_DIAL to open dialer)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutDeveloperSheet(
    onDismiss: () -> Unit
) {
    val theme = LocalAppThemeColors.current
    val context = LocalContext.current
    val contactNumber = stringResource(R.string.about_value_contact)
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = theme.cardSurface,
        contentColor = theme.textPrimary,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Sheet Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.about_screen_title),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = theme.textPrimary
                    )
                    Text(
                        text = stringResource(R.string.app_name),
                        style = MaterialTheme.typography.bodySmall,
                        color = theme.textSecondary
                    )
                }

                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.cd_back),
                        tint = theme.textSecondary
                    )
                }
            }

            HorizontalDivider(color = theme.subtleBorder)

            // Developer Portrait & Identity Banner Card
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = theme.fieldBackground),
                border = BorderStroke(1.dp, theme.subtleBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .clip(RoundedCornerShape(22.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(theme.buttonGradientStart, theme.buttonGradientEnd)
                                )
                            )
                            .padding(2.5.dp)
                            .clip(RoundedCornerShape(19.5.dp))
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.developer_photo),
                            contentDescription = stringResource(R.string.about_value_developer),
                            contentScale = ContentScale.Crop,
                            alignment = Alignment.TopCenter,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(R.string.about_value_organization),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = theme.primary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = stringResource(R.string.about_value_developer),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = theme.textPrimary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = stringResource(R.string.about_value_designation),
                            style = MaterialTheme.typography.bodySmall,
                            color = theme.textSecondary
                        )
                    }
                }
            }

            // Structured Developer Information List
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = theme.cardSurface,
                border = BorderStroke(1.dp, theme.subtleBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    DeveloperDetailRow(
                        label = stringResource(R.string.about_label_developer),
                        value = stringResource(R.string.about_value_developer),
                        leadingIcon = Icons.Default.Person
                    )
                    HorizontalDivider(color = theme.subtleBorder)

                    DeveloperDetailRow(
                        label = stringResource(R.string.about_label_designation),
                        value = stringResource(R.string.about_value_designation),
                        leadingIcon = Icons.Default.Info
                    )
                    HorizontalDivider(color = theme.subtleBorder)

                    DeveloperDetailRow(
                        label = stringResource(R.string.about_label_organization),
                        value = stringResource(R.string.about_value_organization),
                        leadingIcon = Icons.Default.Info
                    )
                    HorizontalDivider(color = theme.subtleBorder)

                    DeveloperDetailRow(
                        label = stringResource(R.string.about_label_address),
                        value = stringResource(R.string.about_value_address),
                        leadingIcon = Icons.Default.LocationOn
                    )
                    HorizontalDivider(color = theme.subtleBorder)

                    DeveloperDetailRow(
                        label = stringResource(R.string.about_label_contact),
                        value = contactNumber,
                        leadingIcon = Icons.Default.Call,
                        onClick = { launchPhoneDialer(context, contactNumber) }
                    )
                }
            }

            // Full-Width Theme Gradient Action Button Opening Phone Dialer
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(theme.buttonGradientStart, theme.buttonGradientEnd)
                        )
                    )
                    .clickable(
                        role = Role.Button,
                        onClick = { launchPhoneDialer(context, contactNumber) }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = null,
                        tint = theme.onButton,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${stringResource(R.string.about_action_open_dialer)} · $contactNumber",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = theme.onButton
                    )
                }
            }

            Text(
                text = stringResource(R.string.footer_developed_by),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = theme.textSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun DeveloperDetailRow(
    label: String,
    value: String,
    leadingIcon: ImageVector,
    onClick: (() -> Unit)? = null
) {
    val theme = LocalAppThemeColors.current

    val rowModifier = if (onClick != null) {
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(role = Role.Button, onClick = onClick)
            .padding(vertical = 2.dp)
    } else {
        Modifier.fillMaxWidth()
    }

    Row(
        modifier = rowModifier,
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .padding(top = 2.dp)
                .size(34.dp)
                .clip(CircleShape)
                .background(theme.softContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                tint = theme.primary,
                modifier = Modifier.size(17.dp)
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = theme.textSecondary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = if (onClick != null) theme.primary else theme.textPrimary
            )
        }
    }
}
