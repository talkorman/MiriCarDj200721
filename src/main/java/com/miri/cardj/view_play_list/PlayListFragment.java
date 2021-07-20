package com.miri.cardj.view_play_list;
/**************************************************************************************
 The play list fragment
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
import com.miri.cardj.models.SongData;
import com.miri.cardj.models.State;
import com.miri.cardj.models.audio_view_models.AppState;
import com.miri.cardj.models.play_lists.SongsDataSource;
import com.miri.cardj.view_search_results.ResultsAdapter;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class PlayListFragment extends Fragment {

    private RecyclerView rvPlayList;
    private PlayListAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private AppState appState;
    public static List<SongData> songs;
    private  int position = 100;

    public static PlayListFragment newInstance(){
        PlayListFragment playListFragment = new PlayListFragment();
        return playListFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        appState = new ViewModelProvider(requireActivity()).get(AppState.class);
        View root =  inflater.inflate(R.layout.fragment_play_list, container, false);
        rvPlayList = root.findViewById(R.id.rvPlayList);
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvPlayList.setLayoutManager(linearLayoutManager);
        SongsDataSource ds = new SongsDataSource(getContext());
        ds.getSongs().observe(getViewLifecycleOwner(), s->{
            songs = s;
            adapter = new PlayListAdapter(s, getContext());
            rvPlayList.setAdapter(adapter);
            LinearSnapHelper snapHelper = new LinearSnapHelper();
            try {
                snapHelper.attachToRecyclerView(rvPlayList);
            }catch (IllegalStateException e){
                return;
            }
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    if(linearLayoutManager.findLastCompletelyVisibleItemPosition() < (adapter.getItemCount() - 1)){
                        try {
                            linearLayoutManager.smoothScrollToPosition(rvPlayList, new RecyclerView.State(),
                                    position);
                        }catch(Exception e){}
                     }
                    else{
                        linearLayoutManager.smoothScrollToPosition(rvPlayList, new RecyclerView.State(),
                                0);
                    }
                    if(position <= 0)position = 100;
                    position--;
                }
            }, 1000, 1000);
        });
        appState.getSongToList().observe(getViewLifecycleOwner(), song->{
            ds.add(song);
        });
        appState.getCurrentState().observe(getViewLifecycleOwner(), state->{
            if(state == State.DISPLAY_PLAYLIST){
            appState.getSongsPlayList().postValue(songs);
            }
            if(state == State.DELETE_ALL){
                ds.deleteAll();
            }
            if(state == State.DELETE){
                String videoId = appState.getVideoId().getValue();
                songs.forEach(songData -> {
                    if(songData.getVideoId() == videoId){
                        ds.remove(songData);
                    }
                });
            }
            if(state == State.OPENING){
                rvPlayList.animate();
            }
        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        appState = new ViewModelProvider(requireActivity()).get(AppState.class);
    }
}
