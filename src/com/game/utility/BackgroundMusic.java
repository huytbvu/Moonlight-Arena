package com.game.utility;

/**
 * @author Huy Vu
 * Background music 
 */

import java.util.ArrayList;
import java.util.Random;

import com.game.resource.*;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

public class BackgroundMusic {
    
    private static ArrayList<MediaPlayer> players = new ArrayList<MediaPlayer>();
    private static MediaPlayer[] sidePlayer = new MediaPlayer[BackgroundResource.NUMBER_OF_SIDE_TRACK];
    private static int curTrack = -1;
    
    public static void playOneSong(Context context, int trackID, int songdata){
        sidePlayer[trackID] = MediaPlayer.create(context, songdata);
        sidePlayer[trackID].setLooping(true);
        sidePlayer[trackID].setVolume(50, 50);
        sidePlayer[trackID].start();
    }
    
    public static void stopOne(int trackID){
        if(sidePlayer[trackID]!= null && sidePlayer[trackID].isPlaying())
            sidePlayer[trackID].stop();
    }
    
    public static void pauseOne(int trackID){
        if(sidePlayer[trackID].isPlaying())
            sidePlayer[trackID].pause();
    }
    
    public static void resumeOne(int trackID){
        if(!sidePlayer[trackID].isPlaying()){
            sidePlayer[trackID].seekTo(sidePlayer[trackID].getCurrentPosition());
            sidePlayer[trackID].start();
        }
    }
    
    public static void playListSong(Context context, int[] songIDs, final Random gen){
        players = new ArrayList<MediaPlayer>();
        for(int id:songIDs) 
            players.add(MediaPlayer.create(context, id));
        
        for(int i=0; i<players.size(); i++){
            players.get(i).setOnCompletionListener(new OnCompletionListener() {
                
                @Override
                public void onCompletion(MediaPlayer mp) {
                    players.get(gen.nextInt(players.size())).start();
                }
            });
        }
        
        players.get(gen.nextInt(players.size())).start();
    }
    
    public static void pauseList(){
        for(int i=0; i<players.size(); i++){
            if(players.get(i).isPlaying()){
                curTrack = i;
                players.get(i).pause();
            }
        }
    }
    
    public static void resumeList(){
       players.get(curTrack).seekTo(players.get(curTrack).getCurrentPosition());
       players.get(curTrack).start();
    }
    
    public static void stopAll(){
        for(MediaPlayer mp:players) mp.stop();
    }
    
    public static void resetPlayer(MediaPlayer mp){
        mp.reset();
    }
    
    public static void resetAll(){
        for(MediaPlayer mp:players) resetPlayer(mp);
    }
    
}
