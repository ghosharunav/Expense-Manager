// ============================================================
// STEP 4: ROOM DATABASE
// ============================================================
// File: data/local/ExpenseDatabase.kt
package com.example.expensemanager.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.expensemanager.data.model.Expense

/**
 * Room Database class for Expense Manager
 *
 * @property entities List of entity classes to include in database
 * @property version Database version number (increment for migrations)
 * @property exportSchema Whether to export schema to JSON
 */
@Database(
    entities = [Expense::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ExpenseDatabase : RoomDatabase() {

    /**
     * Abstract method to get DAO instance
     * Room will provide implementation at compile time
     */
    abstract fun expenseDao(): ExpenseDao

    companion object {
        /**
         * Singleton instance of database
         * @Volatile ensures atomic updates across threads
         */
        @Volatile
        private var INSTANCE: ExpenseDatabase? = null

        /**
         * Get database instance (Singleton pattern)
         * Thread-safe implementation using synchronized block
         *
         * @param context Application context
         * @return ExpenseDatabase instance
         */
        fun getDatabase(context: Context): ExpenseDatabase {
            // Return existing instance if available
            return INSTANCE ?: synchronized(this) {
                // Double-check pattern - check again inside synchronized block
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ExpenseDatabase::class.java,
                    "expense_database"
                )
                    // Optional: Add callback for database creation/opening
                    // .addCallback(DatabaseCallback())

                    // Optional: Allow queries on main thread (NOT recommended for production)
                    // .allowMainThreadQueries()

                    // Optional: Enable in-memory database (for testing)
                    // Room.inMemoryDatabaseBuilder(context, ExpenseDatabase::class.java)

                    .build()

                INSTANCE = instance
                instance
            }
        }

        /**
         * Optional: Database callback for initialization
         * Uncomment and customize if needed
         */
        /*
        private class DatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // Insert default data when database is created
                CoroutineScope(Dispatchers.IO).launch {
                    INSTANCE?.let { database ->
                        // Add sample expenses for demo
                        val sampleExpenses = listOf(
                            Expense(
                                amount = 500.0,
                                category = "Food & Dining",
                                date = Date(),
                                description = "Lunch at restaurant"
                            ),
                            Expense(
                                amount = 200.0,
                                category = "Transportation",
                                date = Date(),
                                description = "Taxi fare"
                            )
                        )
                        database.expenseDao().insertExpenses(sampleExpenses)
                    }
                }
            }
        }
        */
    }
}

/**
 * USAGE EXAMPLE:
 *
 * // In your Activity/Fragment/Application class
 * val database = ExpenseDatabase.getDatabase(applicationContext)
 * val expenseDao = database.expenseDao()
 *
 * // Insert expense
 * lifecycleScope.launch {
 *     val expense = Expense(
 *         amount = 100.0,
 *         category = "Food",
 *         date = Date(),
 *         description = "Lunch"
 *     )
 *     expenseDao.insertExpense(expense)
 * }
 *
 * // Observe expenses
 * lifecycleScope.launch {
 *     expenseDao.getAllExpenses().collect { expenses ->
 *         // Update UI with expenses list
 *     }
 * }
 */