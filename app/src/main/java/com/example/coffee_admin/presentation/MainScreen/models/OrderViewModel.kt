package com.example.myapplicationnew.presentation.OrderScreen.models

data class OrderViewModel(
    val id:String,
    val photoImage:String?,
    val isReady:Boolean,
    val name:String?,
    val volume:Float,
    val productId:String
)
