package com.pjff.firebasenotes.viewModels

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pjff.firebasenotes.model.NotesState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID

//Vid 181
class NotesViewModel:ViewModel() {

    //Vid 183
    private val auth: FirebaseAuth = Firebase.auth
    private val firestore = Firebase.firestore
    //Vid 262
    private val storageRef = FirebaseStorage.getInstance().reference

    //Vid 185
    private val _notesData = MutableStateFlow<List<NotesState>>(emptyList())
    val notesData: StateFlow<List<NotesState>> = _notesData

    //Vid 189
    var state by mutableStateOf(NotesState())
        private set
    //Vid 190
    fun onValue(value:String, text: String){
        when(text){
            "title" -> state = state.copy(title = value)
            "note" -> state = state.copy(note = value)
        }
    }

    //Vid 185
    fun fetchNotes(){
        val email = auth.currentUser?.email
        firestore.collection("Notes")
            .whereEqualTo("emailUser", email.toString())
            .addSnapshotListener {querySnapshot, error ->
                if (error != null){
                    return@addSnapshotListener
                }
                //sacamos los datos
                val documents = mutableListOf<NotesState>()
                if (querySnapshot != null){
                    for (document in querySnapshot){
                        //sacamos cada dato
                        val myDocument = document.toObject(NotesState::class.java).copy(idDoc = document.id)
                        documents.add(myDocument)
                    }
                }
                _notesData.value = documents
            }
    }

    //Vid 183
    //Vid 263, add el nuevo parmaetro,image:Uri
    fun saveNewNote(title: String, note:String,image:Uri, onSuccess:() -> Unit){
        val email = auth.currentUser?.email
        viewModelScope.launch(Dispatchers.IO) {
            //Vid 262
            val imagePath = uploadImage(image)
            try {
                val newNote = hashMapOf(
                    "title" to title,
                    "note" to note,
                    "date" to formatDate(),
                    "emailUser" to email.toString(),
                    //Vid 262
                    "imagePath" to imagePath
                )
                firestore.collection("Notes").add(newNote)
                    .addOnSuccessListener {
                        onSuccess()
                    }
            }catch (e:Exception){
                Log.d("ERROR SAVE","Error al guardar ${e.localizedMessage} ")
            }
        }
    }

    //Vid 262
    //retornamos el string que es donde se quedo guardada la imagén.
    private suspend fun uploadImage(image: Uri) : String {
        return try {
            val imageRef = storageRef.child("images/${UUID.randomUUID()}")
            val taskSnapshot = imageRef.putFile(image).await()
            val downloadUri = taskSnapshot.metadata?.reference?.downloadUrl?.await()
            downloadUri.toString()
        }catch (e: Exception){
            ""
        }
    }



    //Vid 184,formato de  fecha
    private fun formatDate(): String {
        val currentDate : Date = Calendar.getInstance().time
        val res = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return res.format(currentDate)
    }

    //Vid 189
    fun getNoteById(documentId: String){
        firestore.collection("Notes")
            .document(documentId)
            //El gion bajo es para np crear el error
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null){
                    val note = snapshot.toObject(NotesState::class.java)
                    state = state.copy(
                        //para editar el titulo y la nota
                        title = note?.title ?: "",
                        note = note?.note ?: "",
                        //Vid 263
                        imagePath = note?.imagePath ?: ""
                    )
                }
            }
    }

    //Vid 191
    fun updateNote(idDoc: String, onSuccess:() -> Unit){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val editNote = hashMapOf(
                    "title" to state.title,
                    "note" to state.note,
                )
                //estamos editando el documento : document(idDoc)
                firestore.collection("Notes").document(idDoc)
                    .update(editNote as Map<String, Any>)
                    .addOnSuccessListener {
                        onSuccess()
                    }
            }catch (e:Exception){
                Log.d("ERROR EDIT","Error al editar ${e.localizedMessage} ")
            }
        }
    }

    //Vid 192
    fun deleteNote(idDoc: String,image: String, onSuccess:() -> Unit){
        viewModelScope.launch(Dispatchers.IO) {
            deleteImage(image)
            try {
                firestore.collection("Notes").document(idDoc)
                    .delete()
                    .addOnSuccessListener {
                        onSuccess()
                    }
            }catch (e:Exception){
                Log.d("ERROR DELETE","Error al eliminar ${e.localizedMessage} ")
            }
        }
    }

    //Vid 265

    suspend fun deleteImage(imageUrl : String){
        val imageRef = storageRef.child(imageUrl.toUri().lastPathSegment ?: "" )
        try {
            imageRef.delete()
        }catch (e: Exception){
            Log.d("Fallo", "Fallo al eliminar la imagen")
        }
    }

    //Para cerrar la sesion en firebase
    fun signOut(){
        auth.signOut()
    }

}