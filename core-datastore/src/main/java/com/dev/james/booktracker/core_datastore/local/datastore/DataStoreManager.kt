package com.dev.james.booktracker.core_datastore.local.datastore

import androidx.datastore.preferences.core.Preferences
import com.dev.james.booktracker.core.common_models.BaseBookData
import com.dev.james.booktracker.core.datastore.proto.WishlistItems
import kotlinx.coroutines.flow.Flow


interface DataStoreManager {

    suspend fun storeStringValue(key : Preferences.Key<String> , value : String)
    suspend fun storeBooleanValue(key : Preferences.Key<Boolean> , value : Boolean)
    suspend fun readBooleanValueOnce(key : Preferences.Key<Boolean> ) : Boolean
    fun readBooleanValueAsFlow(key: Preferences.Key<Boolean>) : Flow<Boolean>

    suspend fun storeIntValue(key : Preferences.Key<Int> , value : Int)
    suspend fun readIntValueOnce(key : Preferences.Key<Int> ) : Int
    fun readIntValueAsFlow(key: Preferences.Key<Int>) : Flow<Int>

    suspend fun readStringValueOnce(key : Preferences.Key<String>) : String

    fun getSelectedThemeStream(key : Preferences.Key<Int>) : Flow<Int>

    suspend fun addToWishList(wishlistItem: BaseBookData)
    fun getWishList() : Flow<WishlistItems>

    suspend fun removeFromWishList(bookId : String)
}