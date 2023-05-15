package com.example.coffee_admin.domain

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.asDeferred
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject


class ProductRepository @Inject constructor(
) {

    private val database by lazy {
        Firebase.database
    }

    val storage by lazy {
        Firebase.storage
    }


    @Serializable
    data class RemoteProductModel(
        val name:String,
        val price:String
    )


    suspend fun getProductImageUrl(id:String) : String? {
        val storageRef = storage.reference.child("Images/${id}.png")

        return try {
            storageRef.downloadUrl.asDeferred().await().toString()
        }catch (e:Exception) { null }
    }

    suspend fun getName(id:String) : String? {
        val ref = database.getReference("Coffee")

        val idWithName = ref.get().await().children.map {
            it.key as String to Json.decodeFromString(RemoteProductModel.serializer(),it.value as String).name
        }

        return idWithName.firstOrNull() { it.first == id }?.second
    }

    suspend fun getPrice(id:String) : String? {
        val ref = database.getReference("Coffee")

        val idWithPrice = ref.get().await().children.map {
            it.key as String to Json.decodeFromString(RemoteProductModel.serializer(),it.value as String).price
        }

        return idWithPrice.firstOrNull() { it.first == id }?.second
    }
}