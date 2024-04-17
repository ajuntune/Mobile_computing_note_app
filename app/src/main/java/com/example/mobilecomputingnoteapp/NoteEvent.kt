package com.example.mobilecomputingnoteapp

import com.example.mobilecomputingnoteapp.data.Note

sealed interface NoteEvent{
    object SaveNote: NoteEvent
    data class SetTitle(val title: String): NoteEvent
    data class SetContent(val content: String): NoteEvent
    data class SetUri(val uri: String): NoteEvent
    object ShowDialog: NoteEvent
    object HideDialog: NoteEvent
    data class DeleteNote(val note: Note): NoteEvent
}