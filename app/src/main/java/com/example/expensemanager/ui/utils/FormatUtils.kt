// ============================================================
// STEP 7: UTILITY FUNCTIONS
// ============================================================
// File: ui/utils/FormatUtils.kt
package com.example.expensemanager.ui.utils

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Format double value as currency string
 * Uses Indian locale (₹) by default
 *
 * @param amount Amount to format
 * @return Formatted currency string (e.g., "₹1,234.56")
 */
fun formatCurrency(amount: Double): String {
    val format = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
    return format.format(amount)
}

/**
 * Format Date as readable string
 *
 * @param date Date to format
 * @param pattern Format pattern (default: "dd MMM yyyy")
 * @return Formatted date string (e.g., "04 Oct 2025")
 */
fun formatDate(date: Date, pattern: String = "dd MMM yyyy"): String {
    val format = SimpleDateFormat(pattern, Locale.getDefault())
    return format.format(date)
}

/**
 * Format Date with time
 *
 * @param date Date to format
 * @return Formatted date-time string (e.g., "04 Oct 2025, 02:30 PM")
 */
fun formatDateTime(date: Date): String {
    val format = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
    return format.format(date)
}

/**
 * Format amount in short form (K, L, Cr notation)
 *
 * @param amount Amount to format
 * @return Short form string (e.g., "1.2K", "2.5L", "1.5Cr")
 */
fun formatAmountShort(amount: Double): String {
    return when {
        amount >= 10_000_000 -> String.format("%.1fCr", amount / 10_000_000)
        amount >= 100_000 -> String.format("%.1fL", amount / 100_000)
        amount >= 1_000 -> String.format("%.1fK", amount / 1_000)
        else -> String.format("%.0f", amount)
    }
}

/**
 * Get relative date string (Today, Yesterday, etc.)
 *
 * @param date Date to compare
 * @return Relative date string
 */
fun getRelativeDate(date: Date): String {
    val calendar = Calendar.getInstance()
    val today = calendar.apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis

    val yesterday = calendar.apply {
        add(Calendar.DAY_OF_YEAR, -1)
    }.timeInMillis

    return when {
        date.time >= today -> "Today"
        date.time >= yesterday -> "Yesterday"
        else -> formatDate(date)
    }
}

/**
 * Calculate percentage of total
 *
 * @param amount Part amount
 * @param total Total amount
 * @return Percentage string with % symbol (e.g., "25.5%")
 */
fun formatPercentage(amount: Double, total: Double): String {
    if (total == 0.0) return "0%"
    val percentage = (amount / total) * 100
    return String.format("%.1f%%", percentage)
}

/**
 * Validate expense amount
 *
 * @param amount Amount string to validate
 * @return True if valid, false otherwise
 */
fun isValidAmount(amount: String): Boolean {
    if (amount.isBlank()) return false
    val amountValue = amount.toDoubleOrNull() ?: return false
    return amountValue > 0
}

/**
 * Parse amount string to double
 *
 * @param amount Amount string
 * @return Double value or 0.0 if invalid
 */
fun parseAmount(amount: String): Double {
    return amount.toDoubleOrNull() ?: 0.0
}

/**
 * Format month and year
 *
 * @param date Date to format
 * @return Month-year string (e.g., "October 2025")
 */
fun formatMonthYear(date: Date): String {
    val format = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
    return format.format(date)
}

/**
 * Format time only
 *
 * @param date Date to format
 * @return Time string (e.g., "02:30 PM")
 */
fun formatTime(date: Date): String {
    val format = SimpleDateFormat("hh:mm a", Locale.getDefault())
    return format.format(date)
}

/**
 * Get day of week
 *
 * @param date Date to check
 * @return Day name (e.g., "Monday")
 */
fun getDayOfWeek(date: Date): String {
    val format = SimpleDateFormat("EEEE", Locale.getDefault())
    return format.format(date)
}

/**
 * Check if date is today
 *
 * @param date Date to check
 * @return True if date is today
 */
fun isToday(date: Date): Boolean {
    val calendar = Calendar.getInstance()
    val today = calendar.apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis

    calendar.add(Calendar.DAY_OF_YEAR, 1)
    val tomorrow = calendar.timeInMillis

    return date.time in today until tomorrow
}

/**
 * Check if date is this week
 *
 * @param date Date to check
 * @return True if date is in current week
 */
fun isThisWeek(date: Date): Boolean {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    val weekStart = calendar.timeInMillis

    calendar.add(Calendar.WEEK_OF_YEAR, 1)
    val weekEnd = calendar.timeInMillis

    return date.time in weekStart until weekEnd
}

/**
 * Check if date is this month
 *
 * @param date Date to check
 * @return True if date is in current month
 */
fun isThisMonth(date: Date): Boolean {
    val calendar = Calendar.getInstance()
    val currentMonth = calendar.get(Calendar.MONTH)
    val currentYear = calendar.get(Calendar.YEAR)

    calendar.time = date
    val dateMonth = calendar.get(Calendar.MONTH)
    val dateYear = calendar.get(Calendar.YEAR)

    return currentMonth == dateMonth && currentYear == dateYear
}

/**
 * USAGE EXAMPLES:
 *
 * // Format currency
 * val formatted = formatCurrency(1234.56) // "₹1,234.56"
 *
 * // Format date
 * val dateStr = formatDate(Date()) // "04 Oct 2025"
 *
 * // Get relative date
 * val relative = getRelativeDate(Date()) // "Today"
 *
 * // Format percentage
 * val percent = formatPercentage(500.0, 2000.0) // "25.0%"
 *
 * // Validate amount
 * val isValid = isValidAmount("123.45") // true
 */