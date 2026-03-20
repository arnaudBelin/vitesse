package com.example.vitesse.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vitesse.data.entity.Candidate
import com.example.vitesse.data.repository.CandidateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val candidateRepository: CandidateRepository
) : ViewModel() {
    private val _candidateState = MutableStateFlow<List<Candidate>>(emptyList())
    val candidateState: StateFlow<List<Candidate>> = _candidateState

    fun fetchCandidates() {
        viewModelScope.launch(Dispatchers.IO) {
            candidateRepository.getAllCandidates().collect { candidates ->
                _candidateState.value = candidates
            }
        }
    }

    init {
        fetchCandidates()
    }



}