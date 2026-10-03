package com.example.hometask

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chores")
data class ChoreEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String,
    val description: String,
    val assignedTo: String,
    val dueDate: String,
    val priority: String,
    val completed: Boolean = false
)