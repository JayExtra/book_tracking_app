package com.dev.james.booktracker.core_datastore.local.datastore

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey


object DataStorePreferenceKeys {
    val HAS_FINISHED_ON_BOARDING : Preferences.Key<Boolean> =
        booleanPreferencesKey("has_finished_on_boarding")
}