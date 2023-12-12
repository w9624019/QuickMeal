package com.example.agrishop.Data.Order

sealed class OrderStatus(val status:String){
    object Ordered:OrderStatus("Ordered")
    object Cancel:OrderStatus("Cancel")
    object Confirmed:OrderStatus("Confirmed")
    object Shipped:OrderStatus("Shipped")
    object Returned:OrderStatus("Returned")
}

fun getOrderStatus(status: String): OrderStatus {
    return when (status) {
        "Ordered" -> {
            OrderStatus.Ordered
        }
        "Cancel" -> {
            OrderStatus.Cancel
        }
        "Confirmed" -> {
            OrderStatus.Confirmed
        }
        "Shipped" -> {
            OrderStatus.Shipped
        }

        else -> OrderStatus.Returned
    }
}
