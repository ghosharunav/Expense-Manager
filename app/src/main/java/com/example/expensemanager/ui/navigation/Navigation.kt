// ============================================================
// STEP 8: NAVIGATION SETUP
// ============================================================
// File: ui/navigation/Navigation.kt
package com.example.expensemanager.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.expensemanager.ui.screens.*
import com.example.expensemanager.ui.viewmodel.ExpenseViewModel

/**
 * Sealed class defining all navigation destinations
 * Type-safe navigation with compile-time route checking
 */
sealed class Screen(val route: String) {
    /**
     * Home screen - main expense list
     */
    object Home : Screen("home")

    /**
     * Add new expense screen
     */
    object AddExpense : Screen("add_expense")

    /**
     * Edit existing expense screen
     * Requires expenseId parameter
     */
    object EditExpense : Screen("edit_expense/{expenseId}") {
        /**
         * Create route with expense ID
         *
         * @param expenseId ID of expense to edit
         * @return Complete route string
         */
        fun createRoute(expenseId: Long) = "edit_expense/$expenseId"
    }

    /**
     * Statistics and charts screen
     */
    object Statistics : Screen("statistics")

    /**
     * Settings screen
     */
    object Settings : Screen("settings")
}

/**
 * Main navigation graph for the app
 * Defines all screens and their connections
 *
 * @param navController Navigation controller for navigating between screens
 * @param viewModel Shared ViewModel across all screens
 */
@Composable
fun ExpenseNavigation(
    navController: NavHostController,
    viewModel: ExpenseViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        // Home Screen - Main expense list
        composable(route = Screen.Home.route) {
            HomeScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        // Add Expense Screen - Create new expense
        composable(route = Screen.AddExpense.route) {
            AddExpenseScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        // Edit Expense Screen - Modify existing expense
        composable(
            route = Screen.EditExpense.route,
            arguments = listOf(
                navArgument("expenseId") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
            val expenseId = backStackEntry.arguments?.getLong("expenseId") ?: return@composable
            EditExpenseScreen(
                navController = navController,
                viewModel = viewModel,
                expenseId = expenseId
            )
        }

        // Statistics Screen - Charts and analytics
        composable(route = Screen.Statistics.route) {
            StatisticsScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        // Settings Screen - App settings and info
        composable(route = Screen.Settings.route) {
            SettingsScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}

/**
 * Extension functions for easier navigation
 */

/**
 * Navigate to Home screen
 * Clears back stack to make Home the root
 */
fun NavHostController.navigateToHome() {
    navigate(Screen.Home.route) {
        popUpTo(Screen.Home.route) { inclusive = true }
    }
}

/**
 * Navigate to Add Expense screen
 */
fun NavHostController.navigateToAddExpense() {
    navigate(Screen.AddExpense.route)
}

/**
 * Navigate to Edit Expense screen
 *
 * @param expenseId ID of expense to edit
 */
fun NavHostController.navigateToEditExpense(expenseId: Long) {
    navigate(Screen.EditExpense.createRoute(expenseId))
}

/**
 * Navigate to Statistics screen
 */
fun NavHostController.navigateToStatistics() {
    navigate(Screen.Statistics.route)
}

/**
 * Navigate to Settings screen
 */
fun NavHostController.navigateToSettings() {
    navigate(Screen.Settings.route)
}

/**
 * Navigate back to previous screen
 * Returns true if navigation was successful
 */
fun NavHostController.navigateBack(): Boolean {
    return navigateUp()
}

/**
 * USAGE EXAMPLE:
 *
 * // In your composable
 * val navController = rememberNavController()
 *
 * // Navigate to add expense
 * Button(onClick = { navController.navigateToAddExpense() }) {
 *     Text("Add Expense")
 * }
 *
 * // Navigate to edit expense
 * Button(onClick = { navController.navigateToEditExpense(expenseId = 123) }) {
 *     Text("Edit")
 * }
 *
 * // Navigate back
 * IconButton(onClick = { navController.navigateBack() }) {
 *     Icon(Icons.Default.ArrowBack, "Back")
 * }
 *
 * // In MainActivity
 * setContent {
 *     val navController = rememberNavController()
 *     val viewModel: ExpenseViewModel = viewModel(factory = viewModelFactory)
 *
 *     ExpenseNavigation(
 *         navController = navController,
 *         viewModel = viewModel
 *     )
 * }
 */
