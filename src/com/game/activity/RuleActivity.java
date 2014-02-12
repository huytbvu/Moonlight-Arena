package com.game.activity;

/**
 * @author Huy Vu
 * This is the screen where game rules are found
 */

import com.game.resource.BackgroundResource;
import com.game.utility.BackgroundMusic;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;
import android.widget.*;

public class RuleActivity extends Activity {
    
    private static final long ANIMATION_TIME = 1500;

    private RelativeLayout infoLayout;

    private final int main_rule_on = 0;
    private final int question_rule_on = 1;
    private final int rival_rule_on = 2;
    
    private boolean open_screen = false; 
    private boolean[] onScreenFlag = {false, false, false};
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rule);
        infoLayout = (RelativeLayout)findViewById(R.id.infoView);
        
        ((Button)findViewById(R.id.buttonBackInfo)).setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
               finish();
                
            }
        });
        
        ((Button)findViewById(R.id.buttonMainInfo)).setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
               animate(main_rule_on);
            }
        });
        
        ((Button)findViewById(R.id.buttonQuestionInfo)).setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
               animate(question_rule_on);
            }
        });

        ((Button)findViewById(R.id.buttonRivalsInfo)).setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
               animate(rival_rule_on);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.rule, menu);
        return true;
    }

    
    public void expandScreen(){
        open_screen = true;
        infoLayout.setVisibility(View.VISIBLE);
        Animation zoom = new ScaleAnimation(1, 1, 0.01f, 1, 
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        zoom.setDuration(ANIMATION_TIME);
        zoom.setAnimationListener(new AnimationListener(){

            @Override
            public void onAnimationEnd(Animation arg0) {
                (findViewById(R.id.infoView)).setVisibility(View.VISIBLE);
            }
            public void onAnimationRepeat(Animation arg0) {}
            public void onAnimationStart(Animation arg0) {}
            
        });
        infoLayout.startAnimation(zoom);
    }
    
    public void animate(int onScreen){
        if(!open_screen){
            fillBoard(onScreen);
            expandScreen();
            resetFlags(onScreen);
        }
        else{
            if(!onScreenFlag[onScreen]){
                fillBoard(onScreen);
                resetFlags(onScreen);
            }
            else{
                collapseScreen();
                onScreenFlag[onScreen] = false;
            }
        }
    }
    
    public void resetFlags(int current){
        for(int i=0; i<3; i++)
            if(i!=current) onScreenFlag[i] = false;
            else onScreenFlag[i] = true;
    }
    
    public void collapseScreen(){
        open_screen = false;
        infoLayout.setVisibility(View.VISIBLE);
        Animation zoom = new ScaleAnimation(1, 1, 1, 0.01f, 
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        zoom.setDuration(ANIMATION_TIME);
        zoom.setAnimationListener(new AnimationListener(){

            @Override
            public void onAnimationEnd(Animation arg0) {
                (findViewById(R.id.infoView)).setVisibility(View.INVISIBLE);
            }
            public void onAnimationRepeat(Animation arg0) {}
            public void onAnimationStart(Animation arg0) {}
            
        });
        infoLayout.startAnimation(zoom);
    }
    
    public void fillBoard(int index){
        switch(index){
        case main_rule_on:
            fillText(R.string.rule_main_1,R.string.rule_main_2,R.string.rule_main_3);break;
        case question_rule_on:
            fillText(R.string.rule_quest_1,R.string.rule_quest_2,R.string.rule_quest_3); break;
        case rival_rule_on:
            fillText(R.string.rule_rival_1,R.string.rule_rival_2,R.string.rule_rival_3); break;
        default: break;
        }
        
    }
    
    public void fillText(int id1, int id2, int id3){
        ((TextView) findViewById(R.id.infoTextStyle1)).setText(getResources().getString(id1));
        ((TextView) findViewById(R.id.infoTextStyle2)).setText(getResources().getString(id2));
        ((TextView) findViewById(R.id.infoTextStyle3)).setText(getResources().getString(id3));
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
