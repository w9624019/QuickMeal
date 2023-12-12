package com.example.QuickMeal.Adapters

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.QuickMeal.DataModels.CartProduct
import com.example.QuickMeal.DataModels.Firebase.AvailableLocation
import com.example.QuickMeal.DataModels.Firebase.Dish
import com.example.QuickMeal.Ordering_Fragments.Dish_Details
import com.example.QuickMeal.R
import com.example.QuickMeal.databinding.CartProductBinding
import com.example.QuickMeal.databinding.LocationShowerBinding

class ChangeLocationAdapter: RecyclerView.Adapter<ChangeLocationAdapter.AddressViewHoler>() {

    inner class AddressViewHoler(val binding: LocationShowerBinding)
        : RecyclerView.ViewHolder(binding.root){
        fun bind(cartProduct: AvailableLocation){
          binding.locationname.text=cartProduct.name
        }
    }
    val diffCallback =object : DiffUtil.ItemCallback<AvailableLocation>(){
        override fun areItemsTheSame(oldItem: AvailableLocation, newItem: AvailableLocation): Boolean {
            return oldItem.name==newItem.name
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: AvailableLocation, newItem: AvailableLocation): Boolean {
            return oldItem==newItem
        }
    }

    val differ = AsyncListDiffer(this,diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHoler {
        return AddressViewHoler(
            LocationShowerBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            )
        )
    }

    override fun onBindViewHolder(holder: AddressViewHoler, position: Int) {
        val product=differ.currentList[position]
        holder.bind(product)

        Log.d("locationAdapter", differ.currentList[1].name.toString())

        Log.d("locationAdapter",differ.currentList[position].name )

holder.itemView.setOnClickListener {
    onClick?.invoke(product)
}


    }

    override fun getItemCount(): Int {
        return  differ.currentList.size
    }

    var onClick:((AvailableLocation)->Unit)?=null



    var onPlusClick:((CartProduct)->Unit)?=null
    var onMinusClick:((CartProduct)->Unit)?=null
}