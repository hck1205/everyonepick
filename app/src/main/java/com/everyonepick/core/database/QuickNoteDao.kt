package com.everyonepick.core.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface QuickNoteDao {
    @Query("SELECT * FROM quick_notes ORDER BY created_at_epoch_millis DESC")
    fun observeAll(): Flow<List<QuickNoteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: QuickNoteEntity)
}

