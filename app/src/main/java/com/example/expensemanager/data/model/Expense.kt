

// ============================================================
// STEP 1: DATA MODEL & ENTITY
// ============================================================
// File: data/model/Expense.kt
package com.example.expensemanager.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

/**
 * Expense entity representing a single expense record in the database.
 *
 * @property id Unique identifier (auto-generated)
 * @property amount Expense amount in currency
 * @property category Category of expense (Food, Transport, etc.)
 * @property date Date when expense occurred
 * @property description Optional description/note about the expense
 * @property createdAt Timestamp when record was created
 */
@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val amount: Double,
    val category: String,
    val date: Date,
    val description: String,
    val createdAt: Date = Date()
)

/**
 * Predefined expense categories for consistent categorization
 */
object ExpenseCategories {
    val categories = listOf(
        "Food & Dining",
        "Transportation",
        "Shopping",
        "Entertainment",
        "Bills & Utilities",
        "Healthcare",
        "Education",
        "Travel",
        "Groceries",
        "Others"
    )

    /**
     * Get a random category (useful for testing/demo data)
     */
    fun getRandomCategory(): String = categories.random()

    /**
     * Check if a category is valid
     */
    fun isValidCategory(category: String): Boolean = categories.contains(category)
}