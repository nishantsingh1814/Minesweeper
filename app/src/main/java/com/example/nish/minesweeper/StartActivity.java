package com.example.nish.minesweeper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {
    Button startGame;
    RadioGroup level;
    TextView beginnerScore;
    TextView intermediateScore;
    TextView expertScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        startGame=(Button)findViewById(R.id.startGame);
        startGame.setOnClickListener(this);

        level=(RadioGroup)findViewById(R.id.level);
        beginnerScore=(TextView)findViewById(R.id.beginnerScore);
        intermediateScore=(TextView)findViewById(R.id.intermediateScore);
        expertScore=(TextView)findViewById(R.id.expertScore);



    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sp=getSharedPreferences("Minesweeper",MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        int beginnerHighScore=sp.getInt("beginnerHighScore",0);
        int intermediateHighScore=sp.getInt("intermediateHighScore",0);
        int expertHighScore=sp.getInt("expertHighScore",0);


        beginnerScore.setText("Best Score: "+beginnerHighScore);
        intermediateScore.setText("Best Score: "+intermediateHighScore);
        expertScore.setText("Best Score: "+expertHighScore);

    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        Intent i = new Intent();
        i.setClass(this, MainActivity.class);

        int levelSelect=level.getCheckedRadioButtonId();

        if(levelSelect==R.id.beginner){
            i.putExtra("n",8);
            i.putExtra("mines",10);
        }
        else if(levelSelect==R.id.intermediate){
            i.putExtra("n",10);
            i.putExtra("mines",15);
        }
        else if(levelSelect==R.id.expert){
            i.putExtra("n",12);
            i.putExtra("mines",22);
        }
        else{
            Toast.makeText(this,"Please Select level  !!",Toast.LENGTH_SHORT).show();
            return;
        }

        if(id==R.id.startGame) {
            startActivity(i);
        }
    }
}
