package com.project.soloshoppingcart.ui.callback

interface CartEventCallback {

    fun onCartUpdated()

    fun onProceedToCheckout()

    fun onOrderConfirmation()

}