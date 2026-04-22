package com.everyonepick.feature.vote

import com.everyonepick.core.model.VotePoll

data class VoteUiState(
    val polls: List<VotePoll> = emptyList(),
    val isLoading: Boolean = true,
)

