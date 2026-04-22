package com.everyonepick.core.data.remote.model

data class VotePollDto(
    val id: Long,
    val title: String,
    val description: String,
    val closesAtEpochMillis: Long,
    val options: List<VoteOptionDto>,
)

data class VoteOptionDto(
    val id: Long,
    val title: String,
    val voteCount: Int,
    val displayOrder: Int,
)

