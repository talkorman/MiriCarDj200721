package com.miri.cardj.models.audio_view_models;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;
import com.miri.cardj.R;
import com.miri.cardj.models.State;

import androidx.activity.result.ActivityResultCaller;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.Locale;


public class UserSayFragment extends Fragment implements ActivityResultCaller {
    private ImageButton btnUserSay;
    private SpeechRecognizer recognizer;
    private Intent intent;
    private AppState appState;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_user_audio, container, false);
        btnUserSay = root.findViewById(R.id.btnUserSay);
        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkPermission();
        appState = new ViewModelProvider(requireActivity()).get(AppState.class);
        appState.getCurrentState().observe(getViewLifecycleOwner(), (state)-> {
                    if (state == State.SEARCH
                            || state == State.SHOW_RESULTS
                            || state == State.REPEAT_RESULTS
                            || state == State.USER_DIDNT_CHOSE
                            || state == State.NEXT
                            || state == State.GO_BACK
                            || state == State.ADDTO_PLAYLIST
                            || state == State.SEARCH_INPUT
                            || state == State.RELEASE){
                        recognizer.stopListening();
                        System.out.println("stop listen");
                        return;
                    }
                    if (state == State.DEVICE_ALREADY_SAID_RESULT || state == State.OPENING) {
                        recognizer.startListening(intent);
                    }
                }
        );
        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
        recognizer = SpeechRecognizer.createSpeechRecognizer(getContext());
        recognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onResults(Bundle results) {
                recognizer.stopListening();
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null) {
                    String string = matches.get(0);
                    appState.getUserSpokenText().postValue(string);
                }
            }

            @Override
            public void onReadyForSpeech(Bundle params) {
            }
            @Override
            public void onBeginningOfSpeech() {
            }

            @Override
            public void onRmsChanged(float radB) {
            }

            @Override
            public void onBufferReceived(byte[] buffer) {
            }

            @Override
            public void onEndOfSpeech() {
            }

            @Override
            public void onError(int error) {
                if (error == 1 || error == 5) {
                    if(appState.getCurrentState().getValue() == State.OPENING){
                        recognizer.startListening(intent);
                    }
                }
            }

            @Override
            public void onPartialResults(Bundle partialResults) {
            }

            @Override
            public void onEvent(int eventType, Bundle params) {
            }
        });
        btnUserSay.setOnClickListener((v) -> {
            recognizer.stopListening();


            State state = appState.getCurrentState().getValue();
            if(state == State.ENTER_INPUT){
                appState.getCurrentState().postValue(State.SEARCH_INPUT);
                return;
            }
            if(state == State.INTRO
                    || state == State.GREETING
                    || state == State.EMPTY_DISPLAY)
            {
                appState.setAppState(State.START);
                return;
            }
                appState.setAppState(State.OPENING);
                try {
                    recognizer.startListening(intent);
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Mic Issue", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void checkPermission() {
        if(!(ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)){
                new AlertDialog.Builder(getContext())
                        .setTitle("The following permissions and activations are required")
                        .setMessage("Please go to setting and check the following:\n" +
                                "1. Audio recording is permitted\n" +
                                "2. Speach to Text service is enabled.\n" +
                                "3. Text to Speach service is enabled")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                        Uri.parse("package:" + getActivity().getPackageName()));
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
        }

    }
}
