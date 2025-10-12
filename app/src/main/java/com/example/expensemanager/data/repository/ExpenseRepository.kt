// ============================================================
// STEP 5: REPOSITORY LAYER
// ============================================================
// File: data/repository/ExpenseRepository.kt
package com.example.expensemanager.data.repository

import com.example.expensemanager.data.local.ExpenseDao
import com.example.expensemanager.data.model.Expense
import kotlinx.coroutines.flow.Flow
import java.util.*

/**
 * Repository class that abstracts data access from the ViewModel.
 * This is the single source of truth for expense data.
 *
 * Benefits:
 * - Separates business logic from data access
 * - Makes code more testable (can mock repository in tests)
 * - Can easily add remote data source alongside local database
 * - Provides clean API for ViewModels
 *
 * @property expenseDao DAO for database operations
 */
class ExpenseRepository(private val expenseDao: ExpenseDao) {

    // ========================================
    // READ OPERATIONS
    // ========================================

    /**
     * Get all expenses as Flow (reactive updates)
     * UI will automatically update when data changes
     */
    fun getAllExpenses(): Flow<List<Expense>> {
        return expenseDao.getAllExpenses()
    }

    /**
     * Get expenses filtered by category
     *
     * @param category Category name to filter by
     * @return Flow of filtered expenses
     */
    fun getExpensesByCategory(category: String): Flow<List<Expense>> {
        return expenseDao.getExpensesByCategory(category)
    }

    /**
     * Search expenses by keyword
     *
     * @param query Search term
     * @return Flow of matching expenses
     */
    fun searchExpenses(query: String): Flow<List<Expense>> {
        return expenseDao.searchExpenses(query)
    }

    /**
     * Get single expense by ID
     *
     * @param id Expense ID
     * @return Expense or null
     */
    suspend fun getExpenseById(id: Long): Expense? {
        return expenseDao.getExpenseById(id)
    }

    /**
     * Get expenses within date range
     *
     * @param startDate Start of range
     * @param endDate End of range
     * @return Flow of expenses in range
     */
    fun getExpensesByDateRange(startDate: Date, endDate: Date): Flow<List<Expense>> {
        return expenseDao.getExpensesByDateRange(startDate.time, endDate.time)
    }

    /**
     * Get total count of expenses
     *
     * @return Number of expenses
     */
    suspend fun getExpenseCount(): Int {
        return expenseDao.getExpenseCount()
    }

    /**
     * Get sum of all expenses
     *
     * @return Total amount
     */
    suspend fun getTotalExpenses(): Double {
        return expenseDao.getTotalExpenses() ?: 0.0
    }

    // ========================================
    // WRITE OPERATIONS
    // ========================================

    /**
     * Insert new expense
     *
     * @param expense Expense to insert
     * @return ID of inserted expense
     */
    suspend fun insertExpense(expense: Expense): Long {
        return expenseDao.insertExpense(expense)
    }

    /**
     * Insert multiple expenses
     *
     * @param expenses List of expenses
     * @return List of inserted IDs
     */
    suspend fun insertExpenses(expenses: List<Expense>): List<Long> {
        return expenseDao.insertExpenses(expenses)
    }

    /**
     * Update existing expense
     *
     * @param expense Expense with updated values
     */
    suspend fun updateExpense(expense: Expense) {
        expenseDao.updateExpense(expense)
    }

    /**
     * Delete expense
     *
     * @param expense Expense to delete
     */
    suspend fun deleteExpense(expense: Expense) {
        expenseDao.deleteExpense(expense)
    }

    /**
     * Delete expense by ID
     *
     * @param id Expense ID
     */
    suspend fun deleteExpenseById(id: Long) {
        expenseDao.deleteExpenseById(id)
    }

    /**
     * Delete all expenses (use with caution!)
     */
    suspend fun deleteAllExpenses() {
        expenseDao.deleteAllExpenses()
    }

    // ========================================
    // BUSINESS LOGIC / COMPUTED VALUES
    // ========================================

    /**
     * Get today's expenses
     *
     * @return Flow of today's expenses
     */
    fun getTodayExpenses(): Flow<List<Expense>> {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startOfDay = calendar.time

        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        val endOfDay = calendar.time

        return getExpensesByDateRange(startOfDay, endOfDay)
    }

    /**
     * Get this week's expenses
     *
     * @return Flow of this week's expenses
     */
    fun getWeekExpenses(): Flow<List<Expense>> {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startOfWeek = calendar.time

        calendar.add(Calendar.DAY_OF_WEEK, 6)
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        val endOfWeek = calendar.time

        return getExpensesByDateRange(startOfWeek, endOfWeek)
    }

    /**
     * Get this month's expenses
     *
     * @return Flow of this month's expenses
     */
    fun getMonthExpenses(): Flow<List<Expense>> {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startOfMonth = calendar.time

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        val endOfMonth = calendar.time

        return getExpensesByDateRange(startOfMonth, endOfMonth)
    }
}

/**
 * USAGE IN VIEWMODEL:
 *
 * class ExpenseViewModel(private val repository: ExpenseRepository) : ViewModel() {
 *     val allExpenses = repository.getAllExpenses()
 *         .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
 *
 *     fun addExpense(expense: Expense) = viewModelScope.launch {
 *         repository.insertExpense(expense)
 *     }
 * }
 */
