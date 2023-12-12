package com.example.QuickMeal.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.QuickMeal.DataModels.Firebase.Dish
import com.example.QuickMeal.DataModels.Firebase.DishesInALocation
import com.example.QuickMeal.DataModels.Firebase.Location
import com.example.QuickMeal.DataModels.Firebase.Restaurant
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
class GetLocationRestroViewModel @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase
): ViewModel() {

    private val _RestroData= MutableStateFlow<Rsource<List<Restaurant>>>(Rsource.Unspecified())
    val restroData=_RestroData.asStateFlow()






    fun getdata(location:String){

        viewModelScope.launch {
            _RestroData.emit(Rsource.Loading())
        }
        val dataList= arrayListOf<Restaurant>()
        firebaseDatabase.getReference("locations").orderByChild("name").equalTo(location)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        for (data in snapshot.children){
                            Log.d("bestPRoduct", snapshot.children.toString())

                            val data=data.getValue(Location::class.java)
                            Log.d("bestPRoduct", data!!.Location_id.toString())
                            for (food in data!!.restaurants){
                                dataList.add(food)
                            }

                        }



                        viewModelScope.launch {
                            _RestroData.emit(Rsource.Success(dataList))
                        }
                    } else {

                    }



                }

                override fun onCancelled(error: DatabaseError) {
                    viewModelScope.launch {
                        _RestroData.emit(Rsource.Error(error.message.toString()))
                    }
                }
            })
    }


}