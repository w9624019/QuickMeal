package com.example.QuickMeal.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.QuickMeal.DataModels.Users
import com.example.QuickMeal.Util.Rsource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


private val TAG="LoginViewModel"
@HiltViewModel
class LoginViewModel  @Inject constructor(
    private val firebaseAuth: FirebaseAuth
):ViewModel(){

    private val _login = MutableSharedFlow<Rsource<FirebaseUser>>()
    val login=_login.asSharedFlow()

    private val _resetpass=MutableSharedFlow<Rsource<String>>()
    val resetpass=_resetpass.asSharedFlow()


    private val _registerUser= MutableSharedFlow<Rsource<Users>>()
    val register=_registerUser.asSharedFlow()









    fun loginUser(email:String,password:String){
        Log.d(TAG, "Login View MODel called")
        viewModelScope.launch {
          _login.emit(Rsource.Loading())
        }

        firebaseAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener {
            viewModelScope.launch {
               it.user.let {
                   _login.emit(Rsource.Success(it))
               }
            }
        }.addOnFailureListener {
            viewModelScope.launch {
                _login.emit(Rsource.Error(it.message.toString()))
            }
        }

    }


    fun resetPassword(email: String){
        viewModelScope.launch {
            _resetpass.emit(Rsource.Loading())
        }
        viewModelScope.launch {
            firebaseAuth.sendPasswordResetEmail(email).addOnSuccessListener {
                viewModelScope.launch {
                    _resetpass.emit(Rsource.Success(email))
                }

            }.addOnFailureListener {
                viewModelScope.launch {
                    _resetpass.emit(Rsource.Error(it.message.toString()))
                }
            }
        }

    }


    fun logout(){
        firebaseAuth.signOut()
    }




}