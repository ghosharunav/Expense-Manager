// ============================================================
// STEP 14: MAIN ACTIVITY
// ============================================================
// File: MainActivity.kt (in main package: com.example.expensemanager)
package com.example.expensemanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.expensemanager.data.local.ExpenseDatabase
import com.example.expensemanager.data.repository.ExpenseRepository
import com.example.expensemanager.ui.navigation.ExpenseNavigation
import com.example.expensemanager.ui.theme.ExpenseManagerTheme
import com.example.expensemanager.ui.viewmodel.ExpenseViewModel
import com.example.expensemanager.ui.viewmodel.ExpenseViewModelFactory

/**
 * Main Activity - Entry point of the app
 *
 * Responsibilities:
 * - Initialize database and repository
 * - Create ViewModel factory
 * - Set up Compose UI with navigation
 * - Apply app theme
 *
 * Architecture Flow:
 * MainActivity → Database → Repository → ViewModel → UI Screens
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge display for modern immersive UI
        enableEdgeToEdge()

        // Initialize Room Database (Singleton pattern)
        // This creates the database file on first run
        val database = ExpenseDatabase.getDatabase(applicationContext)

        // Initialize Repository with DAO
        // Repository abstracts data operations from ViewModel
        val repository = ExpenseRepository(database.expenseDao())

        // Create ViewModel Factory
        // Factory is needed because ViewModel requires repository parameter
        val viewModelFactory = ExpenseViewModelFactory(repository)

        // Set up Compose UI
        setContent {
            // Apply Material Design 3 theme
            ExpenseManagerTheme {
                // Surface container with background color
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Create navigation controller
                    // This manages navigation between screens
                    val navController = rememberNavController()

                    // Get ViewModel instance using factory
                    // ViewModel is shared across all screens
                    val viewModel: ExpenseViewModel = viewModel(factory = viewModelFactory)

                    // Set up navigation graph with all screens
                    ExpenseNavigation(
                        navController = navController,
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

/**
 * ═══════════════════════════════════════════════════════════
 * IMPORTANT NOTES & ARCHITECTURE EXPLANATION
 * ═══════════════════════════════════════════════════════════
 *
 * 1. DATABASE INITIALIZATION:
 *    ─────────────────────────
 *    - ExpenseDatabase.getDatabase() returns singleton instance
 *    - Database is created only ONCE per app lifecycle
 *    - Room handles all SQLite operations automatically
 *    - Database file location: /data/data/com.example.expensemanager/databases/
 *
 * 2. REPOSITORY PATTERN:
 *    ──────────────────
 *    - ExpenseRepository abstracts data operations
 *    - ViewModel doesn't directly access DAO
 *    - Makes code more testable and maintainable
 *    - Can easily add remote data source later (e.g., Firebase, API)
 *
 * 3. VIEWMODEL FACTORY:
 *    ──────────────────
 *    - Required because ViewModel needs repository parameter
 *    - Factory creates ViewModel with dependencies
 *    - ViewModelProvider uses factory to instantiate ViewModel
 *    - Without factory, we can't pass parameters to ViewModel constructor
 *
 * 4. COMPOSE SETUP:
 *    ─────────────
 *    - ExpenseManagerTheme applies Material Design 3
 *    - Surface provides background container
 *    - NavController manages screen navigation
 *    - ViewModel is shared across all screens (single source of truth)
 *
 * 5. EDGE-TO-EDGE:
 *    ────────────
 *    - enableEdgeToEdge() allows content under system bars
 *    - Creates more immersive UI experience
 *    - Properly handles insets for modern Android
 *    - Status bar and navigation bar become transparent
 *
 * 6. LIFECYCLE:
 *    ─────────
 *    App Launch → onCreate() called
 *                ↓
 *    Database initialized (if not exists)
 *                ↓
 *    Repository created
 *                ↓
 *    ViewModel created with repository
 *                ↓
 *    UI renders with Compose
 *                ↓
 *    Navigation to Home screen
 *
 * 7. DATA FLOW:
 *    ─────────
 *    User Action (e.g., "Add Expense")
 *         ↓
 *    UI Screen calls ViewModel method
 *         ↓
 *    ViewModel calls Repository method
 *         ↓
 *    Repository calls DAO method
 *         ↓
 *    DAO executes SQL query
 *         ↓
 *    Room updates database
 *         ↓
 *    DAO emits Flow update
 *         ↓
 *    Repository forwards Flow
 *         ↓
 *    ViewModel processes data
 *         ↓
 *    StateFlow updates UI
 *         ↓
 *    Compose recomposes screen
 *
 * 8. MEMORY MANAGEMENT:
 *    ─────────────────
 *    - Database: Singleton (one instance)
 *    - Repository: One instance per MainActivity
 *    - ViewModel: Survives configuration changes (screen rotation)
 *    - NavController: Recreated on configuration change
 *    - Compose UI: Recomposed when state changes
 *
 * 9. THREAD SAFETY:
 *    ─────────────
 *    - All database operations run on background threads
 *    - Coroutines handle async operations
 *    - Flow ensures thread-safe data streams
 *    - UI updates always on Main thread
 *
 * 10. TESTING CONSIDERATIONS:
 *     ─────────────────────
 *     - Repository can be mocked for ViewModel tests
 *     - Database can be replaced with in-memory version
 *     - ViewModel logic can be tested independently
 *     - UI screens can be tested with preview or instrumented tests
 *
 * ═══════════════════════════════════════════════════════════
 * USAGE EXAMPLE (Already implemented above):
 * ═══════════════════════════════════════════════════════════
 *
 * class MainActivity : ComponentActivity() {
 *     override fun onCreate(savedInstanceState: Bundle?) {
 *         super.onCreate(savedInstanceState)
 *         enableEdgeToEdge()
 *
 *         // Setup
 *         val database = ExpenseDatabase.getDatabase(applicationContext)
 *         val repository = ExpenseRepository(database.expenseDao())
 *         val factory = ExpenseViewModelFactory(repository)
 *
 *         // Compose UI
 *         setContent {
 *             ExpenseManagerTheme {
 *                 Surface {
 *                     val navController = rememberNavController()
 *                     val viewModel: ExpenseViewModel = viewModel(factory = factory)
 *                     ExpenseNavigation(navController, viewModel)
 *                 }
 *             }
 *         }
 *     }
 * }
 *
 * ═══════════════════════════════════════════════════════════
 */