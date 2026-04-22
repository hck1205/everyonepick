package com.everyonepick.core.data.local

import androidx.room.withTransaction
import com.everyonepick.core.database.EveryonePickDatabase
import com.everyonepick.core.database.VoteDao
import com.everyonepick.core.database.VoteOptionEntity
import com.everyonepick.core.database.VotePollEntity
import com.everyonepick.core.database.VotePollRecord
import com.everyonepick.core.database.VoteSelectionEntity
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class VoteLocalDataSource @Inject constructor(
    private val database: EveryonePickDatabase,
    private val voteDao: VoteDao,
) {
    fun observePolls(): Flow<List<VotePollRecord>> = voteDao.observePolls()

    suspend fun hasPolls(): Boolean = voteDao.countPolls() > 0

    suspend fun upsertPolls(
        polls: List<VotePollEntity>,
        options: List<VoteOptionEntity>,
    ) {
        database.withTransaction {
            voteDao.insertPolls(polls)
            voteDao.insertOptions(options)
        }
    }

    suspend fun submitVote(
        pollId: Long,
        optionId: Long,
    ) {
        database.withTransaction {
            val previousSelection = voteDao.getSelection(pollId)
            if (previousSelection?.optionId == optionId) return@withTransaction

            if (previousSelection != null) {
                voteDao.decrementOptionCount(previousSelection.optionId)
            }

            voteDao.incrementOptionCount(optionId)
            voteDao.upsertSelection(
                VoteSelectionEntity(
                    pollId = pollId,
                    optionId = optionId,
                    selectedAtEpochMillis = System.currentTimeMillis(),
                ),
            )
        }
    }
}

