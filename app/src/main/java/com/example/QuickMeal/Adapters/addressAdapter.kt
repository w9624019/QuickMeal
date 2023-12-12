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

class addressAdapter: RecyclerView.Adapter<addressAdapter.AddressViewHolder>() {
    inner class AddressViewHolder( val binding: AddressRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Address, isSelected: Boolean) {
            Log.d("address Adapter", product.toString())
binding.apply {
    buttonAddress.text=product.addressTitle
    if(isSelected){
        buttonAddress.background=ColorDrawable(itemView.context.resources.getColor(R.color.g_blue))
    } else {
        buttonAddress.background=ColorDrawable(itemView.context.resources.getColor(R.color.white))

    }
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
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        return AddressViewHolder(
            AddressRvItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }
    var selectedAddress=0
    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.bind(product ,selectedAddress==position)

        holder.binding.buttonAddress.setOnClickListener {
            if(selectedAddress>=0){

            notifyItemChanged(selectedAddress)
                selectedAddress=holder.adapterPosition
                notifyItemChanged(selectedAddress)
                OnClick?.invoke(product)
            }
        }

//        holder.itemView.setOnClickListener {
//            navigate(product, it)
//        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
    var OnClick :((Address)->Unit)?=null
}
//
//
//    private fun navigate(product: Address, view: View) {
//
//        val intent= Intent(view.context, Product_Details::class.java).apply {
//            putExtra("id",product.id)
//            putExtra("category",product.category)
//            putExtra("Name",product.product_name)
//            putExtra("img",product.img)
//            putExtra("Des",product.description)
//            putExtra("prices",product.prices.toString())
//            putExtra("what",product.what)
//
//        }
//        view.context.startActivity(intent)
//
//    }

//}