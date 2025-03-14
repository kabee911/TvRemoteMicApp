package com.kabeer.tvremoteaccess;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {
    private static final int MIC_PERMISSION_REQUEST_CODE = 1;
    private static final String TAG = "TVRemoteMicApp";
    private AudioRecord audioRecord;
    private Button startButton;
    private TextView statusText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startButton = findViewById(R.id.startButton);
        statusText = findViewById(R.id.statusText);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.RECORD_AUDIO}, MIC_PERMISSION_REQUEST_CODE);
                } else {
                    startRecording();
                }
            }
        });
    }

    private void startRecording() {
        int sampleRate = 44100;
        int bufferSize = AudioRecord.getMinBufferSize(sampleRate,
                android.media.AudioFormat.CHANNEL_IN_MONO,
                android.media.AudioFormat.ENCODING_PCM_16BIT);

        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                sampleRate,
                android.media.AudioFormat.CHANNEL_IN_MONO,
                android.media.AudioFormat.ENCODING_PCM_16BIT,
                bufferSize);

        if (audioRecord.getState() == AudioRecord.STATE_INITIALIZED) {
            audioRecord.startRecording();
            statusText.setText("Microphone access enabled for third-party apps");
            Log.d(TAG, "Microphone access enabled for third-party apps");
        } else {
            statusText.setText("Failed to initialize microphone access");
            Log.e(TAG, "Failed to initialize microphone access");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MIC_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startRecording();
            } else {
                statusText.setText("Microphone permission denied");
                Log.e(TAG, "Microphone permission denied");
            }
        }
    }
}
