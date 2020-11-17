package com.stud.ubbcluj.manu.plants_model.model

import android.util.Log
import com.stud.ubbcluj.manu.plants_model.model.remote.PlantApi
import com.stud.ubbcluj.manu.utils.TAG

object PlantRepository {
    var cachedItems: MutableList<Plant>? = null;

    suspend fun loadAll(): List<Plant>{
        Log.i(TAG, "load all plants");
        if(cachedItems != null){
            return cachedItems as List<Plant>;
        }
        cachedItems = mutableListOf();
        val plants = PlantApi.service.find();
        cachedItems?.addAll(plants);
        return cachedItems as List<Plant>;
    }

    suspend fun load(plantId: String): Plant {
        Log.i(TAG, "load a plant")
        val item = cachedItems?.find { it.id == plantId }
        if (item != null) {
            return item
        }
        return PlantApi.service.read(plantId)
    }

    suspend fun save(item: Plant): Plant {
        Log.i(TAG, "save a plant")
        val createdPlant = PlantApi.service.create(item)
        cachedItems?.add(createdPlant)
        return createdPlant
    }

    suspend fun update(item: Plant): Plant {
        Log.i(TAG, "update a plant")
        val updatedItem = PlantApi.service.update(item.id, item)
        val index = cachedItems?.indexOfFirst { it.id == item.id }
        if (index != null) {
            cachedItems?.set(index, updatedItem)
        }
        return updatedItem
    }

    suspend fun delete(idPlant: String): Boolean? {
        Log.i(TAG, "trying to delete a plant")
        val deletedPlant = PlantApi.service.delete(idPlant)
        cachedItems?.remove(deletedPlant)
        return true
    }
}