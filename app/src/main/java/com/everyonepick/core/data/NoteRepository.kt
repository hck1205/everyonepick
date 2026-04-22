package com.everyonepick.core.data

import com.everyonepick.core.model.QuickNote
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    val notes: Flow<List<QuickNote>>

    suspend fun createNote(title: String)
}

