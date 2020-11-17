package com.stud.ubbcluj.manu.plants_model.plants

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.stud.ubbcluj.manu.plants_model.model.Plant
import com.stud.ubbcluj.manu.plants_model.model.PlantRepository
import com.stud.ubbcluj.manu.plants_model.model.remote.SocketData
import com.stud.ubbcluj.manu.utils.TAG
import kotlinx.coroutines.launch


class PlantListViewModel() : ViewModel() {
    private val mutablePlants = MutableLiveData<List<Plant>>().apply { value = emptyList() }
    private val mutableLoading = MutableLiveData<Boolean>().apply { value = false }
    private val mutableException = MutableLiveData<Exception>().apply { value = null }

    val plants: LiveData<List<Plant>> = mutablePlants
    val loading: LiveData<Boolean> = mutableLoading
    val loadingException: LiveData<Exception> = mutableException

    fun loadItems() {
        viewModelScope.launch {
            Log.v(TAG, "loading plants in view model")
            mutableLoading.value = true
            mutableException.value = null
            try {
                mutablePlants.value = PlantRepository.loadAll()
                Log.i(TAG, "all items loaded succesfully")
                mutableLoading.value = false
            }catch (e: Exception){
                Log.w(TAG, "load items failed", e)
                mutableLoading.value = false
                mutableException.value = e
            }
        }
    }

    fun newItemIncoming(message: String?){
        Log.v(TAG, "on message")
        viewModelScope.launch{
            if(message == null){
                return@launch
            }


            Log.v(TAG, message)
            val gson = Gson()
            val content = gson.fromJson(message, SocketData::class.java)

            val event = content.event
            val plant = content.payload.item

            Log.v(TAG, plant.toString())
            Log.v(TAG, event)

            PlantRepository.cachedItems?.toString()?.let { Log.v(TAG, "HELLO" + it) }

            val containsRepo = PlantRepository.cachedItems?.indexOfFirst { it.id == plant.id }
            Log.v(TAG, "HEI" + containsRepo.toString())

            when(event){
                "created" -> {
                    if(containsRepo == -1){
                        PlantRepository.cachedItems?.add(plant)
                    }
                }
                "deleted" -> {
                    if(containsRepo != -1){
                        PlantRepository.cachedItems?.remove(plant)
                    }
                }
                "updated" -> {
                    if(PlantRepository.cachedItems != null){
                        val index = PlantRepository.cachedItems?.indexOfFirst { it.id == plant.id }
                        if (index != null) {
                            PlantRepository.cachedItems!![index] = plant
                        }
                    }
                }
            }


            val list = mutableListOf<Plant>()
            list.addAll(PlantRepository.cachedItems!!)
            mutablePlants.value = list
        }
    }
}