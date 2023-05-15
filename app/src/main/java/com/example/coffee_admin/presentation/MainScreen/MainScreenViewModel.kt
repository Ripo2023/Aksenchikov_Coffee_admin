package com.example.coffee_admin.presentation.MainScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coffee_admin.domain.OrderRepository
import com.example.coffee_admin.presentation.MainScreen.models.MainScreenState
import com.example.coffee_admin.presentation.MainScreen.models.ProductInfoDialogState
import com.example.myapplicationnew.presentation.OrderScreen.models.OrderViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val orderRepository: OrderRepository
) : ViewModel() {

    val mainScreenState: MutableStateFlow<MainScreenState> = MutableStateFlow(MainScreenState.Loading)

    val productInfoDialogState: MutableStateFlow<ProductInfoDialogState> = MutableStateFlow(ProductInfoDialogState.Hide)

    val isRefresh = MutableStateFlow(false)

    fun showProductInfoDialog(order:OrderViewModel) {
        if(isRefresh.value) return
        productInfoDialogState.update { ProductInfoDialogState.Show(order) }
    }

    fun hideProductInfoDialog() {
        productInfoDialogState.update { ProductInfoDialogState.Hide }
    }

    fun reload() {
        viewModelScope.launch {
            isRefresh.update { true }
            mainScreenState.update { MainScreenState.OrderList(orderRepository.loadNotExecutedOrdersForView()) }
            isRefresh.update { false }
        }
    }

    fun markCompleted(orderId:String) {
        viewModelScope.launch(Dispatchers.IO) {
            orderRepository.markCompleted(orderId)


            reload()

            hideProductInfoDialog()
        }
    }

    init {
        viewModelScope.launch {
            mainScreenState.update { MainScreenState.OrderList(orderRepository.loadNotExecutedOrdersForView()) }
        }
    }
}