// ============================================================
// STEP 15.3: THEME CONFIGURATION
// ============================================================
// File: ui/theme/Theme.kt
package com.example.expensemanager.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.compose.ui.graphics.Color

/**
 * Dark color scheme for the app
 * Used when system is in dark mode
 */
private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    error = ExpenseRed,
    background = DarkBackground,
    surface = DarkBackground,
    onPrimary = Color(0xFF381E72),
    onSecondary = Color(0xFF332D41),
    onTertiary = Color(0xFF492532),
    onBackground = Color(0xFFE6E1E5),
    onSurface = Color(0xFFE6E1E5)
)

/**
 * Light color scheme for the app
 * Used when system is in light mode (DEFAULT)
 */
private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    error = ExpenseRed,
    background = LightBackground,
    surface = LightBackground,
    onPrimary = Color(0xFFFFFFFF),
    onSecondary = Color(0xFFFFFFFF),
    onTertiary = Color(0xFFFFFFFF),
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F)
)

/**
 * Main theme composable that wraps the entire app
 * Automatically switches between light and dark themes
 *
 * @param darkTheme Whether to use dark theme (default: follows system)
 * @param dynamicColor Whether to use Material You dynamic colors (Android 12+)
 * @param content The app content to be themed
 */
@Composable
fun ExpenseManagerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    // ═══════════════════════════════════════════════════════
    // SELECT COLOR SCHEME
    // ═══════════════════════════════════════════════════════
    val colorScheme = when {
        // Use dynamic colors on Android 12+ if enabled
        // Dynamic colors = colors extracted from wallpaper
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) {
                dynamicDarkColorScheme(context)
            } else {
                dynamicLightColorScheme(context)
            }
        }

        // Use static dark/light schemes on older Android versions
        // or when dynamic colors are disabled
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    // ═══════════════════════════════════════════════════════
    // UPDATE SYSTEM BARS (Status Bar & Navigation Bar)
    // ═══════════════════════════════════════════════════════
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window

            // Set status bar color to match primary color
            window.statusBarColor = colorScheme.primary.toArgb()

            // Set icon colors based on theme
            // Light icons for dark theme, dark icons for light theme
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    // ═══════════════════════════════════════════════════════
    // APPLY MATERIAL THEME
    // ═══════════════════════════════════════════════════════
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

/**
 * ═══════════════════════════════════════════════════════════
 * THEME CUSTOMIZATION GUIDE
 * ═══════════════════════════════════════════════════════════
 *
 * 1. DYNAMIC COLORS (Material You):
 *    ──────────────────────────────
 *    - Android 12+ extracts colors from user's wallpaper
 *    - Creates personalized color scheme automatically
 *    - Set dynamicColor = false to always use static colors
 *    - Example: ExpenseManagerTheme(dynamicColor = false) { }
 *
 * 2. COLOR ROLES EXPLAINED:
 *    ──────────────────────
 *    - primary: Main brand color (app bar, FAB, selected items)
 *    - secondary: Supporting color (chips, secondary actions)
 *    - tertiary: Additional accent color
 *    - error: Error states (we use ExpenseRed for amounts)
 *    - background: Screen background
 *    - surface: Component backgrounds (cards, sheets)
 *    - onPrimary: Text/icons on primary color
 *    - onBackground: Text on background
 *    - onSurface: Text on surface
 *
 * 3. FORCE DARK/LIGHT MODE:
 *    ──────────────────────
 *    ExpenseManagerTheme(darkTheme = true) { }   // Always dark
 *    ExpenseManagerTheme(darkTheme = false) { }  // Always light
 *
 * 4. CUSTOM COLOR SCHEME:
 *    ───────────────────
 *    private val MyColorScheme = lightColorScheme(
 *        primary = Color(0xFF0000FF),     // Blue
 *        secondary = Color(0xFFFF0000),   // Red
 *        // ... other colors
 *    )
 *
 * 5. STATUS BAR CUSTOMIZATION:
 *    ────────────────────────
 *    - Status bar matches primary color by default
 *    - To use transparent: window.statusBarColor = Color.Transparent.toArgb()
 *    - To use custom color: window.statusBarColor = Color(0xFF123456).toArgb()
 *
 * 6. NAVIGATION BAR:
 *    ──────────────
 *    Add to SideEffect block:
 *    window.navigationBarColor = colorScheme.surface.toArgb()
 *
 * ═══════════════════════════════════════════════════════════
 * USAGE IN APP:
 * ═══════════════════════════════════════════════════════════
 *
 * // In MainActivity.kt (already done):
 * setContent {
 *     ExpenseManagerTheme {
 *         // Your app content
 *     }
 * }
 *
 * // Access theme colors in composables:
 * Box(backgroundColor = MaterialTheme.colorScheme.primary)
 * Text(color = MaterialTheme.colorScheme.onPrimary)
 *
 * ═══════════════════════════════════════════════════════════
 */