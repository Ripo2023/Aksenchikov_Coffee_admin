package com.example.myapplicationnew.models

import androidx.annotation.IdRes
import androidx.compose.runtime.Composable

data class ProductModel(
    val id:Int,
    val name:String,
    @IdRes val image:Int,
    val price:Int
) {
    companion object {
        val coffeeProduct : List<ProductModel>
            @Composable get() = listOf(
                ProductModel(
                    id = 0,
                    name = "Каппучино",
                    image = TODO(),
                    price = 180
                ),
                ProductModel(
                    id = 1,
                    name = "Экспрессо",
                    image = TODO(),
                    price = 180
                ),
                ProductModel(
                    id = 2,
                    name = "Латте",
                    image = TODO(),
                    price = 180
                ),
                ProductModel(
                    id = 3,
                    name = "Рафф",
                    image = TODO(),
                    price = 180
                )

            )
    }
}
