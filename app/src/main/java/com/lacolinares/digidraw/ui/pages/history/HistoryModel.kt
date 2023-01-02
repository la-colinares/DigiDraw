package com.lacolinares.digidraw.ui.pages.history

import kotlinx.serialization.Serializable

@Serializable
data class HistoryModel(
    val score: Int,
    val totalQuestion: Int,
    val date: Long = System.currentTimeMillis()
){
    fun getFormattedScoreValue(): String{
        return "$score/$totalQuestion"
    }
}
