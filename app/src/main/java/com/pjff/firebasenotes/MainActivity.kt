package com.pjff.firebasenotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.pjff.firebasenotes.navigation.NavManager
import com.pjff.firebasenotes.ui.theme.FirebaseNotesTheme
import com.pjff.firebasenotes.viewModels.LoginViewModel
import com.pjff.firebasenotes.viewModels.NotesViewModel
import com.pjff.firebasenotes.views.login.TabsView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Vid 175
        val loginVM : LoginViewModel by viewModels()
        val notesVM : NotesViewModel by viewModels()
        setContent {
            FirebaseNotesTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //Vid 175,agregamos las dos variables de los que estaremos usandio
                    NavManager(loginVM, notesVM)
                }
            }
        }
    }
}
