package com.dev.james.booktracker.core_datastore.local.datastore

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey


object DataStorePreferenceKeys {
    val HAS_FINISHED_ON_BOARDING : Preferences.Key<Boolean> =
        booleanPreferencesKey("has_finished_on_boarding")

    val CURRENT_THEME_SELECTED : Preferences.Key<Int> =
        intPreferencesKey("current_selected_theme")

    val CURRENT_ACTIVE_BOOK_ID : Preferences.Key<String> =
        stringPreferencesKey("current_active_book_id")

    val CURRENT_STREAK : Preferences.Key<Int> =
        intPreferencesKey("current_streak")
}