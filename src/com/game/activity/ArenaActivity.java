package com.game.activity;

import com.game.resource.*;
import com.game.utility.*;

import java.io.IOException;
import java.util.*;

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
import android.view.*;
import android.widget.*;

/**
 * @author Huy Vu
 * Main Arena Activity
 * Moonlight Arena
 */

public class ArenaActivity extends Activity {


    // text view of question, score, stage, opponents left
	private TextView scoreLabel, stageLabel, oppoLabel;
	
	// TRUE if the help has been used
	private boolean helper_50 = false;
	private boolean helper_Change = false;
	private boolean helper_Poll = false;
	
	// TRUE if player eliminates 90+ normal opponents
	private boolean atMark = false;
	
	// display of opponents
	private RelativeLayout opponentDisplay;
	private ImageView[] opponentView = new ImageView[GlobalResource.NUMBER_OF_CONTESTANT];
    private Contestant[] rivals = new Contestant[GlobalResource.NUMBER_OF_CONTESTANT];
    private SpecialContestant[] special_rivals = new SpecialContestant[4];
	
	// guest ID
	private int[] guest_ID;
	
	// 4 answering buttons
	private Button[] answer = new Button[4];
	
	// a basic handler for delaying
	private Handler timeHandler = new Handler();
	
	// hold all question and randomize them
	private ArrayList<ArrayList<Question>> qdb = new ArrayList<ArrayList<Question>>();
	
	// count down timer
	private CountDownTimer cdTimer;
	
	// Game attribute
	private int score = 0;
	private int opponents = 100;
	private int eliminated = 0;
	private int stage = 0;
	private int userClick = -1;
	private int goldKeyCount = GlobalResource.GOLD_KEY_TO_START;
	private int guestElimiate = 0;
	private int timeRem = -1;
	private Question currentQuestion = null;
	
	// a random generator
	private Random gen = new Random();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_arena);
		BackgroundMusic.playListSong(this, BackgroundResource.MAIN_GAME_MUSIC, gen);
        scoreLabel = (TextView)findViewById(R.id.score);
        oppoLabel = (TextView)findViewById(R.id.oppoCnt);
        stageLabel = (TextView)findViewById(R.id.stage);
		try {
			qdb.add(QuestionDatabase.importQuestionToDB(getAssets().open(GlobalResource.EASY_QUESTION_FILE), gen));
			qdb.add(QuestionDatabase.importQuestionToDB(getAssets().open(GlobalResource.MEDIUM_QUESTION_FILE),gen));
			qdb.add(QuestionDatabase.importQuestionToDB(getAssets().open(GlobalResource.HARD_QUESTION_FILE),gen));
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		setupButton();
		Delay.delayHandler(1500, timeHandler, new Runnable(){

            @Override
            public void run() {
                setVisibleAll();
                initialSetup();
                new LoadOpponentImage().execute();
            }
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.arena, menu);
		return true;
	}
	
	private void setVisibleAll(){
	    ((TextView)findViewById(R.id.question_tv_sreen)).setVisibility(View.VISIBLE);
        scoreLabel.setVisibility(View.VISIBLE);
        oppoLabel.setVisibility(View.VISIBLE);
        stageLabel.setVisibility(View.VISIBLE);
        findViewById(R.id.help50).setVisibility(View.VISIBLE);
        findViewById(R.id.helpChange).setVisibility(View.VISIBLE);
        findViewById(R.id.helpPoll).setVisibility(View.VISIBLE);
        findViewById(R.id.questionTopicDisplay).setVisibility(View.VISIBLE);
        findViewById(R.id.helpPoll).setVisibility(View.VISIBLE);
        findViewById(R.id.timeRemain).setVisibility(View.VISIBLE);
        findViewById(R.id.question_tv_sreen).setVisibility(View.VISIBLE);
        findViewById(R.id.choicePanel).setVisibility(View.VISIBLE);
        findViewById(R.id.ingameStopButton).setVisibility(View.VISIBLE);
	}

	// initial set up
	private void initialSetup(){
		for(ArrayList<Question> arr:qdb)
			Collections.shuffle(arr);
		setupResource();
		setupContestants();
		setupSpecialContestants();
		displayStatus();
		handleOneQuestion();
	}
	
	// assign ONE question based on level
	// allow each contester/guest to answer 
	private void handleOneQuestion(){
		updateStatus();
		int questionLevel = 0;
		if(stage > 12) questionLevel = 2;
		else if(stage > 6) questionLevel = 1;
		
        if(stage > 5) for(SpecialContestant sc:special_rivals) sc.turnDownImmunity(0);
        else if(stage > 3) for(SpecialContestant sc:special_rivals) sc.turnDownImmunity(gen.nextInt(2));
        
		currentQuestion = qdb.get(questionLevel).get(0);
		displayQuestion(currentQuestion);
		if(opponents <= 10 && atMark==false){
		    bonusMark();
		    atMark = true;
		    Delay.delayHandler(6000, timeHandler, new Runnable(){

                @Override
                public void run() {
                    prepareTimer(GlobalResource.TIME_LIMIT);
                }
		        
		    });
		}else prepareTimer(GlobalResource.TIME_LIMIT);
		qdb.get(questionLevel).add(currentQuestion);
		qdb.get(questionLevel).remove(0);
		((TextView)findViewById(R.id.questionTopicDisplay)).setText(currentQuestion.getTopicDisplay());
		for(Contestant c:rivals) if(c.stillInGame()) c.answer(currentQuestion,gen,stage);
		for(SpecialContestant sc:special_rivals) if(sc.stillInGame()) sc.answer(currentQuestion,gen,stage);
	}
	
	private void bonusMark(){
	    BackgroundMusic.pauseList();
	    BackgroundMusic.playOneSong(this, 
	            BackgroundResource.BONUS_MARK_TRACK, BackgroundResource.BONUS_MARK_TRACK_DATA);
	    
	    LayoutInflater inflater = getLayoutInflater();
	    View layout = inflater.inflate(R.layout.bonus_mark_layout, (ViewGroup) findViewById(R.id.guest_toast_layout_id));

	    ((ImageView) layout.findViewById(R.id.rival_remain)).setImageResource(R.drawable.bonus_mark_90);
        ((TextView) layout.findViewById(R.id.rival_remain_desc)).setText(getResources().getString(R.string.bonus_mark));

	    Toast toast = new Toast(getApplicationContext());
	    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
	    toast.setDuration(Toast.LENGTH_SHORT*13/4);
	    toast.setView(layout);
	    toast.show();

	    Delay.delayHandler(6500, timeHandler, new Runnable(){

            @Override
            public void run() {
                BackgroundMusic.stopOne(BackgroundResource.BONUS_MARK_TRACK);
                BackgroundMusic.resumeList();
            }
	        
	    });
	}
	
	// set up main buttons
	private void setupButton(){
		answer[0] = (Button)findViewById(R.id.choice1);
		answer[1] = (Button)findViewById(R.id.choice2);
		answer[2] = (Button)findViewById(R.id.choice3);
		answer[3] = (Button)findViewById(R.id.choice4);
		
		answer[0].setOnClickListener(new View.OnClickListener() 
		{public void onClick(View v) {userClick = 0;checkAnswer();}});
		
		answer[1].setOnClickListener(new View.OnClickListener() 
		{public void onClick(View v) {userClick = 1;checkAnswer();}});
		
		answer[2].setOnClickListener(new View.OnClickListener() 
		{public void onClick(View v) {userClick = 2;checkAnswer();}});
		
		answer[3].setOnClickListener(new View.OnClickListener()
		{public void onClick(View v) {userClick = 3;checkAnswer();}});
		
		((ImageButton)findViewById(R.id.help50)).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(!helper_50){
					helper_50 = true;
					handleHelp50();
				}else handleDuplicateHelp("50/50");
			}});
		
		
		((ImageButton)findViewById(R.id.helpChange)).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(!helper_Change){
					helper_Change = true;
					handleHelpChange();
				}else handleDuplicateHelp("Change Question");
			}});
		
		((ImageButton)findViewById(R.id.helpPoll)).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(!helper_Poll){
					helper_Poll = true;
					handleHelpPoll();
				}else handleDuplicateHelp("Polling");
			}});
		
		((Button)findViewById(R.id.ingameStopButton)).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				stopGamePopUp();
			}
		});
	}
	
	// set up the normal 100 contestants
	private void setupContestants(){
		for(int i=0; i<100; i++){
			int playerType = gen.nextInt(3);
			switch(playerType){
			case 0: rivals[i] = new Contestant(GlobalResource.FIRST_TIME_CONTESTANT,i); break;
			case 1: rivals[i] = new Contestant(GlobalResource.EXPERIENCE_CONTESTANT,i); break;
			case 2: rivals[i] = new Contestant(GlobalResource.SMART_CONTESTANT,i); break;
			default: break;
			}
		}
		
	}
	
	// set up the 4 special guests
	private void setupSpecialContestants(){
	    guest_ID = getIntent().getIntArrayExtra("guestList");
	    
	    for(int i=0; i<4; i++)
	    special_rivals[i] = new SpecialContestant(
                getString(SpecialContestantCollection.guest_name[guest_ID[i]]), 
                SpecialContestantCollection.guest_specialty_1[guest_ID[i]], 
                SpecialContestantCollection.guest_specialty_2[guest_ID[i]],
                guest_ID[i]);
        
	}
	
	// reset all attributes
	private void setupResource(){
		score = 0;
		opponents = 100;
		stage = 0;
		helper_50 = false;
		helper_Change = false;
		helper_Poll = false;
	}
	
	// handle the 50% Chance Help
	// eliminate two incorrect answers
	private void handleHelp50(){
		int wrong1 = (currentQuestion.getCorrectAnswer() + gen.nextInt(3) + 1) % 4;
		int wrong2;
		do{
			wrong2 = (currentQuestion.getCorrectAnswer() + gen.nextInt(3) + 1) % 4;
		}while(wrong2==wrong1);
		answer[wrong1].setText("");
		answer[wrong2].setText("");
		Toast.makeText(getApplicationContext(), "Two wrong answers have been eliminated", Toast.LENGTH_LONG).show();
	}
	
	// handle the Change Help
	// change to another question at the same level
	private void handleHelpChange(){
		Toast.makeText(getApplicationContext(), "You have change question", Toast.LENGTH_LONG).show();
		stage--;
		cdTimer.cancel();
		handleOneQuestion();
	}
	
	// handle the Poll Help
	// get the answer poll from the remaining player for this question
	private void handleHelpPoll(){
		int[] pollcount = {0,0,0,0};
		for(Contestant c:rivals){
			if(c.stillInGame()) pollcount[c.getAnswer()]++;
		}
		String poll = pollcount[0] + " contestants choose option A \n";
		poll += pollcount[1] + " contestants choose option B \n";
		poll += pollcount[2] + " contestants choose option C \n";
		poll += pollcount[3] + " contestants choose option D \n";
		Toast.makeText(getApplicationContext(), poll, Toast.LENGTH_LONG).show();
	}
	
	// handle situation where player want to use a help twice
	private void handleDuplicateHelp(String helpName){
		AlertDialog.Builder popup = new AlertDialog.Builder(this);
		popup.setCancelable(true);
		popup.setPositiveButton("Click to Proceed",
            new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {dialog.cancel();}});
		popup.setMessage("You have already use "+helpName+" help");
		popup.create().show();
	}
	
	// display question title, choices and topic
	private void displayQuestion(Question q){
		((TextView)findViewById(R.id.question_tv_sreen)).setText(q.getTitle());
		answer[0].setText(q.getAllChoices()[0].getText());
		answer[1].setText(q.getAllChoices()[1].getText());
		answer[2].setText(q.getAllChoices()[2].getText());
		answer[3].setText(q.getAllChoices()[3].getText());
	}
	
	// update status information
	private void updateStatus(){
		int remain = 0;
		for(Contestant c: rivals) if(c.stillInGame()) remain++;
		eliminated = opponents - remain;
		score += calculateScore(opponents, eliminated);
		opponents = remain;
		userClick = -1;
		stage++;
		if(opponents==0){
		    goldKeyCount++;
		    if(guestElimiate==4) goldKeyCount++;
		    toBonus();
		}
		displayStatus();
	}
	
	// display current information (score, remaining opponents, stage)
	private void displayStatus(){
		scoreLabel.setText("score: "+score);
		oppoLabel.setText("Opponents Left: "+opponents);
		stageLabel.setText("Stage: "+stage);
	}
	
	private void stopGamePopUp(){
		cdTimer.cancel();
	    
		AlertDialog.Builder contLabel = new AlertDialog.Builder(this);
		contLabel.setCancelable(true);
		contLabel.setMessage("Are you sure you want to stop the arena now?");
		contLabel.setNegativeButton("NO, bring the heat on",
			    new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
				    dialog.cancel();
				    prepareTimer(timeRem*1000);
					
	        }});
		contLabel.setPositiveButton("YES, I'm overwhelmed",
		    new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
			    dialog.cancel();
			    if(atMark) toBonus();
                else toEndGame();
        }});
		contLabel.create().show();
	}
	
	// check player's answer
	// if correct, continue game and eliminate incorrect players
	// if guests answer incorrectly they are eliminated and announced
	// main player then gets the special effect from guests
	// if wrong, to end game
	private void checkAnswer(){
	    cdTimer.cancel();
	    Toast.makeText(getApplicationContext(), "Checking answer & eliminating players", Toast.LENGTH_SHORT).show(); 
	    
		AlertDialog.Builder resultLabel = new AlertDialog.Builder(this);
		resultLabel.setCancelable(true);
		String proceedMSG = "";
		if(userClick==currentQuestion.getCorrectAnswer()){
			resultLabel.setMessage("EXCELLENT!!! You're through the next stage");
			proceedMSG = "To Stage " + (stage+1);
		}
        else{
        	resultLabel.setMessage("OOPS!!! You've missed it");
        	Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        	v.vibrate(500);
        	proceedMSG = "OK";
        }
		resultLabel.setPositiveButton(proceedMSG,
		    new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
			    dialog.cancel();
				if(userClick==currentQuestion.getCorrectAnswer()){
				    int toWait = 0;
				    
				    for(Contestant c:rivals){
		                if(c.getAnswer()!=currentQuestion.getCorrectAnswer()){
		                    c.eliminated();
		                    opponentDisplay.findViewById(1000+c.getContestantID()).setVisibility(View.INVISIBLE);
		                }
		            }
				    
				    for(SpecialContestant sc:special_rivals){
		                if(sc.stillInGame() && sc.getAnswer()!=currentQuestion.getCorrectAnswer()){
		                    sc.eliminated();
                            guestElimiate++;
		                    handleGuestElimination(sc);
		                    goldKeyCount++;
		                    toWait++;
		                }
		            }
				    
				    Delay.delayHandler((long)toWait*3500, timeHandler, new Runnable(){
				        @Override
                        public void run() {
                            handleOneQuestion();
                        }});
				}
				else{
				    explodeMoon();
				}
        }});
        resultLabel.create().show();
	}
	
	// pop up window when a guest is eliminated
	private void handleGuestElimination(SpecialContestant sc){
	    int bonusPoint = 0;
	    String guestMark = "";
	    switch(guestElimiate){
	    case 1: guestMark = "First"; bonusPoint = 250; break;
	    case 2: guestMark = "Second"; bonusPoint = 500; break;
	    case 3: guestMark = "Third"; bonusPoint = 1000; break;
	    case 4: guestMark = "Final"; bonusPoint = 2000; break;
	    default: break;
	    }
	    score += bonusPoint;
	    LayoutInflater inflater = getLayoutInflater();
	    View layout = inflater.inflate(R.layout.guest_toast, (ViewGroup) findViewById(R.id.guest_toast_layout_id));

	    ((ImageView) layout.findViewById(R.id.eliminated_guest)).setImageResource(SpecialContestantCollection.guest_photo[sc.getContestantID()]);
	    guestMark += " guest is eliminated:\n" + sc.getContestantName().toUpperCase() +".\n";
	    guestMark += "You score an extra " + bonusPoint;
	    ((TextView) layout.findViewById(R.id.eliminated_msg)).setText(guestMark);

	    Toast toast = new Toast(getApplicationContext());
	    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
	    toast.setDuration(Toast.LENGTH_LONG);
	    toast.setView(layout);
	    toast.show();
	}
	
	// jump to End Game
	private void toEndGame(){
		Intent toEndGame = new Intent(ArenaActivity.this,EndGameActivity.class);
		toEndGame.putExtra("score", score);
        toEndGame.putExtra("opponentsLeft", opponents);
        toEndGame.putExtra("stage", stage);
		finish();
		BackgroundMusic.stopAll();
		startActivity(toEndGame);
	}
	
	// jump to Bonus Activity
	private void toBonus(){
	    Intent toBonus = new Intent(ArenaActivity.this,BonusActivity.class);
	    toBonus.putExtra("GoldKeyCount", goldKeyCount);
        toBonus.putExtra("score", score);
        toBonus.putExtra("opponentsDefeat", (GlobalResource.NUMBER_OF_CONTESTANT - opponents));
        toBonus.putExtra("guestsDefeat", guestElimiate);
        toBonus.putExtra("stage", stage);
        finish();
        BackgroundMusic.stopAll();
        startActivity(toBonus);
	}
	
	// calculate score gained from the past round
	private int calculateScore(int opponentStart, int opponentEliminated){
		if(opponentEliminated*2 >= opponentStart && opponentStart >= 10) 
			return (1200*opponentEliminated/opponentStart);
		return (1000*opponentEliminated/opponentStart);
	}
	
	// set up a count down timer
	private void prepareTimer(int timeLeft){
	   cdTimer = new CountDownTimer(timeLeft, 1000) {

	        public void onTick(long millisUntilFinished) {
	            ((TextView) findViewById(R.id.timeRemain)).setText(""+millisUntilFinished/1000);
	            timeRem = (int)millisUntilFinished/1000;
	        }

	        public void onFinish() {
	            ((TextView) findViewById(R.id.timeRemain)).setText("0");
	            explodeMoon();
	        }
	     }.start();
	     

	}

	private void explodeMoon(){
	    LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.guest_toast, (ViewGroup) findViewById(R.id.guest_toast_layout_id));

        ((ImageView) layout.findViewById(R.id.eliminated_guest)).setImageResource(R.drawable.explode_moon);
        ((TextView) layout.findViewById(R.id.eliminated_msg)).setText("You could not answer this question and the moon has EXPLODED");

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
        Delay.delayHandler(3500, timeHandler, new Runnable() {
            
            @Override
            public void run() {
                if(atMark) toBonus();
                else toEndGame();
            }
        });
    }
	
	/**
	 * load all opponents images in background
	 * display when done
	 * opponent image are randomized from a chosen set
	 */
	class LoadOpponentImage extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... arg0) {
            opponentDisplay = (RelativeLayout) findViewById(R.id.opponentLayout);
            int curID = 1000;
            for(int i=0; i<GlobalResource.NUMBER_OF_CONTESTANT; i++){
                final ImageView person = new ImageView(opponentDisplay.getContext());
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
                person.setId(curID);
                switch(gen.nextInt(6)){
                    case 0: person.setImageResource(R.drawable.human_1); break;
                    case 1: person.setImageResource(R.drawable.human_2); break;
                    case 2: person.setImageResource(R.drawable.human_3); break;
                    case 3: person.setImageResource(R.drawable.human_4); break;
                    case 4: person.setImageResource(R.drawable.human_5); break;
                    case 5: person.setImageResource(R.drawable.human_6); break;
                    default: break;
                }
                if(curID%20 == 0) params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
                else params.addRule(RelativeLayout.RIGHT_OF, curID-1);
                if(curID >= 1020)params.addRule(RelativeLayout.BELOW, curID-20);
                person.setLayoutParams(params);
                opponentView[i] = person;
                curID++;
            }
            return null;
        }
        
        @Override
        protected void onPostExecute(Void v){
            for(int i=0; i<100; i++) opponentDisplay.addView(opponentView[i],i);
            opponentDisplay.setVisibility(View.VISIBLE);
        }
	    
	}
	
	@Override
	public void onStop(){
	    cdTimer.cancel();
	    BackgroundMusic.stopAll();
	    super.onStop();
	}
	
	@Override
    public void onPause(){
        cdTimer.cancel();
        BackgroundMusic.stopAll();
        super.onPause();
    }
	

    @Override
    public void onDestroy(){
        cdTimer.cancel();
        BackgroundMusic.stopAll();
        super.onDestroy();
    }
}
