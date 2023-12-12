package com.example.QuickMeal.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.QuickMeal.DataModels.Firebase.AvailableLocation
import com.example.QuickMeal.Util.Rsource
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class GetLocationViewModel@Inject constructor(
    private val firebaseDatabase: FirebaseDatabase
) : ViewModel() {
    private val _locationname= MutableStateFlow<Rsource<List<AvailableLocation>>>(Rsource.Unspecified())
    val location=_locationname.asStateFlow()

init {
    getlocation()
}

    fun getlocation(){
        val datList=arrayListOf<AvailableLocation>()
        viewModelScope.launch {
            _locationname.emit(Rsource.Loading())
        }
        firebaseDatabase.getReference("location_names").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for (data in snapshot.children){
                        Log.d("ViewMODel", data.toString())
                        val names=data.getValue(AvailableLocation::class.java)
                            datList.add(names!!)
                    }
                    Log.d("ViewMODels", datList.size.toString())

                    viewModelScope.launch {
                        _locationname.emit(Rsource.Success(datList))
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
              viewModelScope.launch {
                  _locationname.emit(Rsource.Error(error.message.toString()))
              }
            }
        })
    }


}