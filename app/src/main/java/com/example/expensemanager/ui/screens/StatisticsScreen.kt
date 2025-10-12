// ============================================================
// STEP 12: STATISTICS SCREEN
// ============================================================
// File: ui/screens/StatisticsScreen.kt
package com.example.expensemanager.ui.screens

import android.graphics.Color
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.expensemanager.ui.utils.formatCurrency
import com.example.expensemanager.ui.utils.formatPercentage
import com.example.expensemanager.ui.viewmodel.ExpenseViewModel
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate

/**
 * Statistics Screen - Charts and analytics
 *
 * Features:
 * - Pie chart for category distribution
 * - Bar chart for category comparison
 * - Detailed category breakdown list
 * - Empty state handling
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    navController: NavController,
    viewModel: ExpenseViewModel
) {
    val categoryTotals by viewModel.categoryTotals.collectAsState()
    val expenses by viewModel.expenses.collectAsState()
    val grandTotal by viewModel.grandTotal.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Statistics") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { padding ->
        if (categoryTotals.isEmpty()) {
            // Empty State
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.BarChart,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.outline
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No data to display",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Add some expenses to see statistics",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
            ) {
                // Total Summary Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Total Expenses",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = formatCurrency(grandTotal),
                            style = MaterialTheme.typography.displaySmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${expenses.size} transactions",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }

                // Pie Chart Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Spending by Category",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Distribution of expenses across categories",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        AndroidView(
                            factory = { context ->
                                PieChart(context).apply {
                                    description.isEnabled = false
                                    setUsePercentValues(true)
                                    setDrawEntryLabels(true)
                                    setEntryLabelColor(Color.BLACK)
                                    setEntryLabelTextSize(12f)
                                    legend.apply {
                                        isEnabled = true
                                        textSize = 12f
                                        formSize = 12f
                                        verticalAlignment = com.github.mikephil.charting.components.Legend.LegendVerticalAlignment.BOTTOM
                                        horizontalAlignment = com.github.mikephil.charting.components.Legend.LegendHorizontalAlignment.CENTER
                                        orientation = com.github.mikephil.charting.components.Legend.LegendOrientation.HORIZONTAL
                                        setDrawInside(false)
                                    }
                                    setDrawHoleEnabled(true)
                                    holeRadius = 40f
                                    transparentCircleRadius = 45f
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp),
                            update = { chart ->
                                val entries = categoryTotals.map { (category, total) ->
                                    PieEntry(total.toFloat(), category)
                                }

                                val dataSet = PieDataSet(entries, "").apply {
                                    colors = ColorTemplate.MATERIAL_COLORS.toList()
                                    valueTextSize = 14f
                                    valueTextColor = Color.WHITE
                                    sliceSpace = 3f
                                    selectionShift = 8f
                                }

                                val data = PieData(dataSet).apply {
                                    setValueFormatter(object : ValueFormatter() {
                                        override fun getFormattedValue(value: Float): String {
                                            return "${value.toInt()}%"
                                        }
                                    })
                                }

                                chart.data = data
                                chart.animateY(1000)
                                chart.invalidate()
                            }
                        )
                    }
                }

                // Bar Chart Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Category Comparison",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Amount spent per category",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        AndroidView(
                            factory = { context ->
                                BarChart(context).apply {
                                    description.isEnabled = false
                                    legend.isEnabled = false
                                    setDrawGridBackground(false)
                                    setDrawBarShadow(false)
                                    setDrawValueAboveBar(true)
                                    setPinchZoom(false)
                                    setFitBars(true)

                                    xAxis.apply {
                                        setDrawGridLines(false)
                                        position = com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM
                                        granularity = 1f
                                        textSize = 10f
                                        labelRotationAngle = -45f
                                    }

                                    axisLeft.apply {
                                        setDrawGridLines(true)
                                        gridColor = Color.LTGRAY
                                        axisMinimum = 0f
                                    }

                                    axisRight.isEnabled = false
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp),
                            update = { chart ->
                                val entries = categoryTotals.entries
                                    .sortedByDescending { it.value }
                                    .mapIndexed { index, entry ->
                                        BarEntry(index.toFloat(), entry.value.toFloat())
                                    }

                                val dataSet = BarDataSet(entries, "Amount").apply {
                                    colors = ColorTemplate.MATERIAL_COLORS.toList()
                                    valueTextSize = 10f
                                    valueTextColor = Color.BLACK
                                }

                                chart.xAxis.valueFormatter = object : ValueFormatter() {
                                    override fun getFormattedValue(value: Float): String {
                                        return categoryTotals.keys
                                            .sortedByDescending { categoryTotals[it] }
                                            .getOrNull(value.toInt()) ?: ""
                                    }
                                }

                                chart.data = BarData(dataSet)
                                chart.animateY(1000)
                                chart.invalidate()
                            }
                        )
                    }
                }

                // Detailed Breakdown Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .padding(bottom = 16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Detailed Breakdown",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        categoryTotals.entries
                            .sortedByDescending { it.value }
                            .forEachIndexed { index, (category, total) ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = category,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Medium
                                        )
                                        Text(
                                            text = formatPercentage(total, grandTotal),
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    Text(
                                        text = formatCurrency(total),
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }

                                if (index < categoryTotals.size - 1) {
                                    HorizontalDivider()
                                }
                            }
                    }
                }
            }
        }
    }
}