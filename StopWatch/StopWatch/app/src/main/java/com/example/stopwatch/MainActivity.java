package com.example.stopwatch;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView tvTime;
    private Button btnStart, btnStop, btnHold;
    private long startTime = 0L;
    private long elapsedTime = 0L; // Tracks total elapsed time
    private Handler handler = new Handler();
    private boolean isRunning = false;

    private Runnable updateTimeRunnable = new Runnable() {
        @Override
        public void run() {
            long currentTime = System.currentTimeMillis();
            long millis = currentTime - startTime + elapsedTime; // Add previously elapsed time
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;
            int milliseconds = (int) (millis % 1000);

            tvTime.setText(String.format("%02d:%02d:%02d", minutes, seconds, milliseconds / 10));
            handler.postDelayed(this, 10); // Update every 10ms for smooth display
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvTime = findViewById(R.id.tvTime);
        btnStart = findViewById(R.id.btnStart);
        btnStop = findViewById(R.id.btnStop);
        btnHold = findViewById(R.id.btnHold);

        // Initially, only the start button should be enabled
        btnStart.setEnabled(true);
        btnStop.setEnabled(false);
        btnHold.setEnabled(false);
    }

    // Start the stopwatch
    public void onStartClicked(View view) {
        if (!isRunning) {
            startTime = System.currentTimeMillis(); // Capture current time
            handler.post(updateTimeRunnable);
            isRunning = true;

            btnStart.setEnabled(false);  // Disable start while running
            btnStop.setEnabled(true);    // Enable stop and hold
            btnHold.setEnabled(true);
        }
    }

    // Stop the stopwatch and reset the timer
    public void onStopClicked(View view) {
        if (isRunning) {
            handler.removeCallbacks(updateTimeRunnable);
            elapsedTime += System.currentTimeMillis() - startTime; // Save elapsed time
            isRunning = false;
        }

        // Reset elapsedTime and startTime to start from the beginning next time
        elapsedTime = 0L;
        startTime = 0L;

        // Reset the displayed time to 00:00:00
        tvTime.setText("00:00:00");

        btnStart.setEnabled(true);  // Allow restart
        btnStop.setEnabled(false);  // Disable stop and hold
        btnHold.setEnabled(false);
    }

    // Hold the stopwatch (pause)
    public void onHoldClicked(View view) {
        if (isRunning) {
            handler.removeCallbacks(updateTimeRunnable); // Pause time updates
            elapsedTime += System.currentTimeMillis() - startTime; // Save elapsed time
            isRunning = false;

            btnStart.setEnabled(true);  // Allow start to resume
            btnStop.setEnabled(true);   // Keep stop enabled
            btnHold.setEnabled(false);  // Disable hold
        }
    }
}
