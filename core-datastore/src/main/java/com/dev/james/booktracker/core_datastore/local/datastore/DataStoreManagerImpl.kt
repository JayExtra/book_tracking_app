package com.dev.james.booktracker.core_datastore.local.datastore

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
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


}