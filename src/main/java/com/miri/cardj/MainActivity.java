 package com.miri.cardj;
/**************************************************************************************
 Main Activity is activating the dynamic fragments of Player, Play List and Results.
 **************************************************************************************/
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.miri.cardj.R;
import com.miri.cardj.models.SongData;
import com.miri.cardj.models.audio_view_models.AppState;
import com.miri.cardj.models.play_lists.PlayListViewModel;
import com.miri.cardj.models.State;
import com.miri.cardj.view_play_list.PlayListFragment;
import com.miri.cardj.view_player.PlayerFragment;
import com.miri.cardj.view_search_results.ResultsFragment;

import java.util.List;

public class MainActivity extends AppCompatActivity implements PlayerFragment.OnSongEndListener {
    //properties declarations
    AppState appState;
    State state;
    boolean userDidChoose;
    PlayListViewModel playListViewModel;
    List<SongData> songs;
    public static int playListIndex;
    int playCounter;
    TextView instructions;
    TextView tutorial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
            setContentView(R.layout.activity_main);
        //properties setup
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        playCounter = 1000;
        appState = new ViewModelProvider(this).get(AppState.class);
        playListViewModel = new ViewModelProvider(this).get(PlayListViewModel.class);
        appState.setAppState(State.INTRO);
        displaySong("J0C5IG3FCA0");
        instructions = findViewById(R.id.tvInstructions);
        tutorial = findViewById(R.id.tvTutorial);
        instructions.setVisibility(View.INVISIBLE);
        tutorial.setVisibility(View.INVISIBLE);
        appState.getCurrentState().observe(this, state->{
            if(state == State.BEFORE_ADD_PLAYLIST){
                closePLayList();
            }
            if(state == State.ADDTO_PLAYLIST){
                showPlayList();
            }
            if(state == State.EMPTY_DISPLAY){
                closePlayer();
                instructions.setVisibility(View.VISIBLE);
                tutorial.setVisibility(View.VISIBLE);
            }
            if(state == State.START) {
                instructions.setVisibility(View.INVISIBLE);
                tutorial.setVisibility(View.INVISIBLE);
                displaySong("");
                showPlayList();
                ImageView img = findViewById(R.id.imageView);
                AlphaAnimation alpha = new AlphaAnimation(1.0f, 0.6f);
                alpha.setDuration(5000);
                alpha.setFillAfter(true);
                img.startAnimation(alpha);
                appState.getCurrentState().postValue(State.OPENING);
            }
            if(state == State.SEARCH)showSearchResults();
            if(state == State.USER_SAID_NO
                    || state == State.USER_SAID_YES
                    || state == State.ADDTO_PLAYLIST
                    || state == State.USER_DIDNT_CHOSE
                    || state == State.AGAIN)
                userDidChoose = true;
            if(state == State.DEVICE_ALREADY_SAID_RESULT){
                userDidChoose = false;
                int delay = 5000;
                Handler h = new Handler();
                Runnable resultTimeWindow = new Runnable() {
                    @Override
                    public void run() {
                        if(!userDidChoose) {
                            appState.setAppState(State.USER_DIDNT_CHOSE);
                        }
                    }
                };
                h.postDelayed(resultTimeWindow, delay);
            }
            if(state == State.SONG_STARTED){
                try {
                    displaySong(songs.get(playListIndex).getVideoId());
                }catch (IndexOutOfBoundsException e){
                    Toast.makeText(this, "The play list is empty", Toast.LENGTH_LONG).show();
                    appState.setAppState(State.EMPTY_LIST);
                }
            }
            if(state == State.SONG_ENDED){

            nextSong(1);
            }
            if(state == State.NEXT){
            onSongEnd(true);
            nextSong(1);
            }
            if(state == State.GO_BACK){
            onSongEnd(true);
            nextSong(-1);
            }
            if(state == State.NO_RESULTS){
                closeSearchResults();
            }
        });

        appState.getSongId().observe(this, this::displaySong);
        appState.getSongsPlayList().observe(this, songs->{
            this.songs = songs;
            appState.setAppState(State.SONG_STARTED);
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    private void nextSong(int x){
        if(songs == null){
            displaySong("");
            appState.setAppState(State.OPENING);
            return;
        }
            playCounter += x;
            playListIndex = playCounter % songs.size();
            appState.setAppState(State.SONG_STARTED);
    }
    public void displaySong(String song){
        appState.getVideoId().postValue(song);  //for song deleting from playlist
        closePlayer();
        PlayerFragment player = PlayerFragment.newInstance(song);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.youtubeplayerfragment, player).commit();
    }


    @Override
    public void onSongEnd(Boolean songEnded) {
        //appState.;
        appState.setAppState(State.SONG_ENDED);
    }

    public void closePlayer(){
        FragmentManager fm = getSupportFragmentManager();
        PlayerFragment player = (PlayerFragment)fm.findFragmentById(R.id.youtubeplayerfragment);
        if(player != null){
            FragmentTransaction ft = fm.beginTransaction();
            ft.remove(player).commit();
        }
    }
    public void closeSearchResults(){
        FragmentManager fm = getSupportFragmentManager();
        ResultsFragment resultsFragment = (ResultsFragment) fm.findFragmentById(R.id.results_view);
        if(resultsFragment != null){
            FragmentTransaction ft = fm.beginTransaction();
            ft.remove(resultsFragment).commit();
        }
    }
    public void showSearchResults(){
        closeSearchResults();
        ResultsFragment resultsFragment = ResultsFragment.newInstance();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.results_view, resultsFragment).commit();
    }

    public void showPlayList(){
        PlayListFragment playListFragment = PlayListFragment.newInstance();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.play_list_view, playListFragment).commit();
    }

    public void closePLayList(){
        FragmentManager fm = getSupportFragmentManager();
        PlayListFragment playListFragment = (PlayListFragment) fm.findFragmentById(R.id.play_list_view);
        if(playListFragment != null){
            FragmentTransaction ft = fm.beginTransaction();
            ft.remove(playListFragment).commit();
            appState.setAppState(State.ADDTO_PLAYLIST);
        }
    }


}