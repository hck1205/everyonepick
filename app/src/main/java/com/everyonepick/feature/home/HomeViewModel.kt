package com.everyonepick.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.everyonepick.core.data.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
) : ViewModel() {
    private val draftTitle = MutableStateFlow("")

    val uiState = combine(
        noteRepository.notes,
        draftTitle,
    ) { notes, draft ->
        HomeUiState(
            draftTitle = draft,
            notes = notes,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HomeUiState(),
    )

    fun onDraftTitleChange(value: String) {
        draftTitle.update { value.take(80) }
    }

    fun saveNote() {
        val title = draftTitle.value.trim()
        if (title.isBlank()) return

        viewModelScope.launch {
            noteRepository.createNote(title)
            draftTitle.value = ""
        }
    }
}
