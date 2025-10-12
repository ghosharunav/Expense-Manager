// ============================================================
// STEP 6: VIEWMODEL
// ============================================================
// File: ui/viewmodel/ExpenseViewModel.kt
package com.example.expensemanager.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensemanager.data.model.Expense
import com.example.expensemanager.data.repository.ExpenseRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

/**
 * ViewModel for managing expense-related UI state and business logic.
 *
 * Responsibilities:
 * - Expose data to UI as StateFlow
 * - Handle user actions (add, edit, delete)
 * - Process filtering and searching
 * - Calculate summaries and totals
 * - Survive configuration changes
 *
 * @property repository Data repository for expense operations
 */
class ExpenseViewModel(private val repository: ExpenseRepository) : ViewModel() {

    // ========================================
    // UI STATE - Search & Filter
    // ========================================

    /**
     * Current search query entered by user
     */
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    /**
     * Currently selected category filter (null = show all)
     */
    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    // ========================================
    // EXPENSE DATA
    // ========================================

    /**
     * All expenses from repository, filtered by search query and category
     * Automatically updates UI when database changes
     */
    val expenses: StateFlow<List<Expense>> = combine(
        repository.getAllExpenses(),
        _searchQuery,
        _selectedCategory
    ) { allExpenses, query, category ->
        allExpenses.filter { expense ->
            // Filter by search query
            val matchesSearch = query.isEmpty() ||
                    expense.description.contains(query, ignoreCase = true) ||
                    expense.category.contains(query, ignoreCase = true)

            // Filter by category
            val matchesCategory = category == null || expense.category == category

            matchesSearch && matchesCategory
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000), // Keep alive for 5 seconds after last subscriber
        initialValue = emptyList()
    )

    // ========================================
    // SUMMARY TOTALS
    // ========================================

    /**
     * Total expenses for today
     */
    val todayTotal: StateFlow<Double> = expenses.map { list ->
        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time

        list.filter { it.date >= today }.sumOf { it.amount }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    /**
     * Total expenses for current week
     */
    val weekTotal: StateFlow<Double> = expenses.map { list ->
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val weekStart = calendar.time

        list.filter { it.date >= weekStart }.sumOf { it.amount }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    /**
     * Total expenses for current month
     */
    val monthTotal: StateFlow<Double> = expenses.map { list ->
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val monthStart = calendar.time

        list.filter { it.date >= monthStart }.sumOf { it.amount }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    /**
     * Category-wise totals for charts and statistics
     */
    val categoryTotals: StateFlow<Map<String, Double>> = expenses.map { list ->
        list.groupBy { it.category }
            .mapValues { entry -> entry.value.sumOf { it.amount } }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    /**
     * Total count of all expenses
     */
    val expenseCount: StateFlow<Int> = expenses.map { it.size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    /**
     * Grand total of all expenses
     */
    val grandTotal: StateFlow<Double> = expenses.map { list ->
        list.sumOf { it.amount }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    // ========================================
    // USER ACTIONS - Filtering & Searching
    // ========================================

    /**
     * Update search query
     *
     * @param query New search text
     */
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    /**
     * Set selected category filter
     * Pass null to show all categories
     *
     * @param category Category name or null
     */
    fun setSelectedCategory(category: String?) {
        _selectedCategory.value = category
    }

    /**
     * Clear all filters
     */
    fun clearFilters() {
        _searchQuery.value = ""
        _selectedCategory.value = null
    }

    // ========================================
    // USER ACTIONS - CRUD Operations
    // ========================================

    /**
     * Insert new expense
     *
     * @param expense Expense to add
     */
    fun insertExpense(expense: Expense) {
        viewModelScope.launch {
            repository.insertExpense(expense)
        }
    }

    /**
     * Insert multiple expenses at once
     *
     * @param expenses List of expenses to add
     */
    fun insertExpenses(expenses: List<Expense>) {
        viewModelScope.launch {
            repository.insertExpenses(expenses)
        }
    }

    /**
     * Update existing expense
     *
     * @param expense Expense with updated values
     */
    fun updateExpense(expense: Expense) {
        viewModelScope.launch {
            repository.updateExpense(expense)
        }
    }

    /**
     * Delete expense
     *
     * @param expense Expense to delete
     */
    fun deleteExpense(expense: Expense) {
        viewModelScope.launch {
            repository.deleteExpense(expense)
        }
    }

    /**
     * Delete all expenses
     * Use with caution!
     */
    fun deleteAllExpenses() {
        viewModelScope.launch {
            repository.deleteAllExpenses()
        }
    }

    /**
     * Get expense by ID (for editing)
     * Use this in EditExpenseScreen to load expense details
     *
     * @param id Expense ID
     * @return Expense or null
     */
    suspend fun getExpenseById(id: Long): Expense? {
        return repository.getExpenseById(id)
    }

    // ========================================
    // UTILITY FUNCTIONS
    // ========================================

    /**
     * Generate sample expenses for demo/testing
     * Call this to populate database with test data
     */
    fun generateSampleExpenses() {
        val sampleExpenses = listOf(
            Expense(
                amount = 250.0,
                category = "Food & Dining",
                date = Date(),
                description = "Lunch at restaurant"
            ),
            Expense(
                amount = 150.0,
                category = "Transportation",
                date = Date(System.currentTimeMillis() - 86400000), // Yesterday
                description = "Taxi fare"
            ),
            Expense(
                amount = 500.0,
                category = "Shopping",
                date = Date(System.currentTimeMillis() - 172800000), // 2 days ago
                description = "New shoes"
            ),
            Expense(
                amount = 1200.0,
                category = "Bills & Utilities",
                date = Date(),
                description = "Electricity bill"
            ),
            Expense(
                amount = 800.0,
                category = "Groceries",
                date = Date(),
                description = "Weekly groceries"
            )
        )
        insertExpenses(sampleExpenses)
    }
}