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
class OrdConfirmationViewModel @Inject constructor(private val resProvider: DataRepository) : ViewModel() {
    val products = MutableLiveData<Products>()
}