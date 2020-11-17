package com.stud.ubbcluj.manu.plants_model.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "plants")
data class Plant(
    @PrimaryKey @ColumnInfo(name = "_id") val _id: String,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "type") var type: String
) {
    override fun toString(): String = "$_id -> $name -> $description -> $type";
    override fun equals(other: Any?): Boolean {
        if(other != null && other.javaClass.simpleName != "Plant"){
            return false
        }
        val otherPlant = other as Plant
        return _id == otherPlant._id
    }
}