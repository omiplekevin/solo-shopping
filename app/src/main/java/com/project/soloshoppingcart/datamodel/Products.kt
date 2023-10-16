package com.project.soloshoppingcart.datamodel

import com.google.gson.annotations.SerializedName

data class Products(

	@field:SerializedName("products")
	var products: List<ProductsItem?>? = null

)

data class ProductsItem(

	@field:SerializedName("bgColor")
	var bgColor: String? = null,

	@field:SerializedName("price")
	var price: String? = null,

	@field:SerializedName("name")
	var name: String? = null,

	@field:SerializedName("id")
	var id: String? = null,

	@field:SerializedName("category")
	var category: String? = null,

	@field:SerializedName("uId")
	var uId: Long = 0L
)
