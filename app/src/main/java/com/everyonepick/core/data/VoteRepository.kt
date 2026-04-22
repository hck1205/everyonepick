package com.everyonepick.core.data

import com.everyonepick.core.model.VotePoll
import kotlinx.coroutines.flow.Flow

interface VoteRepository {
    val polls: Flow<List<VotePoll>>

    suspend fun ensureSeedData()

    suspend fun submitVote(
        pollId: Long,
        optionId: Long,
    )
}

