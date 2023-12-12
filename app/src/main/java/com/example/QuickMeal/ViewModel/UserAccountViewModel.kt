package com.example.QuickMeal.ViewModel

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.QuickMeal.DataModels.Users
import com.example.QuickMeal.QuickMealActivity
import com.example.QuickMeal.Util.Rsource

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class UserAccountViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth


) :ViewModel(){


    private val _user=MutableStateFlow<Rsource<Users>>(Rsource.Unspecified())
    val user=_user.asStateFlow()


    private val _editInfo=MutableStateFlow<Rsource<Users>>(Rsource.Unspecified())
    val editinfo=_editInfo.asStateFlow()

        init {

            getuser()
        }


    fun getuser(){
        viewModelScope.launch {
            _user.emit(Rsource.Loading())
        }
        firestore.collection("users").document(auth.uid!!).get()
            .addOnSuccessListener {
                Log.d("UserAccountViewMode;", it.data.toString())
                val user=it.toObject(Users::class.java)
                user?.let {
                    viewModelScope.launch {
                        _user.emit(Rsource.Success(it))
                    }
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _user.emit(Rsource.Error(it.message.toString()))
                }

            }
    }


    fun updateUser(user:Users){

        val areinputValid=  user.firstName.trim().isNotEmpty() &&
                user.lastName.trim().isNotEmpty()


        if(!areinputValid){
            viewModelScope.launch {
                _user.emit(Rsource.Error("Check your inputs"))
            }
            return
        }


        viewModelScope.launch {
            _editInfo.emit(Rsource.Loading())
        }



            saveUserInfo(user,true)



    }

    private fun saveUserInfo(user: Users, shouldRetriveImage: Boolean) {
            firestore.runTransaction {transaction ->
                val docref=firestore.collection("users").document(auth.uid!!)

                if(shouldRetriveImage){
                    val currentUser=transaction.get(docref).toObject(Users::class.java)


                    currentUser?.let { transaction.set(docref, it) }
                } else {
                    transaction.set(docref,user)
                }
            } .addOnSuccessListener {
                viewModelScope.launch {
                    _editInfo.emit(Rsource.Success(user))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _editInfo.emit(Rsource.Error(it.message.toString()))
                }
            }
    }

}