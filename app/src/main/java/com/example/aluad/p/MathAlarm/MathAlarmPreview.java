package com.example.aluad.p.MathAlarm;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.widget.Toast;

import com.example.aluad.p.CountsTimeToAlarmStart;
import com.example.aluad.p.R;
import com.example.aluad.p.ShowLogsOld;

class MathAlarmPreview  {
    private int pickedHour, pickedMinute;
    private int selectedMusic;
    private String alarmMessageText;
    private int alarmComplexityLevel;
     private int selectedDeepSleepMusic;
    private Context activityContext;

     MathAlarmPreview(Context activityContext,int pickedHour,int pickedMinute,int selectedMusic,
                            int alarmComplexityLevel,String alarmMessageText,int selectedDeepSleepMusic) {
        this.pickedHour = pickedHour;
        this.pickedMinute = pickedMinute;
        this.selectedMusic = selectedMusic;
        this.alarmComplexityLevel = alarmComplexityLevel;
        this.alarmMessageText = alarmMessageText;
        this.activityContext = activityContext;
         this.selectedDeepSleepMusic = selectedDeepSleepMusic;
    }

    //OnClick Method for Button - confirm (set Alarm on picked hour and minute)
      void ConfirmAlarmPreview_Method() {
          if(ShowLogsOld.LOG_STATUS) ShowLogsOld.i( "MathAlarmPreview "+" ConfirmAlarmPreview_Method");
//          //stop set alarm if exist
//          Intent dismissIntent = new Intent(MainMathAlarm.ALARM_DISMISS_ACTION);
//         activityContext.sendBroadcast(dismissIntent);

        OnOffAlarm onOffAlarm = new OnOffAlarm(activityContext,pickedHour,pickedMinute,alarmComplexityLevel,selectedMusic,true,alarmMessageText,selectedDeepSleepMusic);
        onOffAlarm.SetNewAlarm();

        ShowToast_TimeToAlarmBoom();
        StartWakeLockAndMainActivity();
    }

    private void ShowToast_TimeToAlarmBoom() {
        // Method that shows calculate - How much time left until alarm start
        CountsTimeToAlarmStart countsTimeToAlarmStart = new CountsTimeToAlarmStart();
        countsTimeToAlarmStart.HowMuchTimeToStart(pickedHour,pickedMinute);

        Toast Toast_timeLeftToAlarmStart = Toast.makeText(activityContext,
                activityContext.getString(R.string.MAP_Toast_timeLeftToAlarmStart,
                CountsTimeToAlarmStart.MinuteHourConvertMethod(countsTimeToAlarmStart.getResultHours(),countsTimeToAlarmStart.getResultMinutes() ))
             , Toast.LENGTH_LONG);
        Toast_timeLeftToAlarmStart.setGravity(Gravity.TOP | Gravity.START, 0, 0);
        Toast_timeLeftToAlarmStart.show();
    }

    private void StartWakeLockAndMainActivity() {
        //start service with PartialWakeLock
        Intent wakeLockIntent = new Intent(activityContext,WakeLockService.class);
        wakeLockIntent.putExtra("alarmTimeKey",CountsTimeToAlarmStart.MinuteHourConvertMethod(pickedHour,pickedMinute));
        activityContext.startService(wakeLockIntent);

        //after confirming alarm configurations, user will be moved to main activity
        Intent intent  = new Intent(activityContext, SetAlarmFromHistory.class);
        activityContext.startActivity(intent);
    }


}

