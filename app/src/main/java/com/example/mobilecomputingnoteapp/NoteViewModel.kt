package com.example.mobilecomputingnoteapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilecomputingnoteapp.data.Note
import com.example.mobilecomputingnoteapp.data.NoteDao
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class NoteViewModel(
    private val dao: NoteDao
): ViewModel() {
    private val _state = MutableStateFlow(NoteState())
    private val _notes = dao.getNotes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val state = combine(_state, _notes) { state, notes ->
        state.copy(
            notes = notes
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NoteState())

    fun onEvent(event: NoteEvent){
        when(event) {
            NoteEvent.SaveNote -> {
                val title = _state.value.title
                val content = _state.value.content
                val uri = _state.value.uri

                if ((title.isBlank() || content.isBlank()) && uri.isBlank()) {
                    return
                }

                val note = Note(
                    title = title,
                    content = content,
                    uri = uri
                    )

                viewModelScope.launch {
                    dao.upsertNote(note)
                }
                _state.update { it.copy(
                    isAddingNote = false,
                    title = "",
                    content = "",
                    uri = ""
                ) }
            }

            is NoteEvent.DeleteNote -> {
                viewModelScope.launch {
                    dao.deleteNote(event.note)
                }
            }

            is NoteEvent.SetContent -> {
                _state.update { it.copy(
                    content = event.content
                ) }
            }

            is NoteEvent.SetTitle -> {
                _state.update { it.copy(
                    title = event.title
                ) }
            }


            is NoteEvent.SetUri -> {
                _state.update { it.copy(
                    uri = event.uri
                ) }
            }

            NoteEvent.ShowDialog -> {
                _state.update { it.copy(
                    isAddingNote = true
                ) }
            }

            NoteEvent.HideDialog -> {
                _state.update { it.copy(
                    isAddingNote = false
                ) }
            }
        }
    }
}