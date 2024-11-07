package com.example.beepy;

import android.media.RingtoneManager;
import android.net.Uri;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private EditText taskNameEditText;
    private TextView timerTextView;
    private Button setAlarmButton, startTimerButton;
    private CountDownTimer countDownTimer;
    private boolean isTimerRunning = false;
    private MediaPlayer mediaPlayer;

    // Using the default alarm sound if no custom sound is provided
    private Uri alarmSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize default alarm sound
        alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        taskNameEditText = findViewById(R.id.taskNameEditText);
        timerTextView = findViewById(R.id.timerTextView);
        setAlarmButton = findViewById(R.id.setAlarmButton);
        startTimerButton = findViewById(R.id.startTimerButton);

        setAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taskName = taskNameEditText.getText().toString();
                if (!taskName.isEmpty()) {
                    setAlarm(taskName);
                } else {
                    Toast.makeText(MainActivity.this, "Enter a task name", Toast.LENGTH_SHORT).show();
                }
            }
        });

        startTimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isTimerRunning) {
                    startTimer(60000); // 1-minute timer for demonstration
                }
            }
        });
    }

    private void setAlarm(String taskName) {
        Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
        intent.putExtra("taskName", taskName);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        long timeAtButtonClick = System.currentTimeMillis();
        long tenSecondsInMillis = 10000;

        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeAtButtonClick + tenSecondsInMillis, pendingIntent);
            Toast.makeText(this, "Alarm set for 10 seconds later", Toast.LENGTH_SHORT).show();
        }
    }

    private void startTimer(long durationMillis) {
        countDownTimer = new CountDownTimer(durationMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int minutes = (int) (millisUntilFinished / 1000) / 60;
                int seconds = (int) (millisUntilFinished / 1000) % 60;
                timerTextView.setText(String.format("%02d:%02d", minutes, seconds));
            }

            @Override
            public void onFinish() {
                timerTextView.setText("00:00");
                playAlarmSound();
                isTimerRunning = false;
                Toast.makeText(MainActivity.this, "Task time finished!", Toast.LENGTH_SHORT).show();
            }
        }.start();
        isTimerRunning = true;
    }

    private void playAlarmSound() {
        // Play the custom alarm sound if specified in raw folder
        mediaPlayer = MediaPlayer.create(this, alarmSound);
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
