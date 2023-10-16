package com.project.soloshoppingcart.datamodel

data class OrderReceipt(
    var name: String,
    var email: String,
    var products: Products,
    var orderId: String
)
