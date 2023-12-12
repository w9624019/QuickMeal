package com.example.QuickMeal.ViewModel

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.QuickMeal.DataModels.CartProduct
import com.example.QuickMeal.Firebase.FirebaseCommon
import com.example.QuickMeal.Util.Rsource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val firebaseCommon: FirebaseCommon
): ViewModel() {

    private val _cartProduct= MutableStateFlow<Rsource<List<CartProduct>>>(Rsource.Unspecified())
    val cartProducts=_cartProduct.asStateFlow()
    private val _deleteDialog= MutableSharedFlow<CartProduct>()
    val deleteDialog=_deleteDialog.asSharedFlow()


    val productPrice=cartProducts.map {
        when(it){
            is Rsource.Success ->{
                calculatePrice(it.data!!)
            }
            else -> null
        }
    }

    private fun calculatePrice(data: List<CartProduct>): Float {
        return data.sumByDouble { cartProduct ->
            (cartProduct.product.price*cartProduct.quantity).toDouble()
        }.toFloat()
    }


    fun deleteCart(cartProduct: CartProduct) {
        val index=cartProducts.value.data?.indexOf(cartProduct)
        if(index!==null && index != -1){
            val docID=cartproductDocs[index].id
            firestore.collection("users").document(auth.uid!!).
            collection("cart").document(docID).delete()
        }



    }

    private var cartproductDocs= emptyList<DocumentSnapshot>()

    init {
        getcartProducts()
    }

    @SuppressLint("SuspiciousIndentation")
    private fun getcartProducts(){
        viewModelScope.launch {
            _cartProduct.emit(Rsource.Loading())
        }
        firestore.collection("users").document(auth.uid!!).collection("cart")
            .addSnapshotListener{ value,error->
                if(error!==null || value==null){
                    viewModelScope.launch {
                        _cartProduct.emit(Rsource.Error(error?.message.toString()))
                    }
                }  else {
                    cartproductDocs=value.documents
                    val cartProduct=value.toObjects(CartProduct::class.java)
                    viewModelScope.launch {
                        _cartProduct.emit(Rsource.Success(cartProduct))
                    }
                }

            }
    }



    fun changeQuantity(
        cartproduct:CartProduct,
        quantityChanging:FirebaseCommon.QuantityChanging
    ){




        val index=cartProducts.value.data?.indexOf(cartproduct)
        if(index!==null && index != -1){
            val docID=cartproductDocs[index].id
            when(quantityChanging){
                FirebaseCommon.QuantityChanging.DECREASE->{
                    if(cartproduct.quantity<=1){

                        viewModelScope.launch {
                            _deleteDialog.emit(cartproduct)
                        }
                        return
                    }
                    viewModelScope.launch {
                        _cartProduct.emit(Rsource.Loading())
                    }
                    DecreaseQuantity(docID)
                }
                FirebaseCommon.QuantityChanging.INCREASE->{

                    viewModelScope.launch {
                        _cartProduct.emit(Rsource.Loading())
                    }
                    IncreaseQuantity(docID)
                }
            }
        }


    }

    private fun IncreaseQuantity(docID: String) {
        firebaseCommon.increaseQuantity(docID){r,e->
            if(e!=null){
                viewModelScope.launch {
                    _cartProduct.emit(Rsource.Error(e.message.toString()))
                }
            }
        }
    }

    private fun DecreaseQuantity(docID: String) {
        firebaseCommon.decreaseQuantity(docID){r,e->
            if(e!=null){
                viewModelScope.launch {
                    _cartProduct.emit(Rsource.Error(e.message.toString()))
                }
            }
        }
    }


}