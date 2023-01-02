package com.lacolinares.digidraw.ui.pages.history

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class HistoryDataStore(private val context: Context) {

    suspend fun setHistory(historyModel: HistoryModel) {
        context.dataStore.edit { prefs ->
            val currentHistory = prefs[KEY_HISTORY]
            val historyList = currentHistory?.let { Json.decodeFromString<List<HistoryModel>>(it) } ?: emptyList()
            val updatedHistory = historyList.toMutableList()
            updatedHistory.add(historyModel)

            prefs[KEY_HISTORY] = Json.encodeToString(updatedHistory)
        }
    }

    val highestScore: Flow<Int>
        get() = context.dataStore.data.map { pref ->
            val history = pref[KEY_HISTORY]
            if (history != null) {
                val scores = Json.decodeFromString<List<HistoryModel>>(history)
                scores.maxOf { it.score }
            } else {
                0
            }
        }

    suspend fun clearHistory() {
        context.dataStore.edit { prefs ->
            prefs[KEY_HISTORY] = Json.encodeToString(emptyList<HistoryModel>())
        }
    }

    val history: Flow<List<HistoryModel>>
        get() = context.dataStore.data.map {
            val history = it[KEY_HISTORY]
            if (history != null) {
                Json.decodeFromString<List<HistoryModel>>(history).asReversed()
            } else {
                emptyList()
            }
        }

    companion object {
        private const val DATA_STORE_NAME = "history_prefs"
        private val Context.dataStore by preferencesDataStore(name = DATA_STORE_NAME)

        private val KEY_HISTORY = stringPreferencesKey("history")
    }
}