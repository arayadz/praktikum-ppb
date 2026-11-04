package com.example.example

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface BukuCatatanDao {

    // CREATE
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCatatan(catatan: BukuCatatanEntity)

    // READ
    @Query("SELECT * FROM buku_catatan ORDER BY id DESC")
    suspend fun getAllCatatan(): List<BukuCatatanEntity>

    // UPDATE: Room mencocokkan baris berdasarkan Primary Key (id) objek
    @Update
    suspend fun updateCatatan(catatan: BukuCatatanEntity)

    // DELETE (dengan objek): juga dicocokkan lewat Primary Key
    @Delete
    suspend fun deleteCatatan(catatan: BukuCatatanEntity)

    // DELETE (dengan id): query khusus
    @Query("DELETE FROM buku_catatan WHERE id = :id")
    suspend fun deleteById(id: Int)
}