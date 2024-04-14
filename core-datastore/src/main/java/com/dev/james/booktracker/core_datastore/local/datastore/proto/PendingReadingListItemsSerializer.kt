package com.dev.james.booktracker.core_datastore.local.datastore.proto

import androidx.datastore.core.Serializer
import com.dev.james.booktracker.core.datastore.proto.PendingReadingList
import com.dev.james.booktracker.core.datastore.proto.PendingReadingListItems
import com.dev.james.booktracker.core.datastore.proto.WishlistItems
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import timber.log.Timber
import java.io.InputStream
import java.io.OutputStream

object PendingReadingListItemsSerializer : Serializer<PendingReadingList> {

    const val TAG = "PendingReadingList"
    override val defaultValue: PendingReadingList
        get() = PendingReadingList()

    override suspend fun readFrom(input: InputStream): PendingReadingList {
        return try {
            Json.decodeFromString(
                deserializer = PendingReadingList.serializer() ,
                string = input.readBytes().decodeToString()
            )
        }catch (e : SerializationException){
            Timber.tag(TAG).e("Error: ${e.stackTrace}")
            defaultValue
        }
    }

    override suspend fun writeTo(t: PendingReadingList, output: OutputStream) {
        output.write(
            Json.encodeToString(
                serializer = PendingReadingList.serializer() ,
                value = t
            ).encodeToByteArray()
        )
    }
}