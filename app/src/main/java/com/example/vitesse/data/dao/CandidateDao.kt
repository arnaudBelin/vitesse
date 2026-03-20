package com.example.vitesse.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.vitesse.data.entity.Candidate
import kotlinx.coroutines.flow.Flow

@Dao
interface CandidateDao {
    @Query("SELECT * FROM Candidate WHERE deleted_at IS NULL ORDER BY created_at DESC")
    fun getAllCandidates(): Flow<List<Candidate>>

    @Query("SELECT * FROM Candidate WHERE id = :id AND deleted_at IS NULL")
    suspend fun getCandidateById(id: Int): Candidate

    @Upsert
    suspend fun addOrUpdateCandidate(candidate: Candidate)

    @Delete
    suspend fun deleteCandidate(candidate: Candidate)


}