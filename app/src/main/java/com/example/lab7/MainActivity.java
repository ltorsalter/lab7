package com.example.lab7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private int progresssRabbit = 0;
    private int progresssTurtle = 0;

    private Button btn_start;
    private SeekBar sb_rabbit, sb_turtle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_start = findViewById(R.id.btn_start);
        sb_rabbit = findViewById(R.id.sb_rabbit);
        sb_turtle = findViewById(R.id.sb_turtle);

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_start.setEnabled(false);
                progresssRabbit = 0;
                progresssTurtle = 0;
                sb_rabbit.setProgress(0);
                sb_turtle.setProgress(0);
                runRabbit();
                runTurtle();
            }
        });
    }
    private final Handler handler = new Handler(Looper.myLooper(), new Handler.Callback(){
       @Override
       public boolean handleMessage(@NonNull Message msg){
           if (msg.what == 1)
               sb_rabbit.setProgress(progresssRabbit);
           else if(msg.what == 2)
               sb_turtle.setProgress(progresssTurtle);

           if(progresssRabbit >= 100 && progresssTurtle < 100){
               Toast.makeText(MainActivity.this,"兔子勝利", Toast.LENGTH_LONG).show();
               btn_start.setEnabled(true);
           }else if(progresssTurtle >= 100 && progresssRabbit < 100) {
               Toast.makeText(MainActivity.this, "烏龜勝利", Toast.LENGTH_LONG).show();
               btn_start.setEnabled(true);
           }
           return false;
       }
    });
    private void runRabbit(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean[] sleepProbability = {true, true, false};
                while(progresssRabbit <= 100 && progresssTurtle < 100){
                    try{
                        Thread.sleep(100);
                        if(sleepProbability[(int)(Math.random()*3)])
                            Thread.sleep(300);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    progresssRabbit += 3;
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                }

            }
        }).start();
    }
    private void runTurtle(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progresssTurtle <= 100 && progresssRabbit < 100){
                    try{
                        Thread.sleep(100);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    progresssTurtle += 1;
                    Message msg = new Message();
                    msg.what = 2;
                    handler.sendMessage(msg);
                }
            }
        }).start();
    }

}