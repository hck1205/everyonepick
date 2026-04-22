package com.everyonepick.core.data

import androidx.room.withTransaction
import com.everyonepick.core.common.IoDispatcher
import com.everyonepick.core.database.EveryonePickDatabase
import com.everyonepick.core.database.VoteDao
import com.everyonepick.core.database.VoteOptionEntity
import com.everyonepick.core.database.VotePollEntity
import com.everyonepick.core.database.VotePollRecord
import com.everyonepick.core.database.VoteSelectionEntity
import com.everyonepick.core.model.VoteOption
import com.everyonepick.core.model.VotePoll
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class OfflineVoteRepository @Inject constructor(
    private val database: EveryonePickDatabase,
    private val voteDao: VoteDao,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : VoteRepository {
    override val polls: Flow<List<VotePoll>> =
        voteDao.observePolls()
            .map { records -> records.map(VotePollRecord::asExternalModel) }
            .flowOn(ioDispatcher)

    override suspend fun ensureSeedData() {
        withContext(ioDispatcher) {
            if (voteDao.countPolls() > 0) return@withContext

            val now = System.currentTimeMillis()
            val pollId = 1L

            voteDao.insertPolls(
                listOf(
                    VotePollEntity(
                        id = pollId,
                        title = "오늘의 픽을 바로 정해볼까요?",
                        description = "EveryonePick의 첫 메인 화면은 지금 바로 선택에 참여하는 경험을 중심으로 둡니다.",
                        createdAtEpochMillis = now,
                        closesAtEpochMillis = now + TWO_DAYS_IN_MILLIS,
                    ),
                ),
            )
            voteDao.insertOptions(
                listOf(
                    VoteOptionEntity(
                        id = 101L,
                        pollId = pollId,
                        title = "삼겹살",
                        voteCount = 14,
                        displayOrder = 0,
                    ),
                    VoteOptionEntity(
                        id = 102L,
                        pollId = pollId,
                        title = "초밥",
                        voteCount = 18,
                        displayOrder = 1,
                    ),
                    VoteOptionEntity(
                        id = 103L,
                        pollId = pollId,
                        title = "마라탕",
                        voteCount = 11,
                        displayOrder = 2,
                    ),
                    VoteOptionEntity(
                        id = 104L,
                        pollId = pollId,
                        title = "파스타",
                        voteCount = 9,
                        displayOrder = 3,
                    ),
                ),
            )
        }
    }

    override suspend fun submitVote(
        pollId: Long,
        optionId: Long,
    ) {
        withContext(ioDispatcher) {
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

    private companion object {
        const val TWO_DAYS_IN_MILLIS = 2 * 24 * 60 * 60 * 1000L
    }
}

private fun VotePollRecord.asExternalModel(): VotePoll {
    val selectedOptionId = selections.firstOrNull()?.optionId
    val mappedOptions = options
        .sortedBy(VoteOptionEntity::displayOrder)
        .map { option ->
            VoteOption(
                id = option.id,
                title = option.title,
                voteCount = option.voteCount,
                displayOrder = option.displayOrder,
            )
        }

    return VotePoll(
        id = poll.id,
        title = poll.title,
        description = poll.description,
        closesAtEpochMillis = poll.closesAtEpochMillis,
        totalVotes = mappedOptions.sumOf(VoteOption::voteCount),
        selectedOptionId = selectedOptionId,
        options = mappedOptions,
    )
}

