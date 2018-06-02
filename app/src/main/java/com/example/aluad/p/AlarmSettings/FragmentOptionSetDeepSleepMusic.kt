package com.example.aluad.p.AlarmSettings

import android.app.Fragment
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.aluad.p.R
import com.mrkostua.mathalarm.KotlinActivitiesInterface


class FragmentOptionSetDeepSleepMusic : Fragment(), KotlinActivitiesInterface, SettingsFragmentInterface {
    override lateinit var fragmentContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentContext = activity.applicationContext
        initializeDependOnContextVariables(fragmentContext)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_option_set_deep_sleep_music, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeDependOnViewVariables(view)
    }

    override fun initializeDependOnContextVariables(context: Context) {
    }

    override fun saveSettingsInSharedPreferences() {
    }

    override fun initializeDependOnViewVariables(view: View?) {
    }


}