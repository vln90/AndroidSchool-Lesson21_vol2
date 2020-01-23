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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    public static final int MSG_START_TIMER = 1;
    public static final int MSG_STOP_TIMER = 2;
    public static final String BUNDLE_KEY_TIME = "BUNDLE_KEY_TIME";

    public static final int MSG_TIMER_CALLED = 3;
    public static final String BUNDLE_CURRENT_TIME = "BUNDLE_CURRENT_TIME";

    private TimerServiceConnection mTimerServiceConnection;

    private Messenger mTimerServiceMessenger;

    private Messenger mMainActivityMessenger = new Messenger(new MainActivityHandler());

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
        mTimerServiceMessenger = null;
    }

    private void startTimer() {
        if (mTimerServiceMessenger != null) {
            Message message = Message.obtain(null, MSG_START_TIMER);

            Bundle bundle = new Bundle();
            bundle.putLong(BUNDLE_KEY_TIME, 30000L);
            message.setData(bundle);
            message.replyTo = mMainActivityMessenger;

            try {
                mTimerServiceMessenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
//        if (mTimerService != null) {
//            mTimerService.startCountdownTimer(10000L, 1000L);
//        }
    }

    private void startListeningTimer() {
//        mTimerService.setOnTimerChangedListener(new OnTimerChangedListener() {
//            @Override
//            public void onTimerChanged(String timer) {
//                mTextView.setText(timer);
//            }
//        });
    }

    private void stopListeningTimer() {
//        if (mTimerService != null) {
//            mTimerService.setOnTimerChangedListener(null);
//        }
    }

    private class TimerServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected() called with: name = [" + name + "], service = [" + service + "]");

//            mTimerService = ((TimerService.LocalTimerServiceBinder) service).getTimerService();
            mTimerServiceMessenger = new Messenger(service);
            startListeningTimer();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected() called with: name = [" + name + "]");
        }
    }

    public interface OnTimerChangedListener {
        void onTimerChanged(String timer);
    }

    public class MainActivityHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case MSG_TIMER_CALLED:
                    String time = msg.getData().getString(BUNDLE_CURRENT_TIME);

                    mTextView.setText(time);

                    break;
            }
        }
    }
}
