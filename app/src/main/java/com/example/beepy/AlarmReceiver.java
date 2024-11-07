package com.example.beepy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

    private MediaPlayer mediaPlayer;

    @Override
    public void onReceive(Context context, Intent intent) {
        // Get the task name from the intent
        String taskName = intent.getStringExtra("taskName");

        // Show a toast notification to remind the user about the task
        Toast.makeText(context, "Time for task: " + taskName, Toast.LENGTH_LONG).show();

        // Play the alarm sound when the alarm goes off
        mediaPlayer = MediaPlayer.create(context, R.raw.alarm_sound);
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    // Optional: Clean up the MediaPlayer if needed
    public void stopAlarm() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
