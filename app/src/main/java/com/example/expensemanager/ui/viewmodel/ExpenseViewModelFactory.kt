// ============================================================
// FILE: ui/viewmodel/ExpenseViewModelFactory.kt
// ============================================================
package com.example.expensemanager.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.expensemanager.data.repository.ExpenseRepository

/**
 * Factory class for creating ExpenseViewModel instances.
 * Required because ViewModel needs a repository parameter.
 *
 * @property repository Repository instance to pass to ViewModel
 */
class ExpenseViewModelFactory(
    private val repository: ExpenseRepository
) : ViewModelProvider.Factory {

    /**
     * Create ViewModel instance
     *
     * @param modelClass Class of ViewModel to create
     * @return ViewModel instance
     * @throws IllegalArgumentException if modelClass is not ExpenseViewModel
     */
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExpenseViewModel::class.java)) {
            return ExpenseViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}

/**
 * USAGE IN ACTIVITY/FRAGMENT:
 *
 * // In MainActivity.kt
 * val database = ExpenseDatabase.getDatabase(applicationContext)
 * val repository = ExpenseRepository(database.expenseDao())
 * val viewModelFactory = ExpenseViewModelFactory(repository)
 *
 * setContent {
 *     val viewModel: ExpenseViewModel = viewModel(factory = viewModelFactory)
 *     // Use viewModel in composables
 * }
 */