package com.example.mobilecomputingnoteapp

import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AddNoteDialog(
    state: NoteState,
    onEvent: (NoteEvent) -> Unit,
    modifier: Modifier = Modifier
){
    AlertDialog(
        modifier = Modifier,
        onDismissRequest = { onEvent(NoteEvent.HideDialog) },
        title = { Text(text = "Create New Note")},
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(value = state.title, onValueChange = {
                    onEvent(NoteEvent.SetTitle(it))
                },
                placeholder = {
                    Text(text = "Title")
                })

                TextField(value = state.content, modifier = Modifier.height(200.dp), onValueChange = {
                    onEvent(NoteEvent.SetContent(it))
                },
                    placeholder = {
                        Text(text = "Content")
                    })
            }
        },
        buttons = {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                Button(onClick = { onEvent(NoteEvent.SaveNote) }) {
                    Text(text = "Save")
                }
            }
        }
    )
}
