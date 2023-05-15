package com.example.coffee_admin.presentation.MainScreen.models

import com.example.myapplicationnew.presentation.OrderScreen.models.OrderViewModel

sealed class MainScreenState {

    object Loading : MainScreenState()

    data class OrderList( val orders:List<OrderViewModel>) : MainScreenState()
}