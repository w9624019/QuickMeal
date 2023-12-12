package com.example.QuickMeal.ViewModel

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.QuickMeal.DataModels.CartProduct
import com.example.QuickMeal.Firebase.FirebaseCommon
import com.example.QuickMeal.Util.Rsource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class addToCartViewModel@Inject constructor(
    private val firestore: FirebaseFirestore,
    val auth: FirebaseAuth,
    private val firebaseCommon: FirebaseCommon
): ViewModel() {
    private val _addToCart= MutableStateFlow<Rsource<CartProduct>>(Rsource.Unspecified())
    val addToCart=_addToCart.asStateFlow()


    @SuppressLint("SuspiciousIndentation")
    fun addUpdateProductInCart(cartProduct: CartProduct){
        viewModelScope.launch {
            _addToCart.emit(Rsource.Loading())
        }
        firestore.collection("users").document(auth.uid!!).collection("cart").whereEqualTo("product.id",cartProduct.product.id).get().addOnSuccessListener {
            it.documents.let {
                if(it.isEmpty()){
                    addNewProduct(cartProduct)
                }  else {


                    val product =it.first().toObject(CartProduct::class.java)
                    if(product==cartProduct){
                        val documentId=it.first().id
                        increaseQuantity(documentId,cartProduct)
                    } else {

                        addNewProduct(cartProduct)}
                }
            }
        }.addOnFailureListener {
            viewModelScope.launch {
                _addToCart.emit(Rsource.Error(it.message.toString()))
            }

        }
    }

    private fun addNewProduct(cartProduct: CartProduct) {
        firebaseCommon.addProductTocart(cartProduct){ addedProduct,e->
            viewModelScope.launch {


                if (e == null) {

                    _addToCart.emit((Rsource.Success(addedProduct)))
                }
                else{
                    _addToCart.emit((Rsource.Error(e.message.toString())))
                }
            }
        }
    }


    private fun increaseQuantity(docid:String,cartProduct:CartProduct) {
        firebaseCommon.increaseQuantity(docid) { _, e ->
            viewModelScope.launch {


                if (e == null) {

                    _addToCart.emit((Rsource.Success(cartProduct)))
                }
                else{
                    _addToCart.emit((Rsource.Error(e.message.toString())))
                }
            }
        }
    }
}