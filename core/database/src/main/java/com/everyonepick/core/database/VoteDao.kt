package com.everyonepick.core.database

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

data class VotePollRecord(
    @Embedded val poll: VotePollEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "poll_id",
    )
    val options: List<VoteOptionEntity>,
    @Relation(
        parentColumn = "id",
        entityColumn = "poll_id",
    )
    val selections: List<VoteSelectionEntity>,
)

@Dao
interface VoteDao {
    @Transaction
    @Query("SELECT * FROM vote_polls ORDER BY created_at_epoch_millis DESC")
    fun observePolls(): Flow<List<VotePollRecord>>

    @Query("SELECT COUNT(*) FROM vote_polls")
    suspend fun countPolls(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPolls(polls: List<VotePollEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOptions(options: List<VoteOptionEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertSelection(selection: VoteSelectionEntity)

    @Query("SELECT * FROM vote_selections WHERE poll_id = :pollId LIMIT 1")
    suspend fun getSelection(pollId: Long): VoteSelectionEntity?

    @Query("UPDATE vote_options SET vote_count = vote_count + 1 WHERE id = :optionId")
    suspend fun incrementOptionCount(optionId: Long)

    @Query("UPDATE vote_options SET vote_count = CASE WHEN vote_count > 0 THEN vote_count - 1 ELSE 0 END WHERE id = :optionId")
    suspend fun decrementOptionCount(optionId: Long)
}

