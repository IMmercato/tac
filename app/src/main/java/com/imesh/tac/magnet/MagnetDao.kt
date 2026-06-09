package com.imesh.tac.magnet

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MagnetDao {
    @Query("SELECT * FROM magnets ORDER BY id")
    fun getAll(): Flow<List<MagnetEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: MagnetEntity)

    @Update
    suspend fun update(entity: MagnetEntity)

    @Query("UPDATE magnets SET posX = :x, posY = :y WHERE id = :id")
    suspend fun updatePosition(id: String, x: Float, y: Float)

    @Query("DELETE FROM magnets WHERE id = :id")
    suspend fun delete(id: String)
}