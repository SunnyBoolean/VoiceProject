package com.geostar.liwei.voiceproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button add = (Button) findViewById(R.id.add);
        Button jian = (Button) findViewById(R.id.jian);
        final VoiceView voice = (VoiceView) findViewById(R.id.voiceview);
        //音量加
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voice.addVoice();
            }
        });
        //音量减
        jian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voice.rediceVoice();
            }
        });
        //音量监听
        voice.setOnVoiceChangeListener(new VoiceView.OnVoicceChangeListener() {
            @Override
            public void onVoiceChange(int position) {
//                Toast.makeText(MainActivity.this, "音量：" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
