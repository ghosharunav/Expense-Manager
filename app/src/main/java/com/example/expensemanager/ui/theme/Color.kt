// ============================================================
// STEP 15.1: COLOR DEFINITIONS
// ============================================================
// File: ui/theme/Color.kt
package com.example.expensemanager.ui.theme

import androidx.compose.ui.graphics.Color

// ═══════════════════════════════════════════════════════════
// MATERIAL DESIGN 3 COLOR PALETTE
// ═══════════════════════════════════════════════════════════

/**
 * Light Theme Colors (for Dark Mode)
 * These are used when the system is in dark mode
 */
val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

/**
 * Dark Theme Colors (for Light Mode)
 * These are used when the system is in light mode
 */
val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

// ═══════════════════════════════════════════════════════════
// CUSTOM APP COLORS
// ═══════════════════════════════════════════════════════════

/**
 * Custom colors for expense-specific UI elements
 * These can be used throughout the app for consistency
 */
val ExpenseRed = Color(0xFFD32F2F)      // For expense amounts (negative)
val ExpenseGreen = Color(0xFF388E3C)    // For income/savings (future feature)
val ExpenseBlue = Color(0xFF1976D2)     // For informational elements
val ExpenseOrange = Color(0xFFFF6F00)   // For warnings/alerts

// ═══════════════════════════════════════════════════════════
// ADDITIONAL SEMANTIC COLORS (Optional - for future use)
// ═══════════════════════════════════════════════════════════

// Chart Colors (if you want custom chart colors)
val ChartColor1 = Color(0xFFE57373)  // Red
val ChartColor2 = Color(0xFF81C784)  // Green
val ChartColor3 = Color(0xFF64B5F6)  // Blue
val ChartColor4 = Color(0xFFFFD54F)  // Yellow
val ChartColor5 = Color(0xFFBA68C8)  // Purple

// Background variations
val LightBackground = Color(0xFFFFFBFE)
val DarkBackground = Color(0xFF1C1B1F)

/**
 * USAGE NOTES:
 * ───────────
 * - Purple40 is primary color for light mode
 * - Purple80 is primary color for dark mode
 * - ExpenseRed used for all expense amounts
 * - Numbers 40/80 represent color tone (40 = darker, 80 = lighter)
 *
 * CUSTOMIZATION:
 * ─────────────
 * To change app colors, modify Purple40 and Purple80
 * Example: For blue theme, use:
 *   val Blue40 = Color(0xFF1976D2)
 *   val Blue80 = Color(0xFF90CAF9)
 */