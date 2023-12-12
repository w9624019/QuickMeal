package com.example.QuickMeal.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.QuickMeal.DataModels.Address
import com.example.QuickMeal.Util.Rsource

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class AddressViewModel @Inject constructor(
    private val firestore:FirebaseFirestore,
    private val auth: FirebaseAuth
):ViewModel(){
    private val _addNewAddesss=MutableStateFlow<Rsource<Address>>(Rsource.Unspecified())
    val addNewAddress=_addNewAddesss.asStateFlow()
    private val _error= MutableSharedFlow<String>()
    val error=_error.asSharedFlow()

    fun addAddress(address:Address){

val validateInputs=validateInputs(address)
        if(validateInputs){
            viewModelScope.launch {
                _addNewAddesss.emit(Rsource.Loading())
            }

            firestore.collection("users").document(auth.uid!!).collection("address")
                .document().set(address).addOnSuccessListener {
                    viewModelScope.launch {
                        _addNewAddesss.emit(Rsource.Success(address))
                    }

                }.addOnFailureListener {
                    viewModelScope.launch {
                        _addNewAddesss.emit(Rsource.Error(it.message.toString()))
                    }

                }
        }   else {
            viewModelScope.launch {
                _error.emit("All fields are Required")
            }

        }



    }

    private fun validateInputs(address:Address): Boolean {

        return address.addressTitle.isNotEmpty() && address.City.isNotEmpty()
                && address.Phone.toString().isNotEmpty() && address.State.isNotEmpty()
                && address.fullName.isNotEmpty() && address.street.isNotEmpty()

    }

}