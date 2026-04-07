package com.example.vitesse.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vitesse.data.entity.Candidate
import com.example.vitesse.data.repository.CandidateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val candidateRepository: CandidateRepository
) : ViewModel() {
    private val _candidatesState = MutableStateFlow<List<Candidate>>(emptyList())
    val candidatesState: StateFlow<List<Candidate>> = _candidatesState

    private val _searchResultState = MutableStateFlow<List<Candidate>>(emptyList())
    val searchResultState : StateFlow<List<Candidate>> = _searchResultState

    private val _candidateState = MutableStateFlow<Candidate?>(null)
    val candidateState: StateFlow<Candidate?> = _candidateState

    fun fetchCandidates() {
        viewModelScope.launch(Dispatchers.IO) {
            candidateRepository.getAllCandidates().collect { candidates ->
                _candidatesState.value = candidates
            }
        }
    }

    fun searchCandidates(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (query.isBlank()) {
                _searchResultState.value = emptyList()
                return@launch
            }
            val results = candidateRepository.getCandidatesBySearch(query)
            _searchResultState.value = results
        }
    }

    fun getCandidateById(candidateId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = candidateRepository.getCandidateById(candidateId)
            _candidateState.value = result
        }
    }

    fun addOrUpdateCandidate(candidate: Candidate) {
        viewModelScope.launch(Dispatchers.IO) {
            candidateRepository.addOrUpdateCandidate(candidate)
        }
    }

    fun deleteCandidate(candidate: Candidate) {
        viewModelScope.launch(Dispatchers.IO) {
            candidateRepository.deleteCandidate(candidate)
        }
    }

    init {
        fetchCandidates()
    }



}
