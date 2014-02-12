package com.game.activity;

import com.game.resource.BackgroundResource;
import com.game.utility.BackgroundMusic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * 
 * @author Huy Vu
 * the game main menu
 */

public class MenuActivity extends Activity implements OnClickListener{
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		
		((Button) findViewById(R.id.menuStart)).setOnClickListener(this);
		((Button) findViewById(R.id.menuExit)).setOnClickListener(this);
		((Button) findViewById(R.id.menuGameRule)).setOnClickListener(this);
		((Button) findViewById(R.id.menuRival)).setOnClickListener(this);
	}
	
	@Override
	public void onStart(){
	    super.onStart();
        BackgroundMusic.playOneSong(this, BackgroundResource.MENU_TRACK, BackgroundResource.MENU_TRACK_DATA);
	}

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        BackgroundMusic.stopOne(BackgroundResource.MENU_TRACK);
        switch(v.getId()){
        case R.id.menuStart:
            Intent toStart = new Intent(MenuActivity.this, WelcomeActivity.class);
            startActivity(toStart);
            break;
        case R.id.menuGameRule:
            Intent toRule = new Intent(MenuActivity.this, RuleActivity.class);
            startActivity(toRule);
            break;
        case R.id.menuRival:
            Intent toRivalsInfo = new Intent(MenuActivity.this, RivalsInfoActivity.class);
            startActivity(toRivalsInfo);
            break;
        case R.id.menuExit:
            finish();
            moveTaskToBack(true);
            break;
        default: break;
        }
        
    }
    
    @Override
    public void onStop(){
        BackgroundMusic.stopOne(BackgroundResource.MENU_TRACK);
        super.onStop();
    }
    
    @Override
    public void onPause(){
        BackgroundMusic.stopOne(BackgroundResource.MENU_TRACK);
        super.onPause();
    }
    

    @Override
    public void onDestroy(){
        BackgroundMusic.stopOne(BackgroundResource.MENU_TRACK);
        super.onDestroy();
    }
	

}
