package com.everyonepick.core.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        VotePollEntity::class,
        VoteOptionEntity::class,
        VoteSelectionEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
abstract class EveryonePickDatabase : RoomDatabase() {
    abstract fun voteDao(): VoteDao
}
