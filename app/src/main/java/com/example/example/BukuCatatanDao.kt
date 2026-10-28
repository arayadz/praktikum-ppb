package com.example.example

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BukuCatatanDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCatatan(catatan: BukuCatatanEntity)

    @Query("SELECT * FROM buku_catatan ORDER BY id DESC")
    suspend fun getAllCatatan(): List<BukuCatatanEntity>
}