package com.yuriihetsko.aurachallenge.data

import android.content.Context


//todo use room database instead in case of having more time
object BootEventStorage {
    private const val PREFS_NAME = "boot_prefs"
    private const val KEY_BOOT_TIMES = "boot_times"

    fun addBootEvent(context: Context, timestamp: Long) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val bootTimes = prefs.getStringSet(KEY_BOOT_TIMES, mutableSetOf())?.toMutableSet()
        bootTimes?.add(timestamp.toString())
        prefs.edit().putStringSet(KEY_BOOT_TIMES, bootTimes).apply()
    }

    fun getBootEvents(context: Context): List<Long> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val bootTimes = prefs.getStringSet(KEY_BOOT_TIMES, mutableSetOf())?.map { it.toLong() }
        return bootTimes?.sorted() ?: listOf()
    }
}