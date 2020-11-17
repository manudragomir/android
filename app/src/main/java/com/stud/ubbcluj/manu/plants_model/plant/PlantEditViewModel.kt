package com.stud.ubbcluj.manu.plants_model.plant

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.stud.ubbcluj.manu.plants_model.model.Plant
import com.stud.ubbcluj.manu.plants_model.model.PlantRepository
import com.stud.ubbcluj.manu.plants_model.model.local.PlantDatabase
import com.stud.ubbcluj.manu.utils.MyResult
import com.stud.ubbcluj.manu.utils.TAG
import kotlinx.coroutines.launch

class PlantEditViewModel(application: Application): AndroidViewModel(application) {
    private val mutablePlant = MutableLiveData<Plant>().apply { value = Plant("", "", "", "") }
    private val mutableIsPlantFetching = MutableLiveData<Boolean>().apply{ value = false}
    private val mutableFetchingException = MutableLiveData<Exception>().apply{ value = null }
    private val mutableIsPlantDeleting = MutableLiveData<Boolean>().apply{ value = false}
    private val mutableDeletingException = MutableLiveData<Exception>().apply{ value = null}
    private val mutableJobDone = MutableLiveData<Boolean>().apply{ value = false }

    val plant: LiveData<Plant> = mutablePlant
    val isPlantFetching: LiveData<Boolean> = mutableIsPlantFetching
    val fetchingException: LiveData<Exception> = mutableFetchingException
    val isPlantDeleting: LiveData<Boolean> = mutableIsPlantDeleting
    val deletingException: LiveData<Exception> = mutableDeletingException
    val jobDone: LiveData<Boolean> = mutableJobDone

    val plantRepository: PlantRepository

    init {
        val itemDao = PlantDatabase.getDatabase(application, viewModelScope).plantDao()
        plantRepository = PlantRepository(itemDao)
    }

    fun loadPlant(plantId: String): LiveData<Plant>{
        Log.v(TAG, "get item by id")
        return plantRepository.getById(plantId)
    }

    fun saveOrUpdatePlant(plant: Plant){
        viewModelScope.launch {
            Log.v(TAG, "save or update plant...");
            mutableIsPlantFetching.value = true
            mutableFetchingException.value = null
            val result: MyResult<Plant>
            if (plant._id.isNotEmpty()) {
                result = plantRepository.update(plant)
            } else {
                result = plantRepository.save(plant)
            }
            when(result) {
                is MyResult.Success -> {
                    Log.d(TAG, "saveOrUpdateItem succeeded");
                }
                is MyResult.Error -> {
                    Log.w(TAG, "saveOrUpdateItem failed", result.exception);
                    mutableFetchingException.value = result.exception
                }
            }
            mutableJobDone.value = true
            mutableIsPlantFetching.value = false
        }
    }

    fun deletePlant(plantId: String){
        viewModelScope.launch {
            Log.i(TAG, "plant edit view model delete plant")

            mutableIsPlantDeleting.value = true
            mutableDeletingException.value = null

            try{
                val deletionResult = plantRepository.delete(plantId)
                Log.v(TAG, deletionResult.toString())
                mutableIsPlantDeleting.value = false
                mutableDeletingException.value = null
                mutableJobDone.value = true
            }catch(e: Exception){
                Log.i(TAG, e.printStackTrace().toString())
                mutableFetchingException.value = e
                mutableIsPlantFetching.value = false
            }
        }
    }
}