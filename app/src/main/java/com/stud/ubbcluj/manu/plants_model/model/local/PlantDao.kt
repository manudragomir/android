package com.stud.ubbcluj.manu.plants_model.model.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.stud.ubbcluj.manu.plants_model.model.Plant

@Dao
interface PlantDao {
    @Query("SELECT * from plants")
    fun getAll(): LiveData<List<Plant>>

    @Query("SELECT * FROM plants WHERE _id=:id ")
    fun getById(id: String): LiveData<Plant>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(plant: Plant)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(plant: Plant)

    @Query("DELETE FROM PLANTS WHERE _id=:id")
    suspend fun delete(id: String)

    @Query("DELETE FROM plants")
    suspend fun deleteAll()
}