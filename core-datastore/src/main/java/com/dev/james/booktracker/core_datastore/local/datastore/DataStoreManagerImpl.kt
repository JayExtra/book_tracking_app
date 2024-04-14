package com.dev.james.booktracker.core_datastore.local.datastore

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.dev.james.booktracker.core.common_models.BaseBookData
import com.dev.james.booktracker.core.datastore.proto.PendingReadingListItems
import com.dev.james.booktracker.core.datastore.proto.WishlistItems
import com.dev.james.booktracker.core_datastore.local.datastore.proto.PendingReadingListItemsSerializer
import com.dev.james.booktracker.core_datastore.local.datastore.proto.WishListSerializer
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.collections.immutable.mutate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class DataStoreManagerImpl @Inject constructor(
    @ApplicationContext private val context : Context
) : DataStoreManager {
    companion object {
        private val Context.datastore : DataStore<Preferences> by preferencesDataStore(name = Constants.STORE_NAME)
        private val Context.protoDatastore by dataStore(fileName = Constants.FILE_NAME , WishListSerializer )
        private val Context.readingListProtoDataStore by dataStore(fileName = Constants.READING_LIST_FILE_NAME , PendingReadingListItemsSerializer )
        const val TAG = "DataStoreManagerImpl"
    }

    override suspend fun storeStringValue(key: Preferences.Key<String>, value: String) {
        context.datastore.edit {
            it[key] = value
        }
    }

    override suspend fun storeBooleanValue(key: Preferences.Key<Boolean>, value: Boolean) {
        context.datastore.edit {
            it[key] = value
        }
    }

    override suspend fun readBooleanValueOnce(key: Preferences.Key<Boolean>): Boolean {
        return context.datastore.data.first()[key] ?: false
    }

    override fun readBooleanValueAsFlow(key: Preferences.Key<Boolean>): Flow<Boolean> {
        return context.datastore.data.map {
            it[key] ?: Constants.DEFAULT_VALUE
        }.catch { exception ->
            if(exception is IOException){
                Timber.e( "Datastore read boolean value error io exception =>" + exception.message)
            }
            Timber.e( "Datastore read boolean value error =>" + exception.localizedMessage)

            Constants.DEFAULT_VALUE
        }
    }

    override suspend fun storeIntValue(key: Preferences.Key<Int>, value: Int) {
        context.datastore.edit {
            it[key] = value
        }
    }

    override suspend fun readIntValueOnce(key: Preferences.Key<Int>): Int {
        return context.datastore.data.first()[key] ?: 0
    }

    override fun readIntValueAsFlow(key: Preferences.Key<Int>): Flow<Int> {
        return context.datastore.data.map {
            it[key] ?: 0
        }.catch { exception ->
            if(exception is IOException){
                Timber.e( "Datastore read string value error io exception =>" + exception.message)
            }
            Timber.e( "Datastore read string value error =>" + exception.localizedMessage)
        }
    }

    override suspend fun readStringValueOnce(key: Preferences.Key<String>): String {
        return context.datastore.data.first()[key] ?: " "
    }

    override fun getSelectedThemeStream(key: Preferences.Key<Int>): Flow<Int> {
        return context.datastore.data.map {
            it[key] ?: AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }.catch { exception ->
            if(exception is IOException){
                Timber.e( "Datastore reading theme value error io exception =>" + exception.message)
            }
            Timber.e( "Datastore read theme value error =>" + exception.localizedMessage)
        }
    }

    override suspend fun addToWishList(wishlistItem: BaseBookData) {
        context.protoDatastore.updateData { wishlistItems ->
            wishlistItems.copy(
                wishlist = wishlistItems.wishlist.mutate {
                    it.add(wishlistItem)
                }
            )
        }
    }

    override fun getWishList(): Flow<WishlistItems> {
        return context.protoDatastore.data
    }

    override suspend fun removeFromWishList(bookId: String) {
        Timber.tag(TAG).d("removing $bookId")
        context.protoDatastore.updateData { wishlistItems ->
            val list = wishlistItems.wishlist
            Timber.tag(TAG).d("actual list ${list.toString()}")
            val book = list.find {
                it.id == bookId
            }
            val bookIndex = list.indexOf(book)
            Timber.tag(TAG).d("book found ${book.toString()} at index : $bookIndex")
            wishlistItems.copy(
                wishlist = list.mutate {
                    it.remove(book)
                }
            )
        }
    }

    override fun pendingReadingListItems(): Flow<List<PendingReadingListItems>> {
        return context.readingListProtoDataStore.data.map { it.pendingItems }
    }


    override suspend fun removeFromPendingList(readingListId: String, bookId: String) {
        context.readingListProtoDataStore.updateData { pendingReadingList ->

            val readingList = pendingReadingList.pendingItems.find { item ->
                item.readingListId == readingListId
            }
            val readingListIndex = pendingReadingList.pendingItems.indexOf(readingList)


            val book = readingList?.books?.find { it.id == bookId }
            val bookIndex = readingList?.books?.indexOf(book)!!



            val newBooksList = readingList.books.mutate {
                it.removeAt(bookIndex)
            }
            val newReadingList = readingList.copy(
                books = newBooksList
            )

            val newPendingItemList = pendingReadingList.pendingItems.mutate {
                it.removeAt(readingListIndex)
            }.mutate {
                it.add(newReadingList)
            }
            pendingReadingList.copy(
                pendingItems = newPendingItemList
            )

        }
    }

    override suspend fun addToPendingReadingList(readingListId: String, book: BaseBookData) {
        context.readingListProtoDataStore.updateData { pendingReadingList ->

            val readingList = pendingReadingList.pendingItems.find { item ->
                item.readingListId == readingListId
            }
            val readingListIndex = pendingReadingList.pendingItems.indexOf(readingList)


            val newBooksList = readingList?.books?.mutate {
                it.add(book)
            }
            val newReadingList = newBooksList?.let {
                readingList.copy(
                    books = it
                )
            }!!

            val newPendingItemList = pendingReadingList.pendingItems.mutate {
                it.removeAt(readingListIndex)
            }.mutate {
                it.add(newReadingList)
            }
            pendingReadingList.copy(
                pendingItems = newPendingItemList
            )
        }
    }

}