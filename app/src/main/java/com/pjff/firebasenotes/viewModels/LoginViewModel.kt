package com.pjff.firebasenotes.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pjff.firebasenotes.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

//Vid 176 ,Configuracion Firebase
class LoginViewModel: ViewModel() {

    //Vid 176
    private val auth: FirebaseAuth = Firebase.auth
    //Vid 177
    var showAlert by mutableStateOf(false)

    fun login(email: String, password: String, onSuccess: () -> Unit){
        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful){
                            onSuccess()
                        }else{
                            Log.d("ERROR EN FIREBASE","Usuario y contrasena incorrectos")
                            //Vid 177
                            showAlert = true
                        }
                    }
            }catch (e: Exception){
                Log.d("ERROR EN JETPACK", "ERROR: ${e.localizedMessage}")
            }
        }
    }

    //Vid 178
    fun createUser(email: String, password: String, username: String, onSuccess: () -> Unit){
        viewModelScope.launch {
            try {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful){
                            saveUser(username)
                            onSuccess()
                        }else{
                            Log.d("ERROR EN FIREBASE","Error al crear usuario")
                            showAlert = true
                        }
                    }
            }catch (e: Exception){
                Log.d("ERROR EN JETPACK", "ERROR: ${e.localizedMessage}")
            }
        }
    }

    //Vid 178
    private fun saveUser(username: String){
        //sacamos el uid y email
        val id = auth.currentUser?.uid
        val email = auth.currentUser?.email

        //Vid 181 addd viewModelScope.launch(Dispatchers.IO)
        viewModelScope.launch(Dispatchers.IO){
            val user = UserModel(
                userId = id.toString(),
                email = email.toString(),
                username = username
            )

            //Para guardar en firebase
            FirebaseFirestore.getInstance().collection("Users")
                .add(user)
                .addOnSuccessListener {
                    Log.d("GUARDO", "Guardo correctamente")
                }.addOnFailureListener{
                    Log.d("ERROR AL GUARDAR", "ERROR al guardar en firestore")
                }
        }


    }

    //Vid 177
    fun closeAlert(){
        showAlert = false
    }

}