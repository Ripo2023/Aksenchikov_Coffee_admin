package com.example.coffee_admin.presentation.MainScreen.models

import com.example.myapplicationnew.presentation.OrderScreen.models.OrderViewModel

sealed class ProductInfoDialogState {

    object Hide : ProductInfoDialogState()

    data class Show(val order:OrderViewModel) : ProductInfoDialogState()
}