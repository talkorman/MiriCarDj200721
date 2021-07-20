package com.miri.cardj.models.audio_view_models;
/**************************************************************************************
 Device Say Fragment - controlling the speach speach audio and UI
 **************************************************************************************/
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.miri.cardj.R;
import com.miri.cardj.models.SongData;
import com.miri.cardj.models.State;

import org.w3c.dom.ls.LSOutput;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DeviceSayFragment extends Fragment {
    private EditText etText;
    private TextToSpeech textToSpeech;
    static public int resultNumber;
    private ArrayList<SongData> results;
    private AppState appState;
    private String searchWords;
    private InputMethodManager imm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       final View root =  inflater.inflate(R.layout.fragment_device_audio, container, false);
        etText = root.findViewById(R.id.etText);
    return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        resultNumber = 0;
        imm = (InputMethodManager)getContext()
                        .getApplicationContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
        deactivateGboard();   //without first activation call, even the second will not cause it to be activated.
            appState = new ViewModelProvider(requireActivity()).get(AppState.class);
        textToSpeech = new TextToSpeech(getContext(),
                new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if(status == TextToSpeech.SUCCESS){
                            int lang = textToSpeech.setLanguage(Locale.US);
                            if(lang == TextToSpeech.LANG_MISSING_DATA||lang == TextToSpeech.LANG_NOT_SUPPORTED){
                                Toast.makeText(getContext(), "Text to Speach Language not supported", Toast.LENGTH_LONG).show();
                            }
                            textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                                @Override
                                public void onStart(String utteranceId) {
                                }

                                @Override
                                public void onDone(String utteranceId) {
                                    State state = appState.getCurrentState().getValue();


                                    if(state == State.GREETING){
                                        appState.setAppState(State.REJECT);
                                        return;
                                    }
                                    if(state == State.ASKING_FOR_SEARCH || state == State.SEARCH_INPUT){
                                        appState.setAppState(State.SEARCH);
                                        appState.getSearchWords().postValue(searchWords);
                                        return;
                                    }
                                    if(state == State.SHOW_RESULTS
                                            || state == State.USER_SAID_NO){
                                        if(state == State.USER_SAID_NO) resultNumber++;
                                        appState.setAppState(State.DEVICE_ALREADY_SAID_RESULT);
                                        return;
                                    }
                                    if(state == State.REPEAT_RESULTS){
                                        appState.setAppState(State.USER_SAID_NO);
                                        if(resultNumber > 0) resultNumber--;
                                    }
                                    if (state == State.AGAIN) {
                                        appState.setAppState(State.OPENING);
                                    }
                                    if(state == State.ENTER_INPUT){
                                        activateGboard();
                                    }
                                    if(state == State.EMPTY_LIST || state == State.NO_RESULTS){
                                        appState.setAppState(State.OPENING);
                                    }
                                }



                                @Override
                                public void onError(String utteranceId) {

                                }
                            });
                        }
                    }
                });
        appState.getDeviceTextToSay().observe(getViewLifecycleOwner(), (text)->{
            StringBuilder fullText = new StringBuilder();
            searchWords = text;
                fullText.append("I'm going to search for ").append(text);
            speak(fullText.toString());
            etText.setText(fullText.toString());
            appState.setAppState(State.ASKING_FOR_SEARCH);
        });
        appState.getCurrentState().observe(getViewLifecycleOwner(), (state)->{
            if(state == State.GREETING){
                greeting();
                return;
            }
            if(state == State.USER_SPEAK
                    || state == State.START
                    || state == State.INTRO){
                textToSpeech.stop();
                return;
            }
            if(state == State.USER_SAID_NO){
                sayResults();
                return;
            }
            if(state == State.USER_SAID_YES){
                try {
                    appState.getSongId().postValue(results.get(resultNumber).getVideoId());
                }catch (Exception e){
                    e.printStackTrace();
                }
                return;
            }
            if(state == State.ADDTO_PLAYLIST){
                toPlayList();
                return;
            }
            if(state == State.DISPLAY_PLAYLIST){
                speak("playing play list");
                return;
            }
            if(state == State.USER_DIDNT_CHOSE && results.size() > 0){
                speak("I repeat");
                appState.setAppState(State.REPEAT_RESULTS);
                //resultNumber = 0;
                return;
            }
            if(state == State.DELETE_ALL){
                speak("I'm deleting all the play list");
                return;
            }
            if(state == State.OPENING ){
                textToSpeech.stop();
                deactivateGboard();
                return;
            }
            if(state == State.AGAIN){
                speak("Choose again");
                resultNumber = 0;
                return;
            }
            if(state == State.ENTER_INPUT){
                activateGboard();
                speak("please input a song in your own language");
                return;
            }
            if(state == State.SEARCH_INPUT){
                searchWords = etText.getText().toString();
                speak("I am going to search for " + searchWords);
                return;
            }
            if(state == State.EMPTY_LIST){
                speak("the play list is empty!");
                return;
            }
            if(state == State.NO_RESULTS){
                speak("there is a problem to get results. please check internet connection or contact support");
            }
        });
        appState.getSearchResults().observe(getViewLifecycleOwner(), (results)->{
            try {
                if (results.size() == 0 || results.get(0).getVideoId().equals("no.1")
                || appState.getCurrentState().getValue() == State.NO_RESULTS) return;
                this.results = results;
                resultNumber = 0;
                sayResults();
            }catch (IndexOutOfBoundsException e){
            }
        });
        appState.getMessages().observe(getViewLifecycleOwner(), (message)->{
            speak(message);
            if(appState.getCurrentState().getValue() == State.USER_DIDNT_CHOSE)
                appState.setAppState(State.SONG_STARTED);
        });

    }
    public void sayResults(){
        if(resultNumber < results.size()){
            String words = results.get(resultNumber).getTitle();
            boolean atleastOneAlpha = words.matches(".*[a-zA-Z]+.*");
            if(!atleastOneAlpha)words = "result number " + (resultNumber + 1);
            speak(words);
        }else{
            System.out.println("result eccsess the limit");
            speak("please chose again");
            resultNumber = 0;
            appState.setAppState(State.OPENING);
        }
    }
    public void speak(String s){
        String sDisplay = s;
        if(s.length() > 50)sDisplay = s.substring(0, 50);
        etText.setText(sDisplay);
        textToSpeech.speak(s, TextToSpeech.QUEUE_FLUSH, null ,TextToSpeech.ACTION_TTS_QUEUE_PROCESSING_COMPLETED);
    }
    public void greeting(){
        LocalTime morning = LocalTime.of(4, 0);
        LocalTime noon = LocalTime.of(12, 0);
        LocalTime evening = LocalTime.of(17, 0);
        LocalTime night = LocalTime.of(21, 0);
        LocalTime midnight = LocalTime.of(0, 0);
        LocalTime currentTime = LocalTime.now();
        String time = "";
        if(currentTime.isAfter(morning))time = "morning";
        if(currentTime.isAfter(noon))time = "afternoon";
        if(currentTime.isAfter(evening))time = "evening";
        else if(currentTime.isAfter(evening) || currentTime.isBefore(morning))time = "night";

        System.out.println(time);
        speak("Good " + time + "!, My name is Miri. I will search for you and play your favorate songs. " +
                "Please just tell me which song do you like and please " +
                "concentrate on the road. have a safty drive and a pleasent jurney!");
    }
    public void toPlayList(){
        try {
            appState.getSongToList().postValue(results.get(resultNumber));
            speak("Added to play list");
            resultNumber = 0;
        }catch (Exception e){
            System.out.println(e);
        }
    }
    private void activateGboard(){
        etText.setText("");
        etText.setEnabled(true);
        etText.requestFocus();
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                InputMethodManager.SHOW_FORCED);

    }
    private void deactivateGboard(){
        etText.setEnabled(false);
            etText.clearFocus();
    }
}