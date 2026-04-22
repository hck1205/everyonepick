package com.everyonepick.core.data

import com.everyonepick.core.common.IoDispatcher
import com.everyonepick.core.data.local.VoteLocalDataSource
import com.everyonepick.core.data.remote.VoteRemoteDataSource
import com.everyonepick.core.data.remote.model.VoteOptionDto
import com.everyonepick.core.data.remote.model.VotePollDto
import com.everyonepick.core.database.VoteOptionEntity
import com.everyonepick.core.database.VotePollEntity
import com.everyonepick.core.database.VotePollRecord
import com.everyonepick.core.model.VoteOption
import com.everyonepick.core.model.VotePoll
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class DefaultVoteRepository @Inject constructor(
    private val localDataSource: VoteLocalDataSource,
    private val remoteDataSource: VoteRemoteDataSource,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : VoteRepository {
    override val polls: Flow<List<VotePoll>> =
        localDataSource.observePolls()
            .map { records -> records.map(VotePollRecord::asExternalModel) }
            .flowOn(ioDispatcher)

    override suspend fun syncPolls() {
        withContext(ioDispatcher) {
            if (localDataSource.hasPolls()) return@withContext

            val remotePolls = remoteDataSource.getCurrentPolls()
            localDataSource.upsertPolls(
                polls = remotePolls.map(VotePollDto::asEntity),
                options = remotePolls.flatMap(VotePollDto::asOptionEntities),
            )
        }
    }

    override suspend fun submitVote(
        pollId: Long,
        optionId: Long,
    ) {
        withContext(ioDispatcher) {
            remoteDataSource.submitVote(
                pollId = pollId,
                optionId = optionId,
            )
            localDataSource.submitVote(
                pollId = pollId,
                optionId = optionId,
            )
        }
    }
}

private fun VotePollDto.asEntity(): VotePollEntity =
    VotePollEntity(
        id = id,
        title = title,
        description = description,
        createdAtEpochMillis = 0L,
        closesAtEpochMillis = closesAtEpochMillis,
    )

private fun VotePollDto.asOptionEntities(): List<VoteOptionEntity> =
    options.map { option -> option.asEntity(pollId = id) }

private fun VoteOptionDto.asEntity(pollId: Long): VoteOptionEntity =
    VoteOptionEntity(
        id = id,
        pollId = pollId,
        title = title,
        voteCount = voteCount,
        displayOrder = displayOrder,
    )

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

