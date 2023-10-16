package com.project.soloshoppingcart.ui.callback

import com.project.soloshoppingcart.datamodel.OrderReceipt

interface CartEventCallback {

    fun onCartUpdated()

    fun onProceedToCheckout()

    fun onOrderConfirmation(orderReceipt: OrderReceipt)

    fun onReturnToProducts()

}