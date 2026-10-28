package com.example.example

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [BukuCatatanEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun bukuCatatanDao(): BukuCatatanDao

    companion object {
        // @Volatile: nilai INSTANCE selalu terbaru dan sama untuk semua thread
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Singleton: hanya ada satu instance database di seluruh aplikasi
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "catatan _db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}