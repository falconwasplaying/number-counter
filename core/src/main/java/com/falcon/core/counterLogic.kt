package com.falcon.core

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private val counterKey = intPreferencesKey("counter_value")

fun increase(counter: Int, scope: CoroutineScope, dataStore: DataStore<Preferences>): Int {
    val newValue = counter + 1
    scope.launch {
        dataStore.edit { it[counterKey] = newValue }
    }
    return newValue
}

fun decrease(counter: Int, scope: CoroutineScope, dataStore: DataStore<Preferences>): Int {
    val newValue = counter - 1
    scope.launch {
        dataStore.edit { it[counterKey] = newValue }
    }
    return newValue
}

fun reset(scope: CoroutineScope, dataStore: DataStore<Preferences>): Int {
    scope.launch {
        dataStore.edit { it[counterKey] = 0 }
    }
    return 0
}
