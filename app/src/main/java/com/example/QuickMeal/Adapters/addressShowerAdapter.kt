package com.example.QuickMeal.Adapters

import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.QuickMeal.DataModels.Address
import com.example.QuickMeal.R
import com.example.QuickMeal.databinding.AddressRvItemBinding
import com.example.QuickMeal.databinding.AddressShowerRvBinding

class addressShowerAdapter : RecyclerView.Adapter<addressShowerAdapter.AddressShowerViewHolder>() {
    inner class AddressShowerViewHolder( val binding: AddressShowerRvBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Address, isSelected: Boolean) {
            Log.d("address Adapter", product.toString())
            binding.apply {
              binding.WhatAddress.text=product.addressTitle
                binding.textView16.text="${product.street},${product.City},${product.State}"
            }

        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<Address>() {
        override fun areItemsTheSame(oldItem: Address, newItem: Address): Boolean {
            return oldItem.addressTitle == newItem.addressTitle
        }

        override fun areContentsTheSame(oldItem: Address, newItem: Address): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressShowerViewHolder {
        return AddressShowerViewHolder(
            AddressShowerRvBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }
    var selectedAddress=0
    override fun onBindViewHolder(holder: AddressShowerViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.bind(product ,selectedAddress==position)



//        holder.itemView.setOnClickListener {
//            navigate(product, it)
//        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
    var OnClick :((Address)->Unit)?=null
}