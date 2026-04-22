package com.everyonepick.core.data.remote

import com.everyonepick.core.data.remote.model.VotePollDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface VoteApi {
    @GET("polls/current")
    suspend fun getCurrentPolls(): List<VotePollDto>

    @POST("polls/{pollId}/vote")
    suspend fun submitVote(
        @Path("pollId") pollId: Long,
        @Body body: VoteSubmissionRequestDto,
    ): VoteSubmissionResponseDto
}

data class VoteSubmissionRequestDto(
    val optionId: Long,
)

data class VoteSubmissionResponseDto(
    val pollId: Long,
    val optionId: Long,
    val accepted: Boolean,
)

