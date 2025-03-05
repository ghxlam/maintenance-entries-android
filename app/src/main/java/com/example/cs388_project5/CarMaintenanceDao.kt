package com.example.cs388_project5

import androidx.room.*

@Dao
interface CarMaintenanceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMaintenance(maintenance: CarMaintenance)

    @Update
    suspend fun updateMaintenance(maintenance: CarMaintenance)

    @Delete
    suspend fun deleteMaintenance(maintenance: CarMaintenance)

    @Query("SELECT * FROM car_maintenance ORDER BY miles DESC")
    fun getAllMaintenance(): kotlinx.coroutines.flow.Flow<List<CarMaintenance>>
}
