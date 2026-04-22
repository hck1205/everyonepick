package com.everyonepick.feature.vote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.everyonepick.core.data.VoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class VoteViewModel @Inject constructor(
    private val voteRepository: VoteRepository,
) : ViewModel() {
    val uiState = voteRepository.polls
        .map { polls ->
            VoteUiState(
                polls = polls,
                isLoading = false,
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = VoteUiState(),
        )

    init {
        viewModelScope.launch {
            voteRepository.syncPolls()
        }
    }

    fun onVoteClick(
        pollId: Long,
        optionId: Long,
    ) {
        viewModelScope.launch {
            voteRepository.submitVote(
                pollId = pollId,
                optionId = optionId,
            )
        }
    }
}

