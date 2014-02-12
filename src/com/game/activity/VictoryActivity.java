package com.game.activity;

/**
 * @author Huy Vu
 * victory scene of the game
 */

import java.text.DecimalFormat;

import com.game.resource.BackgroundResource;
import com.game.utility.BackgroundMusic;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.*;

public class VictoryActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_victory);
        DecimalFormat moneyFormat = new DecimalFormat("#,###");
        boolean foundAll = getIntent().getBooleanExtra("foundAll", false);
        int backgroundRes = (foundAll) ? R.drawable.cheering_moonfound : R.drawable.cheering;
        ((ImageView)findViewById(R.id.victory_background)).setBackgroundResource(backgroundRes);
        
        ((TextView)findViewById(R.id.total_prize_display))
            .setText("$"+moneyFormat.format(getIntent().getIntExtra("money", 0)));
        
        String toshow = (foundAll) ? getResources().getString(R.string.victory_found_all_msg)
                : getResources().getString(R.string.victory_found_not_msg);
        
        toshow += "\n" + "After " + getIntent().getIntExtra("stage", -1) + " stages, you have ultimately outsmarted "
                + getIntent().getIntExtra("oDefeat", -1) + " of 100 opponents and "
                + getIntent().getIntExtra("gDefeat", -1) + " of 4 major guests. "
                + "It will take a while until another great accomplishment.";
        
        ((TextView)findViewById(R.id.victory_msg_display)).setText(toshow);
        
        ((Button)findViewById(R.id.victory_to_menu)).setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {finish();}}
        );
    }
    
    @Override
    public void onStart(){
        super.onStart();
        BackgroundMusic.playOneSong(this, BackgroundResource.END_TRACK, BackgroundResource.END_TRACK_DATA);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.victory, menu);
        return true;
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
