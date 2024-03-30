package com.dev.james.booktracker.core_datastore.local.datastore.proto

import androidx.datastore.core.Serializer
import com.dev.james.booktracker.core.datastore.proto.WishlistItems
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import timber.log.Timber
import java.io.InputStream
import java.io.OutputStream

object WishListSerializer : Serializer<WishlistItems> {

    const val TAG = "WishListSerializer"
    override val defaultValue: WishlistItems
        get() = WishlistItems()

    override suspend fun readFrom(input: InputStream): WishlistItems {
        return try {
            Json.decodeFromString(
                deserializer = WishlistItems.serializer() ,
                string = input.readBytes().decodeToString()
            )
        }catch (e : SerializationException){
            Timber.tag(TAG).e("Error: ${e.stackTrace}")
            defaultValue
        }
    }

    override suspend fun writeTo(t: WishlistItems, output: OutputStream) {
        output.write(
            Json.encodeToString(
                serializer = WishlistItems.serializer() ,
                value = t
            ).encodeToByteArray()
        )
    }
}