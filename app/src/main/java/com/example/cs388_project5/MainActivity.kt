package com.example.cs388_project5

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var database: AppDatabase
    private lateinit var adapter: CarMaintenanceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = CarMaintenanceAdapter { maintenance ->
            deleteMaintenanceEntry(maintenance)
        }
        recyclerView.adapter = adapter

        database = AppDatabase.getDatabase(this)

        val totalCostTextView = findViewById<TextView>(R.id.totalCostTextView)
        val totalMaintenancesTextView = findViewById<TextView>(R.id.totalMaintenancesTextView)
        val averageCostTextView = findViewById<TextView>(R.id.averageCostTextView)

        lifecycleScope.launch {
            database.carMaintenanceDao().getAllMaintenance().collect { records ->
                adapter.setMaintenanceList(records)
                val totalCost = records.sumByDouble { it.cost }
                val totalMaintenances = records.size
                val averageCost = if (totalMaintenances > 0) {
                    totalCost / totalMaintenances
                } else {
                    0.0
                }
                totalCostTextView.text = "Total Cost: $${"%.2f".format(totalCost)}"
                totalMaintenancesTextView.text = "Total Maintenances: $totalMaintenances"
                averageCostTextView.text = "Average Cost: $${"%.2f".format(averageCost)}"
            }
        }

        val buttonAdd = findViewById<Button>(R.id.buttonAddMaintenance)
        buttonAdd.setOnClickListener {
            showAddMaintenanceDialog()
        }

    }

    private fun showAddMaintenanceDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_maintenance, null)
        val editTextName = dialogView.findViewById<EditText>(R.id.editTextName)
        val editTextDescription = dialogView.findViewById<EditText>(R.id.editTextDescription)
        val editTextMiles = dialogView.findViewById<EditText>(R.id.editTextMiles)
        val editTextCost = dialogView.findViewById<EditText>(R.id.editTextCost)

        AlertDialog.Builder(this)
            .setTitle("Add Maintenance")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val name = editTextName.text.toString()
                val description = editTextDescription.text.toString()
                val miles = editTextMiles.text.toString().toIntOrNull() ?: 0
                val cost = editTextCost.text.toString().toDoubleOrNull() ?: 0.0
                addMaintenanceEntry(name, description, miles, cost)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun addMaintenanceEntry(name: String, description: String, miles: Int, cost: Double) {
        val newEntry = CarMaintenance(name = name, description = description, miles = miles, cost = cost)
        lifecycleScope.launch {
            database.carMaintenanceDao().insertMaintenance(newEntry)
        }
    }

    private fun deleteMaintenanceEntry(maintenance: CarMaintenance) {
        lifecycleScope.launch {
            database.carMaintenanceDao().deleteMaintenance(maintenance)
        }
    }
}