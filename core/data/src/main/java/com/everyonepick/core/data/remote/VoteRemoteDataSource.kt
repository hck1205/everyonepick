package com.everyonepick.core.data.remote

import com.everyonepick.core.data.remote.model.VotePollDto
import javax.inject.Inject

class VoteRemoteDataSource @Inject constructor(
    private val voteApi: VoteApi,
) {
    suspend fun getCurrentPolls(): List<VotePollDto> = voteApi.getCurrentPolls()

    suspend fun submitVote(
        pollId: Long,
        optionId: Long,
    ) {
        voteApi.submitVote(
            pollId = pollId,
            body = VoteSubmissionRequestDto(optionId = optionId),
        )
    }
}

