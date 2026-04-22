package com.everyonepick.core.data

import com.everyonepick.core.common.IoDispatcher
import com.everyonepick.core.database.QuickNoteDao
import com.everyonepick.core.database.QuickNoteEntity
import com.everyonepick.core.model.QuickNote
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class OfflineNoteRepository @Inject constructor(
    private val quickNoteDao: QuickNoteDao,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : NoteRepository {
    override val notes: Flow<List<QuickNote>> =
        quickNoteDao.observeAll()
            .map { entities -> entities.map(QuickNoteEntity::asExternalModel) }
            .flowOn(ioDispatcher)

    override suspend fun createNote(title: String) {
        withContext(ioDispatcher) {
            quickNoteDao.insert(
                QuickNoteEntity(
                    title = title.trim(),
                    createdAtEpochMillis = System.currentTimeMillis(),
                ),
            )
        }
    }
}

private fun QuickNoteEntity.asExternalModel(): QuickNote =
    QuickNote(
        id = id,
        title = title,
        createdAtEpochMillis = createdAtEpochMillis,
    )
