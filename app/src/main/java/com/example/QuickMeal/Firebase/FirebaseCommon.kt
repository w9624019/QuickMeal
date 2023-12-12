package com.example.QuickMeal.Firebase

import com.example.QuickMeal.DataModels.CartProduct
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject

class FirebaseCommon(
    private val firestore: FirebaseFirestore,
    private val auth:FirebaseAuth
) {

    private val cartCollection=firestore.collection("users").document(auth.uid!!).collection("cart")

    fun addProductTocart(cartProduct: CartProduct, onResult:(CartProduct?, Exception?)->Unit){
        cartCollection.document().set(cartProduct)
            .addOnSuccessListener {
                onResult(cartProduct, null)
            }.addOnFailureListener {
                onResult(null,it)
            }
    }

    fun increaseQuantity(documentId:String,onResult:(String?,Exception?)->Unit){
        firestore.runTransaction{transition->
            val docRedf=cartCollection.document(documentId)
            val doc=transition.get(docRedf)
            val productObj =doc.toObject(CartProduct::class.java)
            productObj?.let {
                val newQuantity=it.quantity+1
                val newProduct=it.copy(quantity = newQuantity)
                transition.set(docRedf,newProduct)
            }
        }.addOnSuccessListener {
            onResult(documentId,null)
        }.addOnFailureListener {
            onResult(null,it)
        }
    }


    fun decreaseQuantity(documentId:String,onResult:(String?,Exception?)->Unit){
        firestore.runTransaction{transition->
            val docRedf=cartCollection.document(documentId)
            val doc=transition.get(docRedf)
            val productObj =doc.toObject(CartProduct::class.java)
            productObj?.let {
                val newQuantity=it.quantity-1
                val newProduct=it.copy(quantity = newQuantity)
                transition.set(docRedf,newProduct)
            }
        }.addOnSuccessListener {
            onResult(documentId,null)
        }.addOnFailureListener {
            onResult(null,it)
        }
    }


    enum class QuantityChanging{
        INCREASE,DECREASE
    }

}