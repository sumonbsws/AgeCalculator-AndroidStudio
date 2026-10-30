package com.agecalc.android.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.agecalc.android.R
import com.agecalc.android.ui.theme.LocalAppThemeColors

@Composable
fun AboutDeveloperScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val theme = LocalAppThemeColors.current
    val context = LocalContext.current
    val contactNumber = stringResource(R.string.about_value_contact)

    val openDialerPrefilled = {
        // ACTION_DIAL opens the Android dialer with the number prefilled without auto-calling or requiring CALL_PHONE permission
        val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$contactNumber"))
        context.startActivity(dialIntent)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = theme.background,
        topBar = {
            Surface(
                color = theme.background,
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.cd_back),
                            tint = theme.textPrimary
                        )
                    }
                    Text(
                        text = stringResource(R.string.about_screen_title),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = theme.textPrimary
                    )
                }
            }
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(vertical = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.footer_developed_by),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = theme.textSecondary,
                    textAlign = TextAlign.Center
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Developer Avatar & App Identity Card
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = theme.cardSurface),
                border = BorderStroke(1.dp, theme.subtleBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = if (theme.isDark) 0.dp else 3.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(theme.buttonGradientStart, theme.buttonGradientEnd)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = theme.onButton,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = stringResource(R.string.app_name),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = theme.primary
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = stringResource(R.string.about_value_developer),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = theme.textPrimary,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "${stringResource(R.string.about_value_designation)} · ${stringResource(R.string.about_value_organization)}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = theme.textSecondary,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Detailed Profile Information Card
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = theme.cardSurface),
                border = BorderStroke(1.dp, theme.subtleBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = if (theme.isDark) 0.dp else 3.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    DeveloperDetailRow(
                        label = stringResource(R.string.about_label_developer),
                        value = stringResource(R.string.about_value_developer)
                    )
                    HorizontalDivider(color = theme.subtleBorder)

                    DeveloperDetailRow(
                        label = stringResource(R.string.about_label_designation),
                        value = stringResource(R.string.about_value_designation)
                    )
                    HorizontalDivider(color = theme.subtleBorder)

                    DeveloperDetailRow(
                        label = stringResource(R.string.about_label_organization),
                        value = stringResource(R.string.about_value_organization)
                    )
                    HorizontalDivider(color = theme.subtleBorder)

                    DeveloperDetailRow(
                        label = stringResource(R.string.about_label_address),
                        value = stringResource(R.string.about_value_address),
                        leadingIcon = Icons.Default.LocationOn
                    )
                    HorizontalDivider(color = theme.subtleBorder)

                    // Tappable Contact Number opening Android dialer prefilled via Intent.ACTION_DIAL
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = theme.softContainer.copy(alpha = 0.65f),
                        border = BorderStroke(1.dp, theme.accentBorder),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .clickable(
                                role = Role.Button,
                                onClick = openDialerPrefilled
                            )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = stringResource(R.string.about_label_contact),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = theme.textSecondary
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = contactNumber,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = theme.primary
                                )
                                Text(
                                    text = stringResource(R.string.about_action_dial_hint),
                                    fontSize = 11.sp,
                                    color = theme.textSecondary
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(theme.primary),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Call,
                                    contentDescription = null,
                                    tint = theme.onPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DeveloperDetailRow(
    label: String,
    value: String,
    leadingIcon: ImageVector? = null
) {
    val theme = LocalAppThemeColors.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        if (leadingIcon != null) {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                tint = theme.primary,
                modifier = Modifier
                    .padding(top = 2.dp)
                    .size(18.dp)
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = theme.textSecondary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = theme.textPrimary
            )
        }
    }
}
