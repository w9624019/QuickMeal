package com.example.QuickMeal.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.QuickMeal.DataModels.Firebase.Dish
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
private val TAG="RestaurantDataViewModel"
@HiltViewModel
class RestaurantDataViewModel @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase
) : ViewModel(){

    private val _restroData= MutableStateFlow<Rsource<List<Dish>>>(Rsource.Unspecified())
    val restroData=_restroData.asStateFlow()




    fun getRestoFoods(restaurant_id:String,location:String){
        Log.d(TAG, restaurant_id)
        val dataList= arrayListOf<Dish>()
        viewModelScope.launch {
            _restroData.emit(Rsource.Loading())
        }

        firebaseDatabase.getReference("locations").orderByChild("name").equalTo(location)
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                            for (data in snapshot.children){
                                val specialData=data.getValue(Location::class.java)
                                Log.d(TAG, specialData.toString())
                                    for (restroIDFinder in specialData!!.restaurants){
                                        Log.d(TAG, "called once")
                                        if(restroIDFinder.id == restaurant_id.toInt()){
                                            Log.d(TAG, "found it")
                                            for (restro in restroIDFinder.dishes){
                                                Log.d(TAG, restro.name)
                                                dataList.add(restro)
                                            }
                                            viewModelScope.launch {
                                                _restroData.emit(Rsource.Success(dataList))
                                            }
                                            return
                                        }
                                    }
                            }

                        Log.d(TAG, dataList.toString())

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                  viewModelScope.launch {
                      _restroData.emit(Rsource.Error(error.message.toString()))
                  }
                }
            })

    }

}