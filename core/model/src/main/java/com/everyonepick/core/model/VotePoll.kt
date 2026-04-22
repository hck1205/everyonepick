package com.everyonepick.core.model

data class VotePoll(
    val id: Long,
    val title: String,
    val description: String,
    val closesAtEpochMillis: Long,
    val totalVotes: Int,
    val selectedOptionId: Long?,
    val options: List<VoteOption>,
)

data class VoteOption(
    val id: Long,
    val title: String,
    val voteCount: Int,
    val displayOrder: Int,
) {
    fun share(totalVotes: Int): Float =
        if (totalVotes == 0) 0f else voteCount.toFloat() / totalVotes.toFloat()
}

