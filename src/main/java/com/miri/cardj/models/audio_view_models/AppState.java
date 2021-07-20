package com.miri.cardj.models.audio_view_models;
/**************************************************************************************
 App State is an interface for all the Fragment classes which are observing threw this class
 **************************************************************************************/
import android.os.Handler;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.miri.cardj.models.SongData;
import com.miri.cardj.models.State;

import java.util.ArrayList;
import java.util.List;

public class AppState extends ViewModel {
    private final MutableLiveData<State> currentState = new MutableLiveData<>();
    private final MutableLiveData<String> searchWords = new MutableLiveData<>();
    private final MutableLiveData<String> userSpokenText = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<SongData>> searchResults = new MutableLiveData<>();
    private final MutableLiveData<String> deviceTextToSay = new MutableLiveData<>();
    private final MutableLiveData<String> songId = new MutableLiveData<>();
    private final MutableLiveData<SongData> songToList = new MutableLiveData<>();
    private final MutableLiveData<List<SongData>> songsPlayList = new MutableLiveData<>();
    private final MutableLiveData<String> messages = new MutableLiveData<>();
    private final MutableLiveData<String> videoId = new MutableLiveData<>();
    private String wordsText;


    public MutableLiveData<List<SongData>> getSongsPlayList(){return songsPlayList;}
    public MutableLiveData<SongData> getSongToList(){return songToList;}
    public MutableLiveData<String> getSongId(){ return songId;}
    public String getWordsText() {
        return wordsText;
    }
    public MutableLiveData<String> getSearchWords(){ return searchWords;}
    public MutableLiveData<String>getDeviceTextToSay() {
        return deviceTextToSay;
    }
    public MutableLiveData<String> getUserSpokenText() {
        return userSpokenText;
    }
    public MutableLiveData<ArrayList<SongData>> getSearchResults(){ return searchResults;}
    public MutableLiveData<String> getMessages(){ return  messages;}
    public MutableLiveData<String> getVideoId(){ return videoId;}
    public void setAppState(State state) {
        this.currentState.postValue(state);;
    }
    public MutableLiveData<State> getCurrentState(){
        return currentState;
    }
    public void getWordsText(MutableLiveData<String> callback){
        callback.postValue(wordsText);
    }
    public void delay(int seconds, State state){
        System.out.println("call to change state");
        Handler h = new Handler();
        Runnable time = new Runnable() {
            @Override
            public void run() {
                setAppState(state);
                System.out.println("perform state change");
            }
        };
        h.postDelayed(time, seconds);
    }
}

