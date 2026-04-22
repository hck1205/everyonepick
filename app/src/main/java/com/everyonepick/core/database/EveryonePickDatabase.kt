package com.everyonepick.core.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [QuickNoteEntity::class],
    version = 1,
    exportSchema = true,
)
abstract class EveryonePickDatabase : RoomDatabase() {
    abstract fun quickNoteDao(): QuickNoteDao
}

