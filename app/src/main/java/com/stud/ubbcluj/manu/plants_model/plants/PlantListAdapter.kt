package com.stud.ubbcluj.manu.plants_model.plants

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.stud.ubbcluj.manu.R
import com.stud.ubbcluj.manu.plants_model.model.Plant
import com.stud.ubbcluj.manu.plants_model.plant.PlantEditFragment
import com.stud.ubbcluj.manu.utils.TAG
import kotlinx.android.synthetic.main.view_plant.view.*

class PlantListAdapter(
    private val fragment: Fragment
) : RecyclerView.Adapter<PlantListAdapter.ViewHolder>() {

    var plants = emptyList<Plant>()
        set(value){
            field = value
            notifyDataSetChanged()
        }

    private var onPlantClickEvent: View.OnClickListener

    init {
        onPlantClickEvent = View.OnClickListener { view ->
            val plant = view.tag as Plant
            fragment.findNavController().navigate(R.id.PlantEditFragment,
                Bundle().apply{
                    putString(PlantEditFragment.PLANT_ID, plant.id)
                }
            )
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameOfThePlantView: TextView = view.in_list_plant_name
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.v(TAG, "on create view holder")
        var view = LayoutInflater.from(parent.context)
                .inflate(R.layout.view_plant, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.v(TAG, "bind view holder")
        holder.nameOfThePlantView.text = plants[position].name
        holder.itemView.tag = plants[position]
        holder.itemView.setOnClickListener(onPlantClickEvent)
    }

    override fun getItemCount(): Int {
        return plants.size
    }


}
