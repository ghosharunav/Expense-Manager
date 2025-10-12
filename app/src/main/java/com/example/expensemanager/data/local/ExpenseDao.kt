// ============================================================
// STEP 3: ROOM DAO (DATA ACCESS OBJECT)
// ============================================================
// File: data/local/ExpenseDao.kt
package com.example.expensemanager.data.local

import androidx.room.*
import com.example.expensemanager.data.model.Expense
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Expense entity.
 * Defines all database operations for expenses.
 */
@Dao
interface ExpenseDao {

    /**
     * Get all expenses sorted by date (newest first), then by creation time
     * Returns Flow for automatic UI updates when data changes
     *
     * @return Flow emitting list of all expenses
     */
    @Query("SELECT * FROM expenses ORDER BY date DESC, createdAt DESC")
    fun getAllExpenses(): Flow<List<Expense>>

    /**
     * Get a single expense by ID
     * Suspend function for coroutine execution
     *
     * @param id Expense ID
     * @return Expense object or null if not found
     */
    @Query("SELECT * FROM expenses WHERE id = :id")
    suspend fun getExpenseById(id: Long): Expense?

    /**
     * Get all expenses for a specific category
     *
     * @param category Category name
     * @return Flow emitting list of expenses in that category
     */
    @Query("SELECT * FROM expenses WHERE category = :category ORDER BY date DESC")
    fun getExpensesByCategory(category: String): Flow<List<Expense>>

    /**
     * Search expenses by keyword in description or category
     * Uses LIKE operator for partial matching
     *
     * @param query Search keyword
     * @return Flow emitting list of matching expenses
     */
    @Query("""
        SELECT * FROM expenses 
        WHERE description LIKE '%' || :query || '%' 
        OR category LIKE '%' || :query || '%'
        ORDER BY date DESC
    """)
    fun searchExpenses(query: String): Flow<List<Expense>>

    /**
     * Get expenses within a date range
     * Useful for generating reports
     *
     * @param startDate Start of date range (timestamp)
     * @param endDate End of date range (timestamp)
     * @return Flow emitting list of expenses in range
     */
    @Query("SELECT * FROM expenses WHERE date >= :startDate AND date <= :endDate ORDER BY date DESC")
    fun getExpensesByDateRange(startDate: Long, endDate: Long): Flow<List<Expense>>

    /**
     * Insert a new expense
     * OnConflictStrategy.REPLACE means if ID exists, replace it
     *
     * @param expense Expense to insert
     * @return ID of inserted expense
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: Expense): Long

    /**
     * Insert multiple expenses at once
     * Useful for batch operations or importing data
     *
     * @param expenses List of expenses to insert
     * @return List of inserted IDs
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpenses(expenses: List<Expense>): List<Long>

    /**
     * Update an existing expense
     *
     * @param expense Expense with updated values
     */
    @Update
    suspend fun updateExpense(expense: Expense)

    /**
     * Delete a specific expense
     *
     * @param expense Expense to delete
     */
    @Delete
    suspend fun deleteExpense(expense: Expense)

    /**
     * Delete expense by ID
     *
     * @param id Expense ID to delete
     */
    @Query("DELETE FROM expenses WHERE id = :id")
    suspend fun deleteExpenseById(id: Long)

    /**
     * Delete all expenses
     * Use with caution - this clears the entire database
     */
    @Query("DELETE FROM expenses")
    suspend fun deleteAllExpenses()

    /**
     * Get count of all expenses
     *
     * @return Total number of expenses
     */
    @Query("SELECT COUNT(*) FROM expenses")
    suspend fun getExpenseCount(): Int

    /**
     * Get sum of all expenses
     *
     * @return Total amount of all expenses
     */
    @Query("SELECT SUM(amount) FROM expenses")
    suspend fun getTotalExpenses(): Double?
}