package com.everyonepick.core.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "vote_options",
    foreignKeys = [
        ForeignKey(
            entity = VotePollEntity::class,
            parentColumns = ["id"],
            childColumns = ["poll_id"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("poll_id")],
)
data class VoteOptionEntity(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "poll_id") val pollId: Long,
    val title: String,
    @ColumnInfo(name = "vote_count") val voteCount: Int,
    @ColumnInfo(name = "display_order") val displayOrder: Int,
)

