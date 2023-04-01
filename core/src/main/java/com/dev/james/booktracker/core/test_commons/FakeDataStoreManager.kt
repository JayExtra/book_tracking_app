package com.dev.james.booktracker.core.test_commons

import androidx.datastore.preferences.core.Preferences
import com.dev.james.booktracker.core_datastore.local.datastore.DataStoreManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeDataStoreManager : DataStoreManager {
    private val booleansMap = mutableMapOf<Preferences.Key<Boolean> , Boolean>()
    private val stringsMap = mutableMapOf<Preferences.Key<String> , String>()
    private val intMap = mutableMapOf<Preferences.Key<Int> , Int>()

    override suspend fun storeBooleanValue(key: Preferences.Key<Boolean>, value: Boolean) {
        booleansMap[key] = value
    }

    override suspend fun readBooleanValueOnce(key: Preferences.Key<Boolean>): Boolean {
        return booleansMap[key] ?: false
    }

    override fun readBooleanValueAsFlow(key: Preferences.Key<Boolean>): Flow<Boolean> {
        return flow{ emit(booleansMap[key] ?: false) }
    }

    override suspend fun storeIntValue(key: Preferences.Key<Int>, value: Int) {
        intMap[key] = value
    }

    override suspend fun readIntValueOnce(key: Preferences.Key<Int>): Int {
        return intMap[key] ?: 0
    }

    override fun readIntValueAsFlow(key: Preferences.Key<Int>): Flow<Int> {
       return flow { intMap[key] }
    }

    override fun getSelectedThemeStream(key: Preferences.Key<Int>): Flow<Int> {
        return flow { intMap[key] }
    }
}