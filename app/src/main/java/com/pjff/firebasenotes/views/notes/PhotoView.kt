package com.pjff.firebasenotes.views.notes

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import coil.compose.rememberAsyncImagePainter
import com.pjff.firebasenotes.viewModels.NotesViewModel

//Vid 263
@Composable
fun PhotoView(notesVM: NotesViewModel, idDoc: String){

    LaunchedEffect(Unit){
        notesVM.getNoteById(idDoc)
    }

    Column {
        val state = notesVM.state

        //En caso de no tener imagen
        if (state.imagePath.isNotEmpty()){
            Image(painter = rememberAsyncImagePainter(state.imagePath),
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            )

        }

    }

}