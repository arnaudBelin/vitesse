package com.example.vitesse.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.vitesse.data.utils.Converters
import java.time.LocalDate
import java.time.Instant

@Entity
@TypeConverters(Converters::class)
data class Candidate(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "first_name")
    val firstName: String,
    @ColumnInfo(name = "last_name")
    val lastName: String,
    val phone: String,
    val email: String,
    @ColumnInfo(name = "picture_url")
    val pictureUrl: String? = null,
    @ColumnInfo(name = "birth_date")
    val birthDate: LocalDate,
    val salary: Double? = null, // in Euros
    val note: String? = null,
    @ColumnInfo(name = "is_favorite")
    val isFavorite: Boolean = false,
    @ColumnInfo(name = "created_at")
    val createdAt: Instant,
)