package com.stud.ubbcluj.manu.plants_model.plant

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.stud.ubbcluj.manu.R
import com.stud.ubbcluj.manu.plants_model.model.Plant
import com.stud.ubbcluj.manu.utils.TAG
import kotlinx.android.synthetic.main.plant_edit.*


class PlantEditFragment : Fragment() {
    companion object {
        const val PLANT_ID = "PLANT_ID"
    }

    private lateinit var plantEditViewModel: PlantEditViewModel
    private var plantId: String? = null
    private var plant: Plant? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "on creat")
        arguments?.let{
            if(it.containsKey(PLANT_ID)){
                plantId = it.getString(PLANT_ID).toString()
            }
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View?{
        Log.v(TAG, "on create view")
        return inflater.inflate(R.layout.plant_edit, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.v(TAG, "on activity created")
        setupViewModel()
        btn_save.setOnClickListener{
            Log.v(TAG, "save or update a plant")
            val auxPlant = plant
            if(auxPlant != null) {
                if(auxPlant._id == null){
                    Log.v(TAG, "E null");
                }
                if(auxPlant._id == ""){
                    Log.v(TAG, auxPlant._id)
                }
                auxPlant.name = nameView.text.toString();
                auxPlant.description = descriptionView.text.toString()
                auxPlant.type = typeView.text.toString()
                plantEditViewModel.saveOrUpdatePlant(auxPlant)
            }
        }

        btn_delete.setOnClickListener{
            Log.v(TAG, "delete a plant")
            if(plantId != null){
                plantEditViewModel.deletePlant(plantId!!)
            }
        }

        btn_back.setOnClickListener{
            Log.v(TAG, "back to main page")
            findNavController().navigateUp()
        }
    }

    private fun setupViewModel() {
        plantEditViewModel = ViewModelProvider(this).get(PlantEditViewModel::class.java)

        plantEditViewModel.plant.observe(viewLifecycleOwner, { plant ->
            Log.v(TAG, "set plant on window")
            nameView.setText(plant.name)
            descriptionView.setText(plant.description)
            typeView.setText(plant.type)
        })

        plantEditViewModel.isPlantFetching.observe(viewLifecycleOwner, { fetchingStatus ->
            Log.v(TAG, "changing loading plant status")
            progress_edit.visibility = if (fetchingStatus) View.VISIBLE else View.GONE
        })

        plantEditViewModel.fetchingException.observe(viewLifecycleOwner, { exception ->
            if(exception != null){
                val message = exception.message
                Log.e(TAG, "throwing exception when fetching item", exception)
                val parentActivity = activity?.parent
                if (parentActivity != null) {
                    Toast.makeText(parentActivity, message, Toast.LENGTH_LONG).show()
                }
            }
        })

        plantEditViewModel.isPlantDeleting.observe(viewLifecycleOwner, { deletingStatus ->
            Log.v(TAG, "deleting plant changed status")
            progress_edit.visibility = if (deletingStatus) View.VISIBLE else View.GONE
        })

        plantEditViewModel.deletingException.observe(viewLifecycleOwner, { exception ->
            if(exception != null){
                val message = exception.message
                Log.e(TAG, "throwing exception when deleting item", exception)
                val parentActivity = activity?.parent
                if (parentActivity != null) {
                    Toast.makeText(parentActivity, message, Toast.LENGTH_LONG).show()
                }
            }
        })

        plantEditViewModel.jobDone.observe(viewLifecycleOwner, { jobDoneStatus ->
            if(jobDoneStatus){
                findNavController().navigateUp()
            }
        })

        val id = plantId
        if(id == null){
            plant = Plant("", "", "", "")
        }
        else{
            plantEditViewModel.loadPlant(id).observe(viewLifecycleOwner, {
                Log.v(TAG, "update items")
                if (it != null) {
                    plant = it
                    nameView.setText(it.name)
                    descriptionView.setText(it.description)
                    typeView.setText(it.type)
                }
            })
        }

        plantId?.let { plantEditViewModel.loadPlant(it) }
    }

}