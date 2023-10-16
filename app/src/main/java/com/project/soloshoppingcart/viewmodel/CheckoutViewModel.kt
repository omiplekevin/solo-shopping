package com.project.soloshoppingcart.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.soloshoppingcart.datamodel.OrderReceipt
import com.project.soloshoppingcart.datamodel.Products
import com.project.soloshoppingcart.datamodel.ProductsItem
import com.project.soloshoppingcart.repository.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(private val resProvider: DataRepository) : ViewModel() {

    val products = MutableLiveData<Products>()
    val productsInCart = MutableLiveData<HashMap<Long, ProductsItem>>()
    val orderReceipt = MutableLiveData<OrderReceipt>()

    private var productsInCartMap = hashMapOf<Long, ProductsItem>()

    fun getData() {
        val inCartProducts = resProvider.readInCart()
        products.postValue(inCartProducts!!)
        updateInCartData(inCartProducts)
    }

    private fun updateInCartData(inCartProducts: Products?) {
        val p = hashMapOf<Long, ProductsItem>()
        if (inCartProducts != null) {
            for (product in inCartProducts.products!!) {
                p.put(product!!.uId, product)
            }
        }
        productsInCartMap = p
        productsInCart.postValue(productsInCartMap)
    }

    fun removeProductToCart(reference: Long) {
        Log.d(javaClass.name, "productsInCartMap before remove: $productsInCartMap")
        Log.d(javaClass.name, "remove: $reference")
        productsInCartMap.remove(reference)
        Log.d(javaClass.name, "productsInCartMap after remove: $productsInCartMap")
        val newProductModel = createInCartModel(productsInCartMap)
        Log.d(javaClass.name, "newProductModel: $newProductModel")
        resProvider.writeInCart(newProductModel)
        products.postValue(newProductModel)
        productsInCart.postValue(productsInCartMap)
    }

    fun createOrderReceipt(name: String, email: String) {
        val productsForCheckout = products.value
        val timeInCheckout = System.currentTimeMillis()
        val latestOrderReceipt = OrderReceipt(name, email, productsForCheckout!!, "ord$timeInCheckout")
        resProvider.writeOrderReceipt(latestOrderReceipt)
        orderReceipt.postValue(latestOrderReceipt)
    }

    fun clearInCartItems() {
        resProvider.writeInCart(Products())
        products.postValue(Products())
        productsInCartMap = hashMapOf()
        productsInCart.postValue(productsInCartMap)
    }

    private fun createInCartModel(productsInCart: HashMap<Long, ProductsItem>): Products {
        val newProductModel = Products()
        var items = mutableListOf<ProductsItem>()
        for (entry in productsInCart.entries) {
            items.add(entry.value)
        }
        newProductModel.products = items.toList()
        return newProductModel
    }


}