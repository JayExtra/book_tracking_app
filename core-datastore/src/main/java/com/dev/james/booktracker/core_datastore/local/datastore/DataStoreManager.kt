package com.dev.james.booktracker.core_datastore.local.datastore

import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow


interface DataStoreManager {
    suspend fun storeBooleanValue(key : Preferences.Key<Boolean> , value : Boolean)
    suspend fun readBooleanValueOnce(key : Preferences.Key<Boolean> ) : Boolean
    fun readBooleanValueAsFlow(key: Preferences.Key<Boolean>) : Flow<Boolean>

    suspend fun storeIntValue(key : Preferences.Key<Int> , value : Int)
    suspend fun readIntValueOnce(key : Preferences.Key<Int> ) : Int
    fun readIntValueAsFlow(key: Preferences.Key<Int>) : Flow<Int>

    fun getSelectedThemeStream(key : Preferences.Key<Int>) : Flow<Int>
}