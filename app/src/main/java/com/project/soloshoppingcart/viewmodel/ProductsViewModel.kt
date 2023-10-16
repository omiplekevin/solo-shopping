package com.project.soloshoppingcart.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.soloshoppingcart.datamodel.Products
import com.project.soloshoppingcart.datamodel.ProductsItem
import com.project.soloshoppingcart.repository.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(private val resProvider: DataRepository) : ViewModel() {

    val products = MutableLiveData<Products>()
    val productsInCart = MutableLiveData<HashMap<Long, ProductsItem>>()

    private var productsInCartMap = hashMapOf<Long, ProductsItem>()

    fun getData() {
        products.postValue(resProvider.readRawData())
        updateInCartData()
    }

    private fun updateInCartData() {
        val products = resProvider.readInCart()
        val p = hashMapOf<Long, ProductsItem>()
        if (products?.products != null) {
            for (product in products.products!!) {
                p.put(product!!.uId, product)
            }
        }
        productsInCartMap = p
        productsInCart.postValue(productsInCartMap)
    }

    fun addProductToCart(product: ProductsItem) {
        productsInCartMap.put(product.uId, product)
        Log.d(javaClass.name, "productsInCartMap after add: $productsInCartMap")
        resProvider.writeInCart(createInCartModel(productsInCartMap)).also {
            productsInCart.postValue(productsInCartMap)
        }
    }

    fun removeProductToCart(reference: Long) {
        productsInCartMap.remove(reference)
        productsInCart.postValue(productsInCartMap)
    }

    private fun createInCartModel(productsInCart: HashMap<Long, ProductsItem>):Products {
        val newProductModel = Products()
        var items = mutableListOf<ProductsItem>()
        for (entry in productsInCart.entries) {
            items.add(entry.value)
        }
        newProductModel.products = items.toList()
        return newProductModel
    }
}