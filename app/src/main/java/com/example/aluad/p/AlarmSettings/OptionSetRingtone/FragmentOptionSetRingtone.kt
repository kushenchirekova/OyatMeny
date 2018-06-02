package com.example.aluad.p.AlarmSettings.OptionSetRingtone

import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import com.example.aluad.p.AlarmSettings.SettingsFragmentInterface
import com.example.aluad.p.R
import com.mrkostua.mathalarm.AlarmSettings.OptionSetRingtone.*

import com.mrkostua.mathalarm.KotlinActivitiesInterface

import com.mrkostua.mathalarm.Tools.NotificationTools
import com.mrkostua.mathalarm.Tools.ShowLogs
import kotlinx.android.synthetic.main.fragment_option_set_ringtone.*


class FragmentOptionSetRingtone : Fragment(), SettingsFragmentInterface, KotlinActivitiesInterface {
    override lateinit var fragmentContext: Context
    private val TAG = this.javaClass.simpleName
    private var positionOfPlayingButtonItem = 0

    private lateinit var ringtonePopulationList: ArrayList<RingtoneObject>
    private lateinit var notificationTools: NotificationTools
    private lateinit var ringtonesRecycleViewAdapter: RingtonesRecycleViewAdapter
    private lateinit var mediaPlayerHelper: MediaPlayerHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        ShowLogs.log(TAG, "onCreate")
        super.onCreate(savedInstanceState)
        fragmentContext = activity.applicationContext
        initializeDependOnContextVariables(fragmentContext)

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_option_set_ringtone, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        ShowLogs.log(TAG, "onViewCreated")
        super.onViewCreated(view, savedInstanceState)
        initializeDependOnViewVariables(view)

    }

    override fun onPause() {
        super.onPause()
        mediaPlayerHelper.stopRingtone()
    }

    override fun onResume() {
        super.onResume()
        ringtonePopulationList.forEachIndexed { index, ringtoneObject ->
            if (index == positionOfPlayingButtonItem) {
                ringtoneObject.isPlaying = false
                return@forEachIndexed
            }
        }
        ringtonesRecycleViewAdapter.notifyItemChanged(positionOfPlayingButtonItem)
    }

    override fun initializeDependOnContextVariables(context: Context) {
        notificationTools = NotificationTools(fragmentContext)
        mediaPlayerHelper = MediaPlayerHelper(fragmentContext)
        ringtonePopulationList = getRingtonesForPopulation()

    }

    override fun initializeDependOnViewVariables(view: View?) {
        rvListOfRingtones.layoutManager = LinearLayoutManager(fragmentContext)
        rvListOfRingtones.itemAnimator = DefaultItemAnimator()
        rvListOfRingtones.addItemDecoration(DividerItemDecoration(fragmentContext, LinearLayoutManager.VERTICAL))

        ringtonesRecycleViewAdapter = RingtonesRecycleViewAdapter(fragmentContext, ringtonePopulationList, SetRingtoneClickListener())
        rvListOfRingtones.adapter = ringtonesRecycleViewAdapter

    }

    override fun saveSettingsInSharedPreferences() {
        //saving is implemented in OnClickListener for CheckBox and ImageButton
    }

    private fun isRingtoneImagePlay(view: ImageView): Boolean =
            view.contentDescription == fragmentContext.resources.getString(R.string.contentDescription_playRingtone)

    private fun getRingtonesForPopulation(): ArrayList<RingtoneObject> {
        val ringtonesList = ArrayList<RingtoneObject>()
        ringtonesList.add(RingtoneObject("ringtone_mechanic_clock", 2))
        ringtonesList.add(RingtoneObject("ringtone_energy", 1))
        ringtonesList.add(RingtoneObject("ringtone_loud", 3))
        ringtonesList.addAll(RingtoneManagerHelper.getDefaultAlarmRingtonesList(fragmentContext))

        ringtonesList.sortByDescending { ringtoneObject -> ringtoneObject.rating }
        return ringtonesList
    }

    private fun addRingtoneFromExternalPath(): Int {
        val openFileActionIntent: Intent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            openFileActionIntent = Intent.makeMainSelectorActivity(Intent.ACTION_MAIN, Intent.CATEGORY_APP_MUSIC)
            openFileActionIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        } else {
            openFileActionIntent = Intent("android.intent.action.MUSIC_PLAYER")
        }
        startActivity(openFileActionIntent)



        return 0
    }

    inner class SetRingtoneClickListener : RingtoneClickListeners {

        override fun checkBoxClickListener(view: CheckBox, position: Int) {
            ShowLogs.log(TAG, "getRingtoneClickListeners checkBoxClickListener position: " + position)
            if (view.isChecked) {
                setClickedIndexToTrue({ it.isChecked = true }, { it.isChecked = false },
                        { it.isChecked }, position)

            } else {
                setAllIndexesToFalse({ it.isChecked }, { it.isChecked = false }, position)

            }
        }

        override fun imageButtonClickListener(view: ImageButton, position: Int) {
            ShowLogs.log(TAG, "getRingtoneClickListeners imageButtonClickListener position: " + position)
            when {
                isRingtoneImagePlay(view) -> {
                    positionOfPlayingButtonItem = position
                    if (ringtonePopulationList[position].uri == null) {
                        mediaPlayerHelper.playRingtoneFromStringResource(ringtonePopulationList[position].name)
                    } else {
                        mediaPlayerHelper.playRingtoneFromUri(ringtonePopulationList[position].uri!!)

                    }
                    setClickedIndexToTrue({ it.isPlaying = true }, { it.isPlaying = false },
                            { it.isPlaying }, position)

                }
                !isRingtoneImagePlay(view) -> {
                    mediaPlayerHelper.stopRingtone()
                    setAllIndexesToFalse({ it.isPlaying }, { it.isPlaying = false }, position)
                }
            }
        }

        override fun recycleViewClickListener(view: View, position: Int) {
            //in case of setting clickListener for whole row view
        }

        private inline fun setClickedIndexToTrue(actionSetTrue: (RingtoneObject) -> Unit,
                                                 actionSetFalse: (RingtoneObject) -> Unit,
                                                 actionIsCheckedOrPlaying: (RingtoneObject) -> Boolean,
                                                 position: Int) {
            ringtonePopulationList.forEachIndexed { index, ringtoneObject ->
                if (index == position) {
                    actionSetTrue(ringtoneObject)
                    ringtonesRecycleViewAdapter.notifyItemChanged(position)

                } else if (actionIsCheckedOrPlaying(ringtoneObject)) {
                    actionSetFalse(ringtoneObject)
                    ringtonesRecycleViewAdapter.notifyItemChanged(index)

                }
            }
        }

        private inline fun setAllIndexesToFalse(actionIsCheckedOrPlaying: (RingtoneObject) -> Boolean,
                                                actionSetFalse: (RingtoneObject) -> Unit, position: Int) {
            ringtonePopulationList.forEach { ringtoneObject ->
                if (actionIsCheckedOrPlaying(ringtoneObject)) {
                    actionSetFalse(ringtoneObject)
                    ringtonesRecycleViewAdapter.notifyItemChanged(position)
                    return

                }
            }
        }

    }

}

