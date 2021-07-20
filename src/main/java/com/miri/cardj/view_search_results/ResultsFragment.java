package com.miri.cardj.view_search_results;
/**************************************************************************************
The search results fragment
 **************************************************************************************/
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.miri.cardj.R;
import com.miri.cardj.models.JsonSongDummy;
import com.miri.cardj.models.SongData;
import com.miri.cardj.models.SongsDataResults;
import com.miri.cardj.models.State;
import com.miri.cardj.models.YoutubeConfig;
import com.miri.cardj.models.audio_view_models.AppState;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ResultsFragment extends Fragment {
    private RecyclerView rvSongs;
    private  int position = 100;
    private ResultsAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private static final ArrayList<SongData> resultsToShow = new ArrayList<>();

   public static ResultsFragment newInstance(){
       return new ResultsFragment();
   }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_results_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppState appState = new ViewModelProvider(requireActivity()).get(AppState.class);
        appState.getSearchWords().observe(getViewLifecycleOwner(), (search)->{
            SongsDataResults sdr = new SongsDataResults(search);
            sdr.readSearch(appState.getSearchResults());
        });
        appState.getSearchResults().observe(getViewLifecycleOwner(), (results)->{
                if(results.size() == 0){
                    appState.setAppState(State.NO_RESULTS);
                    return;
                }
                if(results.get(0).getVideoId().equals("no.1")){
                    appState.setAppState(State.NO_RESULTS);
                    return;
                }
                appState.setAppState(State.SHOW_RESULTS);
            rvSongs = view.findViewById(R.id.view_list);
            linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            rvSongs.setLayoutManager(linearLayoutManager);
            try {
                adapter = new ResultsAdapter(results, getContext());
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            rvSongs.setAdapter(adapter);
            LinearSnapHelper snapHelper = new LinearSnapHelper();
            try {
                snapHelper.attachToRecyclerView(rvSongs);
            }catch (IllegalStateException e){
                return;
            }
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    if(linearLayoutManager.findLastCompletelyVisibleItemPosition() < (adapter.getItemCount() - 1)){
                        try {
                            linearLayoutManager.smoothScrollToPosition(rvSongs, new RecyclerView.State(),
                                    position);
                        }catch(Exception e){}
                    }
                    else{
                        linearLayoutManager.smoothScrollToPosition(rvSongs, new RecyclerView.State(),
                                0);
                    }
                    if(position <= 0)position = 100;
                    position--;
                }
            }, 1100, 1100);

        });

    }
}