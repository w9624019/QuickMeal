package com.example.QuickMeal.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.QuickMeal.DataModels.Address
import com.example.QuickMeal.Util.Rsource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BillingViewModel @Inject constructor(
    private var firestore: FirebaseFirestore,
    private val auth :FirebaseAuth
):ViewModel(){
    private val _address =MutableStateFlow<Rsource<List<Address>>>(Rsource.Unspecified())
     val address =_address.asStateFlow()


    init {
        getUserAddress()
    }

fun getUserAddress(){
    viewModelScope.launch {
        _address.emit((Rsource.Loading()))
    }
    firestore.collection("users").document(auth.uid!!).collection("address")
        .addSnapshotListener{ value,error->

            if(error!=null) {
                viewModelScope.launch {
                    _address.emit(Rsource.Error(error.message))

                }
                return@addSnapshotListener
            }
            val addressDataa=value?.toObjects(Address::class.java)
            viewModelScope.launch {
                _address.emit(Rsource.Success(addressDataa!!))
            }

        }
}

}