package com.example.coffee_admin.domain

import com.example.myapplicationnew.models.OrderModel
import com.example.myapplicationnew.presentation.OrderScreen.models.OrderViewModel
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.asDeferred
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

//репозиторий заказов
@Singleton
class OrderRepository @Inject constructor(
    private val productRepository: ProductRepository
) {
    @Serializable
     data class RemoteOrderModel(
        val productId:String,
        val volumeCof:Float,
        val isReady:Boolean
     )

    val database by lazy {
        Firebase.database
    }




    private suspend fun loadNotExecutedOrders() : List<OrderModel> {
        val ref = database.getReference("Orders")

        val allUserOrders = ref.get().asDeferred().await().children

        val finalList = mutableListOf<OrderModel>()

        allUserOrders.forEach {
            it.children.forEach {
                val remoteModel = Json.decodeFromString(RemoteOrderModel.serializer(),(it.value as String))

                finalList.add(OrderModel(it.key as String,remoteModel.productId, remoteModel.volumeCof,remoteModel.isReady))
            }
        }

        return finalList
    }


    suspend fun loadNotExecutedOrdersForView() : List<OrderViewModel> {
        val orders = loadNotExecutedOrders()

        return orders.map {
            OrderViewModel(it.id,productRepository.getProductImageUrl(it.productId),it.isReady,productRepository.getName(it.productId),it.volumeCof,it.productId)
        }.filter { !it.isReady }
    }

    suspend fun markCompleted(orderId: String) {
        val allUserOrders = database.getReference("Orders").get().asDeferred().await().children

        allUserOrders.forEach {
            it.children.forEach {
                val remoteModel = Json.decodeFromString(RemoteOrderModel.serializer(),(it.value as String))

                if(it.key == orderId) {
                    it.ref.setValue(Json.encodeToString(RemoteOrderModel.serializer(),remoteModel.copy(isReady = true)))
                }
            }
        }
    }
}