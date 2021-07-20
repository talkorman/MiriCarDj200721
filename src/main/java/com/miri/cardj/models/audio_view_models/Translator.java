package com.miri.cardj.models.audio_view_models;
/**************************************************************************************
 Translator translates every words and centences spoken by the user and affecting the app accordingly.
 **************************************************************************************/
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.miri.cardj.MainActivity;
import com.miri.cardj.R;
import com.miri.cardj.models.State;
import com.miri.cardj.view_play_list.PlayListFragment;
import com.miri.cardj.view_search_results.ResultsAdapter;

import java.util.Objects;

public class Translator extends Fragment {
    private int countSayResults;
    private StringBuilder voice;
    private StringBuilder searchingString;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_translate, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppState appState = new ViewModelProvider(requireActivity()).get(AppState.class);
        appState.getUserSpokenText().observe(getViewLifecycleOwner(), (s)->{
            voice = new StringBuilder();
            searchingString = new StringBuilder();
            String words = s.toLowerCase();

            System.out.println(words);
            State state = appState.getCurrentState().getValue();
            if(words.contains("check") || words.contains("jack")){
                appState.setAppState(State.SEARCH_INPUT);
                return;
            }
            if (words.contains("i want") || words.contains("i won't")) {
                if(words.length() < 8) return;

                countSayResults = 0;
                voice.setLength(0);
                searchingString.setLength(0);
                searchingString.append(words);
                searchingString.delete(0, 7);
                String item = searchingString.toString();
                item.replace(".", "/").replace(",", "/").replace(" ", "/");
                voice.append(item);
                System.out.println(item);
                appState.getDeviceTextToSay().postValue(voice.toString());
                //appState.getSearchWords().postValue(item);
                return;
            }
            if(words.contains("no") && state == State.DEVICE_ALREADY_SAID_RESULT){
                appState.setAppState(State.USER_SAID_NO);
                return;
            }
            if(words.contains("yes") && state == State.DEVICE_ALREADY_SAID_RESULT){
                appState.setAppState(State.USER_SAID_YES);
                return;
            }
            if(words.contains("next result") && state == State.OPENING){
                appState.setAppState(State.USER_SAID_NO);
                return;
            }
            if(words.contains("number")){
                String[] number = words.split(" ");
                if(number.length < 2)return;
                int chosenIndex = textNumToDigitNum(number[number.length - 1]);
                if(words.contains("result") && chosenIndex < ResultsAdapter.songs.size()){
                    DeviceSayFragment.resultNumber = chosenIndex;
                    appState.setAppState(State.USER_SAID_YES);
                    return;
                }
                if(words.contains("play") && chosenIndex < PlayListFragment.songs.size()){
                    MainActivity.playListIndex = chosenIndex;
                    appState.getCurrentState().postValue(State.DISPLAY_PLAYLIST);
                }
                return;
            }
            if(words.contains("ok")
                    && (state == State.DEVICE_ALREADY_SAID_RESULT
                    ||  state == State.USER_SAID_YES
                    ||  state == State.OPENING)){
                appState.setAppState(State.BEFORE_ADD_PLAYLIST);
                return;
            }
            if(words.contains("go") && state == State.OPENING){
                appState.setAppState(State.DISPLAY_PLAYLIST);
                return;
            }
            if(words.contains("delete all")){
                appState.setAppState(State.DELETE_ALL);
                return;
            }
            if(words.contains("again")){
                appState.setAppState(State.AGAIN);
                System.out.println(state);
                return;
            }
            if((words.contains("next") || words.contains("nest")) && state == State.OPENING){
                appState.setAppState(State.NEXT);
                return;
            }
            if((words.contains("return") || words.contains("before") || words.contains("back")) && state == State.OPENING){
                appState.setAppState(State.GO_BACK);
                return;
            }
            if(words.contains("delete") && state == State.OPENING){
                appState.setAppState(State.DELETE);
                return;
            }
            if(words.contains("write") || words.contains("right")){
                appState.setAppState(State.ENTER_INPUT);
                return;
            }
            if(words.contains("release")){
                appState.setAppState(State.RELEASE);
            }
        });
    }
private int textNumToDigitNum(String text){
        String [] textNum = {"one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten"};
            for (int i = 0; i < textNum.length; i++)
                if(text.contains(textNum[i])) return i;
         String numNum = text.replaceAll("[^\\d]", "");
            if(numNum.length() != 0)return Integer.parseInt(numNum) - 1;
        return 0;
}
}
