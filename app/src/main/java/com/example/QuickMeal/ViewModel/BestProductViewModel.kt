package com.example.QuickMeal.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.QuickMeal.DataModels.Firebase.Dish
import com.example.QuickMeal.DataModels.Firebase.DishesInALocation
import com.example.QuickMeal.Util.Rsource
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class BestProductViewModel @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase
):ViewModel() {

    private val _productsData= MutableStateFlow<Rsource<List<Dish>>>(Rsource.Unspecified())
    val productData=_productsData.asStateFlow()






    fun getdata(location:String){

        viewModelScope.launch {
            _productsData.emit(Rsource.Loading())
        }
        val dataList= arrayListOf<Dish>()
        firebaseDatabase.getReference("dishes").orderByChild("Location_name").equalTo(location)
            .addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                        for (data in snapshot.children){
                            Log.d("bestPRoduct", snapshot.children.toString())

                            val data=data.getValue(DishesInALocation::class.java)
                            Log.d("bestPRoduct", data!!.Location_name.toString())
                            for (food in data!!.Available_Dishes){
                                dataList.add(food)
                            }

                        }



                    viewModelScope.launch {
                        _productsData.emit(Rsource.Success(dataList))
                    }
                    } else {

                    }



            }

            override fun onCancelled(error: DatabaseError) {
              viewModelScope.launch {
                  _productsData.emit(Rsource.Error(error.message.toString()))
              }
            }
        })
    }


}