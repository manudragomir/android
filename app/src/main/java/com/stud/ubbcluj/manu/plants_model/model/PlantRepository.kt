package com.stud.ubbcluj.manu.plants_model.model

import android.util.Log
import androidx.lifecycle.LiveData
import com.stud.ubbcluj.manu.plants_model.model.local.PlantDao
import com.stud.ubbcluj.manu.plants_model.model.remote.PlantApi
import com.stud.ubbcluj.manu.utils.MyResult
import com.stud.ubbcluj.manu.utils.TAG

class PlantRepository(private val plantDao: PlantDao) {

    val plants = plantDao.getAll();

    suspend fun refresh(): MyResult<Boolean> {
        return try {
            Log.i(TAG, "trying to refresh plants")
            val plants = PlantApi.service.find()
            for (plant in plants) {
                plantDao.insert(plant)
            }
            MyResult.Success(true)
        } catch(e: Exception) {
            MyResult.Error(e)
        }
    }

    fun getById(plantId: String): LiveData<Plant> {
        Log.i(TAG, "trying to get a plant")
        return plantDao.getById(plantId)
    }

    suspend fun save(plant: Plant): MyResult<Plant> {
        return try {
            Log.i(TAG, "trying to save a plant in repo")
            Log.i(TAG, plant.toString())
            val createdPlant = PlantApi.service.create(plant)
            Log.v(TAG, "REPO RETURN $createdPlant")
            plantDao.insert(createdPlant)
            MyResult.Success(createdPlant)
        } catch(e: Exception) {
            MyResult.Error(e)
        }
    }

    suspend fun update(plant: Plant): MyResult<Plant> {
        return try {
            Log.i(TAG, "trying to update a plant")
            val updatedPlant = PlantApi.service.update(plant._id, plant)
            plantDao.update(updatedPlant)
            MyResult.Success(updatedPlant)
        } catch(e: Exception) {
            MyResult.Error(e)
        }
    }

    suspend fun delete(idPlant: String): MyResult<Plant> {
        return try {
            Log.i(TAG, "trying to delete a plant")
            val deletedPlant = PlantApi.service.delete(idPlant)
            plantDao.delete(idPlant)
            MyResult.Success(deletedPlant)
        } catch(e: Exception) {
            MyResult.Error(e)
        }
    }
}