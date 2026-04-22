package com.everyonepick.feature.vote

import android.text.format.DateUtils
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.everyonepick.core.model.VoteOption
import com.everyonepick.core.model.VotePoll
import kotlin.math.roundToInt

@Composable
fun VoteRoute(
    viewModel: VoteViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    VoteScreen(
        uiState = uiState,
        onVoteClick = viewModel::onVoteClick,
    )
}

@Composable
fun VoteScreen(
    uiState: VoteUiState,
    onVoteClick: (Long, Long) -> Unit,
) {
    when {
        uiState.isLoading -> {
            VoteEmptyState(
                title = "Loading vote",
                description = "Preparing a poll that users can join from the first screen.",
            )
        }

        uiState.polls.isEmpty() -> {
            VoteEmptyState(
                title = "No active polls",
                description = "The vote domain stays separate even if the main entry screen changes later.",
            )
        }

        else -> {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                uiState.polls.forEach { poll ->
                    VotePollCard(
                        poll = poll,
                        onVoteClick = onVoteClick,
                    )
                }
            }
        }
    }
}

@Composable
private fun VoteEmptyState(
    title: String,
    description: String,
) {
    OutlinedCard {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

@Composable
private fun VotePollCard(
    poll: VotePoll,
    onVoteClick: (Long, Long) -> Unit,
) {
    val closesText = DateUtils.getRelativeTimeSpanString(
        poll.closesAtEpochMillis,
        System.currentTimeMillis(),
        DateUtils.MINUTE_IN_MILLIS,
    ).toString()

    OutlinedCard {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Text(
                    text = "Live Vote",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
                Text(
                    text = poll.title,
                    style = MaterialTheme.typography.headlineMedium,
                )
                Text(
                    text = poll.description,
                    style = MaterialTheme.typography.bodyLarge,
                )
                Text(
                    text = "${poll.totalVotes} votes · closes $closesText",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                poll.options.forEach { option ->
                    VoteOptionRow(
                        option = option,
                        totalVotes = poll.totalVotes,
                        isSelected = poll.selectedOptionId == option.id,
                        onClick = { onVoteClick(poll.id, option.id) },
                    )
                }
            }
        }
    }
}

@Composable
private fun VoteOptionRow(
    option: VoteOption,
    totalVotes: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val share = option.share(totalVotes)
    val percentage = (share * 100).roundToInt()
    val shape = RoundedCornerShape(20.dp)

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = option.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            )
            Text(
                text = "${option.voteCount} votes · $percentage%",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape)
                .clickable(onClick = onClick),
            color = MaterialTheme.colorScheme.surfaceVariant,
            shape = shape,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(share.coerceIn(0f, 1f))
                        .height(48.dp)
                        .clip(shape)
                        .background(
                            if (isSelected) {
                                MaterialTheme.colorScheme.primaryContainer
                            } else {
                                MaterialTheme.colorScheme.secondaryContainer
                            },
                        ),
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = if (isSelected) "Selected" else "Vote",
                        style = MaterialTheme.typography.labelLarge,
                        color = if (isSelected) {
                            MaterialTheme.colorScheme.onPrimaryContainer
                        } else {
                            MaterialTheme.colorScheme.onSurface
                        },
                    )
                    Text(
                        text = if (isSelected) "Tap to change" else "Tap to vote",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

