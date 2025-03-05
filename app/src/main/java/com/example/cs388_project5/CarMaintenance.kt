package com.example.cs388_project5

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "car_maintenance")
data class CarMaintenance(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val description: String,
    val miles: Int,
    val cost: Double
)
