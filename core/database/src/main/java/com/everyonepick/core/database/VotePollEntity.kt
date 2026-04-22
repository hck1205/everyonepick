package com.everyonepick.core.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vote_polls")
data class VotePollEntity(
    @PrimaryKey val id: Long,
    val title: String,
    val description: String,
    @ColumnInfo(name = "created_at_epoch_millis") val createdAtEpochMillis: Long,
    @ColumnInfo(name = "closes_at_epoch_millis") val closesAtEpochMillis: Long,
)

