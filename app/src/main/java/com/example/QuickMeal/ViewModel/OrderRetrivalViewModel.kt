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
class OrderRetrivalViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
)
    :ViewModel() {

        private val _allorders= MutableStateFlow<Rsource<List<Order>>>(Rsource.Unspecified())
    val allorder=_allorders.asStateFlow()

    init {
        getAllOrders()
    }

    fun getAllOrders(){
        viewModelScope.launch {
            _allorders.emit(Rsource.Loading())
        }

        firestore.collection("users").document(auth.uid!!).collection("orders")
            .get().addOnSuccessListener {
                val order=it.toObjects(Order::class.java)
                viewModelScope.launch {
                    _allorders.emit(Rsource.Success(order))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _allorders.emit(Rsource.Error(it.message.toString()))
                }
            }

    }
}