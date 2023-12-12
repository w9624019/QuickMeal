package com.example.QuickMeal.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.QuickMeal.Util.Rsource
import com.example.agrishop.Data.Order.Order
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class OrderViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
):ViewModel(){

    private val _order=MutableStateFlow<Rsource<Order>>(Rsource.Unspecified())
    val order=_order.asStateFlow()


    fun placeOrder(order: Order){
        viewModelScope.launch {
            _order.emit(Rsource.Loading())
        }


        firestore.runBatch{batch->
            firestore.collection("users").document(auth.uid!!).collection("orders")
                .document().set(order)

            firestore.collection("orders").document().set(order)

            firestore.collection("users").document(auth.uid!!).collection("cart").get()
                .addOnSuccessListener {
                    it.documents.forEach(){
                        it.reference.delete()
                    }
                }
        }.addOnSuccessListener {
            viewModelScope.launch {
                _order.emit(Rsource.Success(order))
            }
        }.addOnFailureListener {
            viewModelScope.launch {
                _order.emit(Rsource.Error(it.message.toString()))
            }
        }
    }
}