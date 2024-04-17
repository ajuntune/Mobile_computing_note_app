package com.example.mobilecomputingnoteapp

import com.example.mobilecomputingnoteapp.data.Note

data class NoteState(
    val notes: List<Note> = emptyList(),
    val title: String = "",
    val content: String = "",
    val uri: String = "",
    val isAddingNote: Boolean = false,
)