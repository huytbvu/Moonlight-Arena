package com.game.activity;

/**
 * @author Huy Vu
 * Moonlight Arena
 */

import java.util.ArrayList;
import java.util.Collections;

import com.game.resource.*;
import com.game.utility.BackgroundMusic;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.animation.*;
import android.view.animation.Animation.AnimationListener;
import android.widget.*;

public class WelcomeActivity extends Activity {
    
    private int[] today_guests = new int[4];
    
    int[] guest_photo_ID = {R.id.chosenGuestImage1,R.id.chosenGuestImage2,
            R.id.chosenGuestImage3,R.id.chosenGuestImage4};
    
    int[] guest_name_ID = {R.id.chosenGuestName1,R.id.chosenGuestName2,
            R.id.chosenGuestName3,R.id.chosenGuestName4};
    

    int[] guest_layout_ID = {R.id.chosenGuestLayout1,R.id.chosenGuestLayout2,
            R.id.chosenGuestLayout3,R.id.chosenGuestLayout4};
    
    int[] text_layout_ID = {R.id.welcomeText1,R.id.welcomeText2,R.id.welcomeText3,R.id.welcomeText4};
    
    private final int OFFSET = 1000;
    private final int INTERVAL = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        findViewById(R.id.backgroundArena).setBackgroundResource(R.drawable.stage_background_congrat);
        ArrayList<Integer> guest_ID = new ArrayList<Integer>();
        for(int i=0; i<10; i++) guest_ID.add(i);
        Collections.shuffle(guest_ID);
        
        for(int i=0; i<4; i++){
            today_guests[i] = guest_ID.get(i);
        
            ((ImageView) findViewById(guest_photo_ID[i]))
                .setBackgroundResource(SpecialContestantCollection.guest_photo[today_guests[i]]);
            ((TextView) findViewById(guest_name_ID[i]))
                .setText(getString(SpecialContestantCollection.guest_name[today_guests[i]]));
            
        }

        animateTitle();
        for(int i=0; i<4; i++) animateWelcomeText(i);
        
        ((Button) findViewById(R.id.toArenaButton)).setOnClickListener(new View.OnClickListener() 
        {
            public void onClick(View v) {
                setReady();
                BackgroundMusic.stopOne(BackgroundResource.INTRO_TRACK);
                BackgroundMusic.playOneSong(getApplicationContext(), 
                        BackgroundResource.INTRO_OPPO_TRACK, BackgroundResource.INTRO_OPPO_TRACK_DATA);
                for(int i=0; i<4; i++){
                    animateGuest(i);
                    animateWelcomeRivalText(i);
                }
            }});
        
        ((Button) findViewById(R.id.toRuleButton)).setOnClickListener(new View.OnClickListener() 
        {
            public void onClick(View v) {
                Intent toRule = new Intent(WelcomeActivity.this, RuleActivity.class);
                BackgroundMusic.stopOne(BackgroundResource.INTRO_TRACK);
                startActivity(toRule);
            }});
        
    }
    
    private void setReady(){
        ((TextView)findViewById(text_layout_ID[0])).setText(getResources().getString(R.string.rival_msg_1));
        ((TextView)findViewById(text_layout_ID[1])).setText(getResources().getString(R.string.rival_msg_2));
        ((TextView)findViewById(text_layout_ID[2])).setText(getResources().getString(R.string.rival_msg_3));
        ((TextView)findViewById(text_layout_ID[3])).setText(getResources().getString(R.string.rival_msg_4));   
        findViewById(R.id.toArenaButton).setVisibility(View.GONE);
        findViewById(R.id.toRuleButton).setVisibility(View.GONE);
        findViewById(R.id.textReady).setVisibility(View.GONE);
        findViewById(R.id.pictureMoonlightArena).setVisibility(View.GONE);
        findViewById(R.id.backgroundArena).setBackgroundResource(R.drawable.welcome_stage);
        findViewById(R.id.backgroundArena).setVisibility(View.VISIBLE);    
    }
    
    private AnimationSet animateTransAndFade(int index, int baseInterval, boolean withTrans){
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator());
        fadeIn.setStartOffset(2*index*baseInterval);
        fadeIn.setDuration(baseInterval);
        
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setStartOffset(2*(index+1)*baseInterval);
        fadeOut.setDuration(baseInterval);
        
        int transOffset = 75;
        Animation trans = new TranslateAnimation(((index==0)?1:0)*transOffset, 
                ((index==1)?1:0)*transOffset, ((index==2)?1:0)*transOffset, ((index==3)?1:0)*transOffset);
        trans.setStartOffset(2*index*baseInterval);
        trans.setDuration(baseInterval*3);
        
        AnimationSet animation = new AnimationSet(false);
        animation.addAnimation(fadeIn);
        animation.addAnimation(fadeOut);
        if(withTrans) animation.addAnimation(trans);
        animation.setRepeatCount(1);
        
        return animation;
    }
    
    private void animateWelcomeRivalText(final int index){
        AnimationSet animation = animateTransAndFade(index,INTERVAL*6/5,false);
        ((TextView)findViewById(text_layout_ID[index])).setAnimation(animation);
        animation.setAnimationListener(new AnimationListener(){

            @Override
            public void onAnimationEnd(Animation animation) {
                ((TextView)findViewById(text_layout_ID[index])).setVisibility(View.INVISIBLE);
                if(index==3){
                    Intent toStart = new Intent(WelcomeActivity.this, ArenaActivity.class);
                    toStart.putExtra("guestList", today_guests);
                    BackgroundMusic.stopOne(BackgroundResource.INTRO_OPPO_TRACK);
                    finish();
                    startActivity(toStart);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}

            @Override
            public void onAnimationStart(Animation animation) {}
            
        });
        
    }
    
    private void animateWelcomeText(final int index){
        AnimationSet animation = animateTransAndFade(index,INTERVAL*3/2,false);
        
        Animation trans;
        switch(index){
        case 0: trans = new TranslateAnimation(0,20,0,0);break;
        case 1: trans = new TranslateAnimation(30,0,0,0);break;
        case 2: trans = new TranslateAnimation(0,0,20,5);break;
        case 3: trans = new TranslateAnimation(10,10,0,0);break;
        default: trans = new TranslateAnimation(0,0,0,0);break;
        }
        trans.setStartOffset(2*index*INTERVAL*3/2);
        trans.setDuration(3*INTERVAL*3/2);
        
        animation.addAnimation(trans);
        ((TextView)findViewById(text_layout_ID[index])).setAnimation(animation);
        animation.setAnimationListener(new AnimationListener(){

            @Override
            public void onAnimationEnd(Animation animation) {
                ((TextView)findViewById(text_layout_ID[index])).setVisibility(View.INVISIBLE);
                
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}

            @Override
            public void onAnimationStart(Animation animation) {}
            
        });
        
    }
    
    private void animateGuest(final int index){
        AnimationSet animation = animateTransAndFade(index,INTERVAL,true);
        
        ((RelativeLayout) findViewById(guest_layout_ID[index])).setAnimation(animation);
        
        animation.setAnimationListener(new AnimationListener() {
            public void onAnimationEnd(Animation animation) {
                ((RelativeLayout) findViewById(guest_layout_ID[index])).setVisibility(View.INVISIBLE);
                
            }
            public void onAnimationRepeat(Animation animation) {}
            public void onAnimationStart(Animation animation) {}
        });
    }
    
    public void animateTitle(){
        Animation zoom = new ScaleAnimation(0.1f, 1, 0.1f, 1, 
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        zoom.setDuration(INTERVAL*5/2);
        (findViewById(R.id.pictureMoonlightArena)).setAnimation(zoom);
    }
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.welcome, menu);
        return true;
    }
    
    @Override
    public void onStart(){
        super.onStart();
        BackgroundMusic.playOneSong(this, BackgroundResource.INTRO_TRACK, BackgroundResource.INTRO_TRACK_DATA);
    }
    
    @Override
    public void onStop(){
        BackgroundMusic.stopOne(BackgroundResource.INTRO_OPPO_TRACK);
        BackgroundMusic.stopOne(BackgroundResource.INTRO_TRACK);
        super.onStop();
    }
    
    @Override
    public void onPause(){
        BackgroundMusic.stopOne(BackgroundResource.INTRO_OPPO_TRACK);
        BackgroundMusic.stopOne(BackgroundResource.INTRO_TRACK);
        super.onPause();
    }
    

    @Override
    public void onDestroy(){
        BackgroundMusic.stopOne(BackgroundResource.INTRO_OPPO_TRACK);
        BackgroundMusic.stopOne(BackgroundResource.INTRO_TRACK);
        super.onDestroy();
    }

}
