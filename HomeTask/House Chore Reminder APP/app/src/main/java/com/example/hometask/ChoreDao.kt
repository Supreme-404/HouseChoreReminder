package com.example.hometask

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ChoreDao {

    @Insert
    suspend fun insertChore(chore: ChoreEntity): Long

    @Query("SELECT * FROM chores ORDER BY id ASC")
    suspend fun getAllChores(): List<ChoreEntity>

    @Update
    suspend fun updateChore(chore: ChoreEntity)

    @Delete
    suspend fun deleteChore(chore: ChoreEntity)

    @Query("UPDATE chores SET completed = 1 WHERE id = :id")
    suspend fun completeChore(id: Int)
}