package com.everyonepick.core.data.remote

import com.everyonepick.core.data.network.NetworkRequestExecutor
import com.everyonepick.core.data.network.NetworkResult
import com.everyonepick.core.data.remote.model.VotePollDto
import javax.inject.Inject

class VoteRemoteDataSource @Inject constructor(
    private val voteApi: VoteApi,
    private val networkRequestExecutor: NetworkRequestExecutor,
) {
    suspend fun getCurrentPolls(): List<VotePollDto> =
        when (val result = networkRequestExecutor.execute { voteApi.getCurrentPolls() }) {
            is NetworkResult.Success -> result.value
            is NetworkResult.Failure -> throw result.throwable
        }

    suspend fun submitVote(
        pollId: Long,
        optionId: Long,
    ) {
        when (
            val result = networkRequestExecutor.execute {
                voteApi.submitVote(
                    pollId = pollId,
                    body = VoteSubmissionRequestDto(optionId = optionId),
                )
            }
        ) {
            is NetworkResult.Success -> Unit
            is NetworkResult.Failure -> throw result.throwable
        }
    }
}
