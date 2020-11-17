package com.stud.ubbcluj.manu.plants_model.model

data class Plant(
    val id: String,
    var name: String,
    var description: String,
    var type: String
) {
    override fun toString(): String = "$id -> $name -> $description -> $type";
    override fun equals(other: Any?): Boolean {
        if(other != null && other.javaClass.simpleName != "Plant"){
            return false
        }
        val otherPlant = other as Plant
        return id == otherPlant.id
    }
}