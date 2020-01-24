package com.vleonidov.service_1;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.vleonidov.lesson_23_another_process.ITimerServiceAIDL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private TimerServiceConnection mTimerServiceConnection;

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.start_service_button).setOnClickListener(this);
        findViewById(R.id.stop_service_button).setOnClickListener(this);
        findViewById(R.id.bind_service_button).setOnClickListener(this);
        findViewById(R.id.unbind_service_button).setOnClickListener(this);
        findViewById(R.id.start_timer_button).setOnClickListener(this);

        mTextView = findViewById(R.id.timer_text_view);

        mTimerServiceConnection = new TimerServiceConnection();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_service_button:
                Log.d(TAG, "start_service_button clicked");

                startService();
                break;

            case R.id.stop_service_button:
            default:
                Log.d(TAG, "stop_service_button clicked");

                stopService();
                break;
            case R.id.bind_service_button:
                bindService();
                break;
            case R.id.unbind_service_button:
                unbindService();
                break;
            case R.id.start_timer_button:
                startTimer();
                break;
        }
    }

    private void startService() {
//        Intent intent = new Intent(this, TimerService.class);
//        startService(intent);
    }

    private void stopService() {
//        Intent intent = new Intent(this, TimerService.class);
//        stopService(intent);

//        Intent intent = new Intent(this, TimerService.class);
//        intent.setAction(TimerService.ACTION_CLOSE);
//
//        startService(intent);
    }

    private void bindService() {
        ComponentName componentName = new ComponentName("com.vleonidov.lesson_23_another_process",
                "com.vleonidov.lesson_23_another_process.TimerService");
        Intent intent = new Intent();
        intent.setComponent(componentName);

        bindService(intent, mTimerServiceConnection, BIND_AUTO_CREATE);
    }

    private void unbindService() {
        unbindService(mTimerServiceConnection);

        stopListeningTimer();
    }

    private void startTimer() {
    }

    private void stopListeningTimer() {
    }

    private class TimerServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected() called with: name = [" + name + "], service = [" + service + "]");

            ITimerServiceAIDL timerServiceAIDL = ITimerServiceAIDL.Stub.asInterface(service);

            try {
                timerServiceAIDL.startTimer(50000, 1000);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected() called with: name = [" + name + "]");
        }
    }
}
