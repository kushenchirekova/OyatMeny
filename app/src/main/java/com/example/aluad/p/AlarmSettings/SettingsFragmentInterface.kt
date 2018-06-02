package com.example.aluad.p.AlarmSettings

import android.content.Context
import android.view.View

interface SettingsFragmentInterface {
    fun saveSettingsInSharedPreferences()
    fun initializeDependOnViewVariables(view : View?)
    var fragmentContext: Context
}