package com.project.soloshoppingcart.repository

import android.content.Context
import com.google.gson.Gson
import com.project.soloshoppingcart.R
import com.project.soloshoppingcart.datamodel.Products
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.lang.StringBuilder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
interface DataRepository {
    fun readRawData(): Products?
    fun writeInCart(products: Products)
    fun readInCart(): Products?
}

class DataRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : DataRepository {

    override fun readRawData(): Products? {
        val inputStream = context.resources.openRawResource(R.raw.products)
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        val content = StringBuilder()
        try {
            var line = bufferedReader.readLine()
            while (line != null) {
                content.append(line)
                line = bufferedReader.readLine()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
        return Gson().fromJson(content.trim().toString(), Products::class.java)
    }

    override fun readInCart(): Products? {
        try {
            val raw = File(context.filesDir, "inCart.json")
                .inputStream()
                .readBytes()
                .toString(Charsets.UTF_8)
            return Gson().fromJson(raw, Products::class.java)
        } catch (e: Exception) {
            return Products()
        }
    }

    override fun writeInCart(products: Products) {
        val file = File(context.filesDir, "inCart.json")
        val b = file.createNewFile()
        file.writeText(Gson().toJson(products))
    }
}
