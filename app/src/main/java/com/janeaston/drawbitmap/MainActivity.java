package com.janeaston.drawbitmap;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private CanvasView view_Canvas;
    private Button button_Forward;
    private Button button_Backward;
    private Button button_TurnLeft;
    private Button button_TurnRight;
    private SeekBar seekBar_LineSpeed;
    private SeekBar seekBar_AngleSpeed;
    private TextView textView_LineSpeed;
    private TextView textView_AngleSpeed;

    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> forwardTaskHandle = null;
    private ScheduledFuture<?> backwardTaskHandle = null;
    private ScheduledFuture<?> turnLeftTaskHandle = null;
    private ScheduledFuture<?> turnRightTaskHandle = null;

    private final static float MAX_LINE_SPEED = 50.0f;
    private final static float MAX_ANGLE_SPEED = 0.78f;
    private float lineSpeed = 5f;
    private float angleSpeed = 0.09f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initWidgets();
    }

    private void initWidgets() {
        view_Canvas = findViewById(R.id.view_Canvas);
        button_Forward = findViewById(R.id.button_Forward);
        button_Backward = findViewById(R.id.button_Backward);
        button_TurnLeft = findViewById(R.id.button_TurnLeft);
        button_TurnRight = findViewById(R.id.button_TurnRight);
        seekBar_LineSpeed = findViewById(R.id.seekBar_LineSpeed);
        seekBar_AngleSpeed = findViewById(R.id.seekBar_AngleSpeed);
        textView_LineSpeed = findViewById(R.id.textView_LineSpeed);
        textView_AngleSpeed = findViewById(R.id.textView_AngleSpeed);

        seekBar_LineSpeed.setProgress((int)((lineSpeed / MAX_LINE_SPEED) * 100));
        seekBar_AngleSpeed.setProgress((int)((angleSpeed / MAX_ANGLE_SPEED) * 100));
        textView_LineSpeed.setText(String.valueOf(lineSpeed));
        textView_AngleSpeed.setText(String.valueOf(angleSpeed));

        seekBar_LineSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float precent = (float)progress / 100;
                lineSpeed = MAX_LINE_SPEED * precent;
                textView_LineSpeed.setText(String.valueOf(MAX_LINE_SPEED * precent));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBar_AngleSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float precent = (float)progress / 100;
                angleSpeed = MAX_ANGLE_SPEED * precent;
                textView_AngleSpeed.setText(String.valueOf(MAX_ANGLE_SPEED * precent));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        button_Forward.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        if(forwardTaskHandle != null) {
                            forwardTaskHandle.cancel(true);
                        }
                        forwardTaskHandle = scheduler.scheduleWithFixedDelay(forwardTask, 0, 50, TimeUnit.MILLISECONDS);
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL: {
                        if(forwardTaskHandle != null) {
                            forwardTaskHandle.cancel(true);
                            forwardTaskHandle = null;
                        }
                        break;
                    }
                }
                return true;
            }
        });

        button_Backward.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        if(backwardTaskHandle != null) {
                            backwardTaskHandle.cancel(true);
                        }
                        backwardTaskHandle = scheduler.scheduleWithFixedDelay(backwardTask, 0, 50, TimeUnit.MILLISECONDS);
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL: {
                        if(backwardTaskHandle != null) {
                            backwardTaskHandle.cancel(true);
                            backwardTaskHandle = null;
                        }
                        break;
                    }
                }
                return true;
            }
        });

        button_TurnLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        if(turnLeftTaskHandle != null) {
                            turnLeftTaskHandle.cancel(true);
                        }
                        turnLeftTaskHandle = scheduler.scheduleWithFixedDelay(turnLeftTask, 0, 50, TimeUnit.MILLISECONDS);
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL: {
                        if(turnLeftTaskHandle != null) {
                            turnLeftTaskHandle.cancel(true);
                            turnLeftTaskHandle = null;
                        }
                        break;
                    }
                }
                return true;
            }
        });

        button_TurnRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        if(turnRightTaskHandle != null) {
                            turnRightTaskHandle.cancel(true);
                        }
                        turnRightTaskHandle = scheduler.scheduleWithFixedDelay(turnRightTask, 0, 50, TimeUnit.MILLISECONDS);
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL: {
                        if(turnRightTaskHandle != null) {
                            turnRightTaskHandle.cancel(true);
                            turnRightTaskHandle = null;
                        }
                        break;
                    }
                }
                return true;
            }
        });
    }

    private Runnable forwardTask = new Runnable() {
        @Override
        public void run() {
            runOnUiThread(()->{
                view_Canvas.updateRobotPose(lineSpeed, 0, 0);
                Log.d("MoveRobotTask", "move forward");
            });
        }
    };

    private Runnable backwardTask = new Runnable() {
        @Override
        public void run() {
            runOnUiThread(()->{
                view_Canvas.updateRobotPose(-lineSpeed, 0, 0);
                Log.d("MoveRobotTask", "move backward");
            });
        }
    };

    private Runnable turnLeftTask = new Runnable() {
        @Override
        public void run() {
            runOnUiThread(()->{
                view_Canvas.updateRobotPose(0, 0, -angleSpeed);
                Log.d("MoveRobotTask", "move turn left");
            });
        }
    };

    private Runnable turnRightTask = new Runnable() {
        @Override
        public void run() {
            runOnUiThread(()->{
                view_Canvas.updateRobotPose(0, 0, angleSpeed);
                Log.d("MoveRobotTask", "move turn right");
            });
        }
    };

}