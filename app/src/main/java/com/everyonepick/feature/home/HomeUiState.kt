package com.everyonepick.feature.home

import com.everyonepick.core.model.QuickNote

data class HomeUiState(
    val draftTitle: String = "",
    val notes: List<QuickNote> = emptyList(),
) {
    val canSave: Boolean
        get() = draftTitle.isNotBlank()
}

