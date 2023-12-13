package com.example.QuickMeal.Adapters

import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.QuickMeal.databinding.OrderShowerRecyclerviewBinding

import com.example.agrishop.Data.Order.Order
import java.util.Calendar
import java.util.Date
import kotlin.random.Random


class OrderAdapter : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {
    inner class OrderViewHolder( val binding: OrderShowerRecyclerviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(order: Order) {
            Log.d("Order Adapter", order.toString())
            binding.apply {
                tvOrderId.text = "Your Order id - "+order.orderId.toString()
                tvOrderDate.text ="Your Order date - "+ order.date.toString() // Corrected line


                // Format the new date as a string
                val randomNumber = Random.nextInt(1, 60)
                    deliveryDate.text="You will get your food in ${randomNumber} minutes "

            }
        }

    }

    private val diffCallback = object : DiffUtil.ItemCallback<Order>() {
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem.products == newItem.products
        }

        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        return OrderViewHolder(
            OrderShowerRecyclerviewBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.bind(product )



//        holder.itemView.setOnClickListener {
//            navigate(product, it)
//        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

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