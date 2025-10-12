// ============================================================
// STEP 15.2: TYPOGRAPHY DEFINITIONS
// ============================================================
// File: ui/theme/Type.kt
package com.example.expensemanager.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Material Design 3 Typography System
 * Defines text styles used throughout the app
 *
 * Typography Scale:
 * - Display: Largest text (headlines, hero text)
 * - Headline: Large emphasis text
 * - Title: Medium emphasis text
 * - Body: Main content text
 * - Label: Small UI text (buttons, chips)
 */
val Typography = Typography(

    // ═══════════════════════════════════════════════════════
    // DISPLAY STYLES - Largest text on screen
    // ═══════════════════════════════════════════════════════
    displayLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp
    ),
    displayMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp
    ),
    displaySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp
    ),

    // ═══════════════════════════════════════════════════════
    // HEADLINE STYLES - Section headers
    // ═══════════════════════════════════════════════════════
    headlineLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),

    // ═══════════════════════════════════════════════════════
    // TITLE STYLES - Card titles, list items
    // ═══════════════════════════════════════════════════════
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),
    titleSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),

    // ═══════════════════════════════════════════════════════
    // BODY STYLES - Main content text (MOST USED)
    // ═══════════════════════════════════════════════════════
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    bodySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    ),

    // ═══════════════════════════════════════════════════════
    // LABEL STYLES - Buttons, chips, small UI elements
    // ═══════════════════════════════════════════════════════
    labelLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)

/**
 * USAGE EXAMPLES:
 * ──────────────
 * Text("Hello", style = MaterialTheme.typography.displayLarge)   // Big hero text
 * Text("Title", style = MaterialTheme.typography.titleMedium)    // Card title
 * Text("Content", style = MaterialTheme.typography.bodyLarge)    // Paragraph text
 * Text("Button", style = MaterialTheme.typography.labelLarge)    // Button text
 *
 * CUSTOMIZATION:
 * ─────────────
 * To use custom fonts:
 * 1. Add font files to res/font/ directory
 * 2. Create FontFamily:
 *    val MyFont = FontFamily(Font(R.font.myfont))
 * 3. Replace FontFamily.Default with MyFont
 */