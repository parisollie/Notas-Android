package com.pjff.firebasenotes.views.notes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.pjff.firebasenotes.components.CardNote
import com.pjff.firebasenotes.viewModels.NotesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
//Vid 181,ponemos el NavController que apunta a notas
fun HomeView(navController: NavController, notesVM: NotesViewModel){

    //Vid 186, para ver las notas
    LaunchedEffect(Unit){
        notesVM.fetchNotes()
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Mis Notas") },
                //iconos de lado izquierdo
                navigationIcon = {
                    IconButton(onClick = {
                        //Para salir
                        notesVM.signOut()
                        navController.popBackStack()
                    }) {
                        Icon(imageVector = Icons.Default.ExitToApp, contentDescription = "")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        //Vid 183,
                        navController.navigate("AddNoteView")
                    }) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "")
                    }
                }
            )
        }
    ) { pad ->
        Column(
            modifier = Modifier.padding(pad),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //Vid 186
            val datos by notesVM.notesData.collectAsState()
            
            LazyColumn{

                items(datos){ item ->
                    //Vid 187
                    CardNote(title = item.title, note = item.note, date = item.date, onClick = {
                        //Vid 188,ponemos el edit
                        navController.navigate("EditNoteView/${item.idDoc}")
                    }) {
                        //Vud 264
                        navController.navigate("PhotoView/${item.idDoc}")
                    }
                }
            }
        }

    }
}