package com.pjff.firebasenotes.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.pjff.firebasenotes.viewModels.LoginViewModel
import com.pjff.firebasenotes.viewModels.NotesViewModel
import com.pjff.firebasenotes.views.login.BlankView
import com.pjff.firebasenotes.views.notes.HomeView
import com.pjff.firebasenotes.views.login.TabsView
import com.pjff.firebasenotes.views.notes.AddNoteView
import com.pjff.firebasenotes.views.notes.EditNoteView
import com.pjff.firebasenotes.views.notes.PhotoView

//Vid 175,
@Composable
fun NavManager(loginVM: LoginViewModel, notesVM: NotesViewModel){
    val navController = rememberNavController()
    //Creamos el navhost
    //vid 182, ponemos blank
    NavHost(navController = navController, startDestination = "Blank" ){
        composable("Blank"){
            BlankView(navController)
        }
        //Vid 175,ponemos la ruta de Login
        composable("Login"){
            TabsView(navController, loginVM)
        }
        //Vid 181
        composable("Home"){
            HomeView(navController, notesVM)
        }
        //Vid 183
        composable("AddNoteView"){
            AddNoteView(navController, notesVM)
        }
        //Vid 188
        composable("EditNoteView/{idDoc}", arguments = listOf(
            navArgument("idDoc") { type = NavType.StringType }
        )){
            val idDoc = it.arguments?.getString("idDoc") ?: ""
            EditNoteView(navController, notesVM, idDoc)
        }

        //Vid 263
        composable("PhotoView/{idDoc}", arguments = listOf(
            navArgument("idDoc") { type = NavType.StringType }
        )){
            val idDoc = it.arguments?.getString("idDoc") ?: ""
            PhotoView(notesVM, idDoc)
        }


    }
}