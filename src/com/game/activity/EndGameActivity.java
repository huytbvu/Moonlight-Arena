package com.game.activity;

import java.text.DecimalFormat;

import com.game.resource.BackgroundResource;
import com.game.utility.BackgroundMusic;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * 
 * @author Huy Vu
 * end of the game
 */

public class EndGameActivity extends Activity {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_end);
		
		int opponentLeft = getIntent().getIntExtra("opponentsLeft", -1);
		int finalScore = getIntent().getIntExtra("score", -1);
		int currentStage = getIntent().getIntExtra("stage", -1);
		
		DecimalFormat moneyFormat = new DecimalFormat("#,###");
		String statsToShow = getResources().getString(R.string.end_msg) + "\n";
		statsToShow += "You are eliminated at stage " + currentStage + " and thus could only secure half of what you have earned along the way.";
        statsToShow += "You have been outsmarted by " + opponentLeft + " opponents at stage " + currentStage + " and thus could only secure half of what you have earned along the way.";
		statsToShow += "As a condolence, you bring $" + moneyFormat.format(finalScore) + " from the arena";
		
		((TextView) findViewById(R.id.stats)).setText(statsToShow);
		
        
		((Button) findViewById(R.id.endButton)).setOnClickListener(new View.OnClickListener() 
        {
            public void onClick(View v) {
                finish();
            }});
	}
	
	@Override
    public void onStart(){
        super.onStart();
        BackgroundMusic.playOneSong(this, BackgroundResource.END_TRACK, BackgroundResource.END_TRACK_DATA);
    }
	
	@Override
    public void onStop(){
        BackgroundMusic.stopOne(BackgroundResource.END_TRACK);
        super.onStop();
    }
    
    @Override
    public void onPause(){
        BackgroundMusic.stopOne(BackgroundResource.END_TRACK);
        super.onPause();
    }
    

    @Override
    public void onDestroy(){
        BackgroundMusic.stopOne(BackgroundResource.END_TRACK);
        super.onDestroy();
    }
	
}
