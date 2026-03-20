package com.example.vitesse.data.repository

import com.example.vitesse.data.dao.CandidateDao
import com.example.vitesse.data.entity.Candidate
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CandidateRepository @Inject constructor(private val candidateDao: CandidateDao) {

    suspend fun getCandidateById(id: Int): Candidate? =
        candidateDao.getCandidateById(id)

    fun getAllCandidates(): Flow<List<Candidate>> =
        candidateDao.getAllCandidates()

    suspend fun addOrUpdateCandidate(candidate: Candidate) {
        candidateDao.addOrUpdateCandidate(candidate)
    }
    suspend fun deleteCandidate(candidate: Candidate) {
        candidateDao.deleteCandidate(candidate)
    }
}
