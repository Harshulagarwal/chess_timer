package com.example.timer;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    TextView p1, p2;
    long time = appdata.timedata;
    CountDownTimer countDownTimer1, countDownTimer2;
    long timeleft1 = time, timeleft2 = time;
    boolean p1running, p2running, ispaused;
    Button resetbutton, pausebutton;
    TextToSpeech textToSpeech;
    int play;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //    return super.onCreateOptionsMenu(menu);

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.gamemenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (timeleft1 < time || timeleft2 < time) {
            Toast.makeText(this, "RESET TIMER FIRST", Toast.LENGTH_SHORT).show();
        } else {
            if (item.getItemId() == R.id.set5min) {
                appdata.timedata = 5 * 1000 * 60;

            } else if (item.getItemId() == R.id.set10min) {
                appdata.timedata = 10 * 1000 * 60;

            } else {
                appdata.timedata = 15 * 1000 * 60;

            }
            startActivity(new Intent(MainActivity.this, MainActivity.class));

            finish();


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        p1 = findViewById(R.id.textView);
        p2 = findViewById(R.id.textView2);
        resetbutton = findViewById(R.id.button);
        updatetimetext(time, p1);
        updatetimetext(time, p2);
        pausebutton = findViewById(R.id.button2);
        pausebutton.setEnabled(false);
        resetbutton.setEnabled(false);
        textToSpeech = new TextToSpeech(MainActivity.this, new OnInitListener() {

            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    //  int r = textToSpeech.setLanguage(Locale.ENGLISH);
                }
            }
        });
        p1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pausebutton.setEnabled(true);
                f1();
            }
        });

        p2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pausebutton.setEnabled(true);

                f2();
            }
        });

        resetbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textToSpeech.shutdown();
                if (countDownTimer1 != null)
                    countDownTimer1.cancel();
                if (countDownTimer2 != null)
                    countDownTimer2.cancel();
//                time = 6000;
//                timeleft2 = time;
//                timeleft1 = time;
                startActivity(new Intent(MainActivity.this, MainActivity.class));
                finish();

            }
        });

        pausebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ispaused) {
                    if (countDownTimer1 != null)
                        countDownTimer1.cancel();
                    if (countDownTimer2 != null)
                        countDownTimer2.cancel();
                    updatetimetext(timeleft2, p2);
                    updatetimetext(timeleft1, p1);
                    if (p1running)
                        play = 0;
                    else
                        play = 1;
                    p2running = false;
                    p1running = false;
                    ispaused = true;
                    pausebutton.setText("PLAY");
                } else {
                    if (play == 0) f1();
                    else if (play == 1) f2();
                }

            }

        });
    }

    public void updatetimetext(long time, TextView textView) {
        int min = (int) time / 60 / 1000;
        int sec = ((int) time / 1000) % 60;
        textView.setText(String.format(Locale.getDefault(), "%02d:%02d", min, sec));
    }

    public void f1() {
        resetbutton.setEnabled(true);
        if (!p1running) {
            ispaused = false;
            pausebutton.setText("PAUSE");
            countDownTimer1 = new CountDownTimer(timeleft1, 1000) {
                int c = 0;

                @Override
                public void onTick(long l) {
                    if (timeleft1 == time) timeleft1 = l;
                    else
                        timeleft1 = l - 1000;
                    updatetimetext(timeleft1, p1);
                    c++;
                    if (c % 20 == 0) {
                        textToSpeech.speak("PLAYER 1, TIME RUNNING", TextToSpeech.QUEUE_FLUSH, null);
                    }
                }

                @Override
                public void onFinish() {
                    if (timeleft1 < 1000) {
                        textToSpeech.speak("PLAYER 1 HAS WON", TextToSpeech.QUEUE_FLUSH, null);
                        //Toast.makeText(MainActivity.this, "PLAYER 2 WON", Toast.LENGTH_SHORT).show();
                    } else if (timeleft2 < 1000) {
                        textToSpeech.speak("PLAYER 2 HAS WON", TextToSpeech.QUEUE_FLUSH, null);
                    }
                    if (countDownTimer1 != null)
                        countDownTimer1.cancel();
                    if (countDownTimer2 != null)
                        countDownTimer2.cancel();
                }


            }.start();
            if (p2running) {
                countDownTimer2.cancel();
                updatetimetext(timeleft2, p2);
                p2running = false;
            }
            p1.setBackgroundColor(Color.GREEN);
            p2.setBackgroundColor(Color.RED);

            p1running = true;
        }
    }

    public void f2() {
        resetbutton.setEnabled(true);
        if (!p2running) {
            ispaused = false;
            pausebutton.setText("PAUSE");
            countDownTimer2 = new CountDownTimer(timeleft2, 1000) {
                int c = 0;

                @Override
                public void onTick(long l) {
                    if (timeleft2 == time) timeleft2 = l;
                    else
                        timeleft2 = l - 1000;
                    updatetimetext(timeleft2, p2);
                    c++;
                    if (c % 20 == 0) {
                        textToSpeech.speak("PLAYER 2, TIME RUNNING", TextToSpeech.QUEUE_FLUSH, null);
                    }
                }

                @Override
                public void onFinish() {
                    if (timeleft1 < 1000) {
                        textToSpeech.speak("PLAYER 1 HAS WON", TextToSpeech.QUEUE_FLUSH, null);
                    } else if (timeleft2 < 1000) {
                        textToSpeech.speak("PLAYER 2 HAS WON", TextToSpeech.QUEUE_FLUSH, null);
                    }
                    if (countDownTimer1 != null)
                        countDownTimer1.cancel();
                    if (countDownTimer2 != null)
                        countDownTimer2.cancel();
                }

            }.start();

            if (p1running) {
                countDownTimer1.cancel();
                updatetimetext(timeleft1, p1);
                p1running = false;
            }
            p2.setBackgroundColor(Color.GREEN);
            p1.setBackgroundColor(Color.RED);
            p2running = true;
        }
//                else {
//                    countDownTimer2.cancel();
//                    updatetimetext(timeleft2, p2);
//                    p2running = false;
//                }
    }
}
