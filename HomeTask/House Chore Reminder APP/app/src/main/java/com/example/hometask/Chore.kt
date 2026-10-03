package com.example.hometask

data class Chore (
    val id: Int,
    var name: String,
    var description: String,
    var assignedTo: String,
    var dueDate: String,
    var priority: String,
    var completed: Boolean = false,
)

