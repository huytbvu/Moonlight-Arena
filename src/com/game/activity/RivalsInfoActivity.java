package com.game.activity;

/**
 * @author Huy Vu
 * provide information about the guests
 */

import com.game.resource.*;
import com.game.utility.BackgroundMusic;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class RivalsInfoActivity extends Activity{
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rivals_info);
        for(int i=0; i<SpecialContestantCollection.NUMBER_OF_GUEST; i++){
            final String cur_name = getString(SpecialContestantCollection.guest_name[i]);
            final String cur_desc = getString(SpecialContestantCollection.guest_desc[i]);
            final String cur_spec_1 = "Primary specialty: " + Question.SPECIALTY_CODE.get(SpecialContestantCollection.guest_specialty_1[i]);
            final String cur_spec_2 = "Secondary specialty: " + Question.SPECIALTY_CODE.get(SpecialContestantCollection.guest_specialty_2[i]);
            ((ImageButton) findViewById(SpecialContestantCollection.guest_photo_button[i])).setOnClickListener(
                    new View.OnClickListener() {
                        public void onClick(View v) {
                            ((TextView) findViewById(R.id.guest_name)).setText(cur_name);
                            ((TextView) findViewById(R.id.guest_description)).setText(cur_desc);
                            ((TextView) findViewById(R.id.guest_specialty_1)).setText(cur_spec_1);
                            ((TextView) findViewById(R.id.guest_specialty_2)).setText(cur_spec_2);
                        }});
        }
        
        ((Button) findViewById(R.id.buttonBackRivalsInfo)).setOnClickListener(
                new View.OnClickListener() {public void onClick(View v) {finish();}});
        
    }
    
    @Override
    public void onStart(){
        super.onStart();
        BackgroundMusic.playOneSong(this, BackgroundResource.INFO_TRACK, BackgroundResource.INFO_TRACK_DATA);
    }
    
    @Override
    public void onStop(){
        BackgroundMusic.stopOne(BackgroundResource.INFO_TRACK);
        super.onStop();
    }
    
    @Override
    public void onPause(){
        BackgroundMusic.stopOne(BackgroundResource.INFO_TRACK);
        super.onPause();
    }
    

    @Override
    public void onDestroy(){
        BackgroundMusic.stopOne(BackgroundResource.INFO_TRACK);
        super.onDestroy();
    }
    
   
}
