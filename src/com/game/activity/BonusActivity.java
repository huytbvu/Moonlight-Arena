package com.game.activity;

/**
 * @author Huy Vu
 * Bonus Round 
 * for players who have eliminated at least 90% of regular opponents
 */

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

import com.game.resource.*;
import com.game.utility.BackgroundMusic;
import com.game.utility.Delay;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

public class BonusActivity extends Activity {
    
    private ArrayList<Chest> chests = new ArrayList<Chest>();
    private int moonPieceFound = 0;
    private int goldKeyRemain, timeRem;
    private int moneyBank;
    private RelativeLayout chestLayout;
    private boolean trapHit = false;
    private LinearLayout keyLayout, pieceLayout;
    TextView[] chestView = new TextView[ChestResource.NUMBER_OF_CHESTS];
    ImageView[] keyView = new ImageView[ChestResource.MAX_KEY];
    Handler bonusHandler = new Handler();
    DecimalFormat moneyFormat = new DecimalFormat("#,###");

    CountDownTimer bonusTimer;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bonus);
        chestLayout = (RelativeLayout)findViewById(R.id.chest_display);
        keyLayout = (LinearLayout)findViewById(R.id.key_display);
        pieceLayout = (LinearLayout)findViewById(R.id.moonpiece_display);
        goldKeyRemain = getIntent().getIntExtra("GoldKeyCount", -1);
        ((Button)findViewById(R.id.bonusStopButton)).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				stopBonusPopUp();
			}
		});
        BackgroundMusic.playOneSong(this, BackgroundResource.BONUS_TRACK, BackgroundResource.BONUS_TRACK_DATA);
        new PrepareChest().execute();
        setupWelcome();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.bonus, menu);
        return true;
    }
    
    private void stopBonusPopUp(){
    	bonusTimer.cancel();
	    
		AlertDialog.Builder contLabel = new AlertDialog.Builder(this);
		contLabel.setCancelable(true);
		contLabel.setMessage("Are you sure you want to stop searching the Moon Treasure now?");
		contLabel.setNegativeButton("NO, let's look for it",
			    new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
				    dialog.cancel();
				    handleTime(timeRem*1000);
					
	        }});
		contLabel.setPositiveButton("YES, I've had enough",
		    new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
			    dialog.cancel();
        }});
		contLabel.create().show();
    }
    
    // handle activity when a chest is clicked by user
    private void openChest(int openIndex, int imageSource, String msg){
        chests.get(openIndex).getValue();
        
        View chLayout = getLayoutInflater().inflate(R.layout.chest_toast, (ViewGroup)findViewById(R.id.chest_toast_layout_id));
        Toast chToast = new Toast(getApplicationContext());
        ((ImageView)chLayout.findViewById(R.id.chosen_chest)).setImageResource(imageSource);
        ((TextView)chLayout.findViewById(R.id.chosen_desc)).setText(msg);
        
        chToast.setDuration(Toast.LENGTH_SHORT);
        chToast.setGravity(Gravity.CENTER, 0, 0);
        chToast.setView(chLayout);
        chToast.show();
    }
    
    // setup this activity
    private void setupWelcome(){
        AlertDialog.Builder imageDialog = new AlertDialog.Builder(this);
        View boardLayout = getLayoutInflater().inflate(R.layout.chest_toast, (ViewGroup)findViewById(R.id.chest_toast_layout_id));
        ImageView chart = (ImageView)boardLayout.findViewById(R.id.chosen_chest);
        ((TextView)boardLayout.findViewById(R.id.chosen_desc)).setText(getResources().getString(R.string.bonus_instruct));
        chart.setImageResource(R.drawable.prize_chart);
        imageDialog.setView(boardLayout);
        imageDialog.setPositiveButton("Search for Moon Treasure", new DialogInterface.OnClickListener(){

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                handleTime(ChestResource.BONUS_TIME);
            }
        });
        AlertDialog al = imageDialog.create();
        al.getWindow().setLayout(chart.getWidth()+10, chart.getHeight()+10);
        al.show();
    }
    
    // handle trap chest
    // blow a bomb and game over
    private void handleTrapChest(int openIndex){
        openChest(openIndex, chests.get(openIndex).getChestResource(),"Oh No!! This is a TRAP");
        trapHit = true;
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(500);
    }
    
    // moon chest, get a moon piece
    // update message and piece collection accordingly
    // make announcement to user
    private void handleMoonChest(int openIndex){
        moonPieceFound++;
        moneyBank += ChestResource.GOLDEN_MOON[moonPieceFound-1];
        String msg = "";
        switch(moonPieceFound){
        case 1: msg = getResources().getString(R.string.moon_piece_found_1);break;
        case 2: msg = getResources().getString(R.string.moon_piece_found_2);break;
        case 3: msg = getResources().getString(R.string.moon_piece_found_3);break;
        case 4: msg = getResources().getString(R.string.moon_piece_found_4);break;
        case 5: msg = getResources().getString(R.string.moon_piece_found_5);break;
        default: break;
        }
        openChest(openIndex, chests.get(openIndex).getChestResource(), msg);
        ImageView iv = new ImageView(pieceLayout.getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        iv.setImageResource(ChestResource.MOON_CHEST_PHOTO_MINI[chests.get(openIndex).getID()]);
        iv.setLayoutParams(params);
        iv.setVisibility(View.VISIBLE);
        pieceLayout.addView(iv);
    }
    
    // get extra key
    // add a key to key bank
    private void handleKeyChest(int openIndex){
        goldKeyRemain++;
        openChest(openIndex, chests.get(openIndex).getChestResource(),"Wow!!! You get a new key, another chance to find treasure");
    }
    
    
    // update money bank according to amount of money inside chest
    private void handleNormalAndEmptyChest(int openIndex){
        moneyBank += chests.get(openIndex).getValue();
        openChest(openIndex, chests.get(openIndex).getChestResource(),"");
    }
    
    // remove a key from view
    // if no keys left, game over and go to victory view
    private void updateKeyView(){
        ((TextView)findViewById(R.id.moneybank)).setText("Gold Bag: $" + moneyFormat.format(moneyBank)+"\nKey Value: $"+moneyFormat.format(goldKeyRemain*ChestResource.KEY_VALUE));
        for(int i=0; i<ChestResource.MAX_KEY; i++){
            if(i<goldKeyRemain) keyLayout.getChildAt(i).setVisibility(View.VISIBLE);
            else keyLayout.getChildAt(i).setVisibility(View.INVISIBLE);
        }
        if(goldKeyRemain==0 || moonPieceFound==5 || trapHit == true) 
            Delay.delayHandler(1000, bonusHandler, new Runnable(){

                @Override
                public void run() {
                    toVictory();
                }
                
            });
        
    }
    
    
    // handle timer for this activity
    private void handleTime(int time){
        bonusTimer = new CountDownTimer(time, 1000) {
            
            @Override
            public void onTick(long millisUntilFinished) {
                ((TextView)findViewById(R.id.timeLeft)).setText("Time Left: \n" + millisUntilFinished/1000);
                timeRem = (int) millisUntilFinished/1000;
            }
            
            @Override
            public void onFinish() {
                ((TextView)findViewById(R.id.timeLeft)).setText("Time Left: \n 0");
                Handler timeHandler = new Handler();
                Delay.delayHandler(3000, timeHandler, new Runnable()
                {@Override public void run() {toVictory();}});
            }
        }.start();
    }
    
    // go to victory
    private void toVictory(){
        Intent toVictory = new Intent(BonusActivity.this, VictoryActivity.class);
        bonusTimer.cancel();
        toVictory.putExtra("money", moneyBank);
        toVictory.putExtra("oDefeat",getIntent().getIntExtra("opponentsDefeat", -1));
        toVictory.putExtra("gDefeat",getIntent().getIntExtra("guestsDefeat", -1));
        toVictory.putExtra("stage",getIntent().getIntExtra("stage", -1));
        if(moonPieceFound==5) toVictory.putExtra("foundAll", true);
        finish();
        startActivity(toVictory);
    }

    
    class PrepareChest extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... arg0) {
            for(int i=0;i<ChestResource.NUMBER_OF_CHESTS;i++){
                if(i<5) 
                    chests.add(new Chest(ChestResource.MOON_CHEST, ChestResource.MOON_CHEST_PHOTO[i], i));
                else if(i<7) 
                    chests.add(new Chest(ChestResource.KEY_CHEST, ChestResource.KEY_CHEST_PHOTO, i));
                else if(i<9) 
                    chests.add(new Chest(ChestResource.TRAP_CHEST, ChestResource.TRAP_CHEST_PHOTO, i));
                else if(i<11) 
                    chests.add(new Chest(ChestResource.EMPTY_CHEST, ChestResource.EMPTY_CHEST_PHOTO, i));
                else if(i<14) 
                    chests.add(new Chest(ChestResource.BIG_CHEST, ChestResource.BIG_CHEST_PHOTO, i));
                else if(i<18) 
                    chests.add(new Chest(ChestResource.MEDIUM_CHEST, ChestResource.MEDIUM_CHEST_PHOTO, i));
                else
                    chests.add(new Chest(ChestResource.SMALL_CHEST, ChestResource.SMALL_CHEST_PHOTO, i));
                
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
                TextView chestNum = new TextView(chestLayout.getContext());
                chestNum.setId(500+i);
                chestNum.setText(""+(i+1));
                chestNum.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
                chestNum.setTypeface(null, Typeface.BOLD);
                chestNum.setPadding(2, 2, 2, 2);
                chestNum.setGravity(Gravity.CENTER);
                chestNum.setTextColor(getResources().getColor(R.color.Red));
                chestNum.setBackgroundResource(R.drawable.gold_chest_close);
                if(i%6 == 0) params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
                else params.addRule(RelativeLayout.RIGHT_OF, 500+i-1);
                if(i >= 6)params.addRule(RelativeLayout.BELOW, 500+i-6);
                chestNum.setLayoutParams(params);
                chestNum.setClickable(true);
                chestView[i] = chestNum;
            }
            Collections.shuffle(chests);
            
            for(int i=0;i<ChestResource.MAX_KEY;i++){
                ImageView keyNum = new ImageView(keyLayout.getContext());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                keyNum.setBackgroundResource(ChestResource.KEY_PHOTO);
                keyNum.setLayoutParams(params);
                keyView[i] = keyNum;
            }
            return null;
        }
        
        @Override
        protected void onPostExecute(Void v){
            for(int i=0;i<ChestResource.NUMBER_OF_CHESTS;i++){
                final int index = i;
                chestView[i].setOnClickListener(new View.OnClickListener() {
                    
                    @Override
                    public void onClick(View v) {
                        
                        chestLayout.getChildAt(index).setVisibility(View.INVISIBLE);
                        goldKeyRemain--;
                        if(chests.get(index).isTrap()) handleTrapChest(index);
                        else if(chests.get(index).isMoon()) handleMoonChest(index);
                        else if(chests.get(index).isKey()) handleKeyChest(index);
                        else handleNormalAndEmptyChest(index);
                        updateKeyView();
                    }
                });
                chestLayout.addView(chestView[i], i);
            }
            chestLayout.setVisibility(View.VISIBLE);
            for(ImageView iv:keyView){
                iv.setVisibility(View.INVISIBLE);
                keyLayout.addView(iv);
            }
            keyLayout.setVisibility(View.VISIBLE);
            moneyBank = getIntent().getIntExtra("score", 0);
            updateKeyView();
        }
    }
    
    @Override
    public void onStop(){
        BackgroundMusic.stopOne(BackgroundResource.BONUS_TRACK);
        super.onStop();
    }
    
    @Override
    public void onPause(){
        BackgroundMusic.stopOne(BackgroundResource.BONUS_TRACK);
        super.onPause();
    }
    

    @Override
    public void onDestroy(){
        BackgroundMusic.stopOne(BackgroundResource.BONUS_TRACK);
        super.onDestroy();
    }
}
