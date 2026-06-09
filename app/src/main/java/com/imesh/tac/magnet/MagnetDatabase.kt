package com.imesh.tac.magnet

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MagnetEntity::class], version = 1, exportSchema = false)
abstract class MagnetDatabase : RoomDatabase() {
    abstract fun magnetDao(): MagnetDao

    companion object {
        @Volatile private var INSTANCE: MagnetDatabase? = null

        fun getInstance(context: Context): MagnetDatabase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                MagnetDatabase::class.java,
                "tac_magnet.db"
            ).build().also { INSTANCE = it }
        }
    }
}