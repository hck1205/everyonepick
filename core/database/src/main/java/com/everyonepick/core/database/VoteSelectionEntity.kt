package com.everyonepick.core.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "vote_selections",
    foreignKeys = [
        ForeignKey(
            entity = VotePollEntity::class,
            parentColumns = ["id"],
            childColumns = ["poll_id"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = VoteOptionEntity::class,
            parentColumns = ["id"],
            childColumns = ["option_id"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("poll_id"), Index("option_id")],
)
data class VoteSelectionEntity(
    @PrimaryKey
    @ColumnInfo(name = "poll_id")
    val pollId: Long,
    @ColumnInfo(name = "option_id") val optionId: Long,
    @ColumnInfo(name = "selected_at_epoch_millis") val selectedAtEpochMillis: Long,
)

