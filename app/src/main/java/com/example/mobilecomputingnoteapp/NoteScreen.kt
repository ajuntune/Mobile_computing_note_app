package com.example.mobilecomputingnoteapp

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import java.io.File
import java.util.concurrent.Executor

private var shouldShowCamera: MutableState<Boolean> = mutableStateOf(false)

@Composable
fun NoteScreen(
    state: NoteState,
    onEvent: (NoteEvent) -> Unit,
    outputDirectory: File,
    cameraExecutor: Executor,
    camera: Boolean
){
    Scaffold(
        floatingActionButton = {
            Row(modifier = Modifier.padding(15.dp)) {
                if (camera) {
                    FloatingActionButton(onClick = {
                        shouldShowCamera.value = true
                    }) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Open Camera"
                        )
                    }
                    if (shouldShowCamera.value) {
                        CameraView(
                            outputDirectory = outputDirectory,
                            executor = cameraExecutor,
                            onImageCaptured = ::handleImageCapture,
                            onEvent = onEvent,
                            onError = { Log.e("-", "View error:", it) })
                    }
                }

                Spacer(modifier = Modifier.width(20.dp))

                FloatingActionButton(onClick = {
                    onEvent(NoteEvent.ShowDialog)
                }) {
                    Icon(imageVector = Icons.Default.Add,
                        contentDescription = "New Note")
                }
            }

    }) { padding ->
        if(state.isAddingNote) {
            AddNoteDialog(state = state, onEvent = onEvent)
        }
        LazyColumn(
            contentPadding = padding,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
           items(state.notes) { note ->
               Row(modifier = Modifier
                   .fillMaxWidth()
                   .padding(20.dp)) {
                   Column(modifier = Modifier.weight(1f)) {
                       if (note.title.isNotBlank() && note.content.isNotBlank()) {
                           Text(text = note.title,
                               fontSize = 22.sp)
                           Text(text = note.content, fontSize = 15.sp)
                       }

                       if (note.uri.isNotBlank()){
                           AsyncImage(
                               model = note.uri,
                               contentDescription = null)
                       }
                   }
                   IconButton(onClick = {
                       onEvent(NoteEvent.DeleteNote(note))
                   }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Note")
                   }
               }

           }
            item {
                Spacer(modifier = Modifier.height(100.dp))
            }

        }
    }
    
}

private  fun handleImageCapture(uri: Uri) {
    Log.i("-", "Image captured: $uri")
    shouldShowCamera.value = false
}
