package com.example.example

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "buku_catatan")
data class BukuCatatanEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "judul")
    val judul: String,

    @ColumnInfo(name = "isi")
    val isi: String,

    @ColumnInfo(name = "kategori")
    val kategori: String,

    @ColumnInfo(name = "tanggal_dibuat")
    val tanggalDibuat: String
)