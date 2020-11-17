package com.stud.ubbcluj.manu.plants_model.plants

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.google.gson.Gson
import com.stud.ubbcluj.manu.plants_model.model.Plant
import com.stud.ubbcluj.manu.plants_model.model.PlantRepository
import com.stud.ubbcluj.manu.plants_model.model.local.PlantDatabase
import com.stud.ubbcluj.manu.plants_model.model.remote.SocketData
import com.stud.ubbcluj.manu.utils.MyResult
import com.stud.ubbcluj.manu.utils.TAG
import kotlinx.coroutines.launch


class PlantListViewModel(application: Application) : AndroidViewModel(application) {
    private val mutableLoading = MutableLiveData<Boolean>().apply { value = false }
    private val mutableException = MutableLiveData<Exception>().apply { value = null }

    val plants: LiveData<List<Plant>>
    val loading: LiveData<Boolean> = mutableLoading
    val loadingException: LiveData<Exception> = mutableException

    val plantRepository: PlantRepository

    init {
        val plantDao = PlantDatabase.getDatabase(application, viewModelScope).plantDao()
        plantRepository = PlantRepository(plantDao)
        plants = plantRepository.plants
    }

    fun loadItems() {
        viewModelScope.launch {
            Log.v(TAG, "refresh...");
            mutableLoading.value = true
            mutableException.value = null
            when (val result = plantRepository.refresh()) {
                is MyResult.Success -> {
                    Log.d(TAG, "refresh succeeded");
                }
                is MyResult.Error -> {
                    Log.w(TAG, "refresh failed", result.exception);
                    mutableException.value = result.exception
                }
            }
            mutableLoading.value = false
        }
    }
}