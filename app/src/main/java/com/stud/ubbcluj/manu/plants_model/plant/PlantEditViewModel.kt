package com.stud.ubbcluj.manu.plants_model.plant

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stud.ubbcluj.manu.plants_model.model.Plant
import com.stud.ubbcluj.manu.plants_model.model.PlantRepository
import com.stud.ubbcluj.manu.utils.TAG
import kotlinx.coroutines.launch

class PlantEditViewModel: ViewModel() {
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

    fun loadPlant(plantId: String){
        viewModelScope.launch {
            Log.i(TAG,"plant is loading")
            mutableIsPlantFetching.value = true
            mutableFetchingException.value = null
            try{
                mutablePlant.value = PlantRepository.load(plantId)
                Log.i(TAG, "plant edit view model load plant")
                mutableIsPlantFetching.value = false
            } catch(e: Exception){
                Log.w(TAG, "plant edit view model exception loading plant", e)
                mutableFetchingException.value = e
                mutableIsPlantFetching.value = false
            }
        }
    }

    fun saveOrUpdatePlant(name: String, description: String, type: String){
        viewModelScope.launch{
            Log.i(TAG, "plant edit view model save or update plant")
            val currentPlant = mutablePlant.value ?: return@launch
            currentPlant.name = name
            currentPlant.description = description
            currentPlant.type = type

            mutableIsPlantFetching.value = true
            mutableFetchingException.value = null

            try{
                if(currentPlant.id.isEmpty()){
                    mutablePlant.value = PlantRepository.save(currentPlant)
                } else{
                    mutablePlant.value = PlantRepository.update(currentPlant)
                }
                mutableIsPlantFetching.value = false
                mutableJobDone.value = true
            } catch(e: Exception){
                Log.e(TAG, "error when saving/updating item", e)
                mutableFetchingException.value = e
                mutableIsPlantFetching.value = false
            }
        }
    }

    fun deletePlant(plantId: String){
        viewModelScope.launch {
            Log.i(TAG, "plant edit view model delete plant")

            mutableIsPlantDeleting.value = true
            mutableDeletingException.value = null

            try{
                val deletionResult = PlantRepository.delete(plantId)
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