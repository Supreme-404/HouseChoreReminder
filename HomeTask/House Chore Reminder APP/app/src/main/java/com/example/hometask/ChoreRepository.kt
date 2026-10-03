package com.example.hometask

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChoreRepository private constructor(context: Context) {

    private val choreDao = AppDatabase
        .getDatabase(context)
        .choreDao()

    companion object {

        @Volatile
        private var INSTANCE: ChoreRepository? = null

        fun getInstance(context: Context): ChoreRepository {

            return INSTANCE ?: synchronized(this) {

                val instance = ChoreRepository(context)

                INSTANCE = instance

                instance
            }
        }
    }

    suspend fun addChore(
        name: String,
        description: String,
        assignedTo: String,
        dueDate: String,
        priority: String
    ): Long {

        val chore = ChoreEntity(
            name = name,
            description = description,
            assignedTo = assignedTo,
            dueDate = dueDate,
            priority = priority
        )

        return choreDao.insertChore(chore)
    }

    suspend fun getChores(): List<ChoreEntity> {
        return choreDao.getAllChores()
    }

    fun updateChore(chore: ChoreEntity) {

        CoroutineScope(Dispatchers.IO).launch {

            choreDao.updateChore(chore)
        }
    }

    fun deleteChore(chore: ChoreEntity) {

        CoroutineScope(Dispatchers.IO).launch {

            choreDao.deleteChore(chore)
        }
    }

    fun completeChore(id: Int) {

        CoroutineScope(Dispatchers.IO).launch {

            choreDao.completeChore(id)
        }
    }
}