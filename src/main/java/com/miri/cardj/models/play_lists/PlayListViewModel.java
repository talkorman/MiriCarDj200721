package com.miri.cardj.models.play_lists;
import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.miri.cardj.models.SongData;

import java.util.ArrayList;
import java.util.List;

public class PlayListViewModel extends AndroidViewModel {
    private SongsDataSource ds;

    public PlayListViewModel(@NonNull Application application) {
        super(application);
        MutableLiveData<ArrayList<SongData>> songs = new MutableLiveData<>();
        };

        public LiveData<List<SongData>> getSongs(){
         ds = new SongsDataSource(getApplication());
        return ds.getSongs();
        }

        public void add(SongData song){
        ds.add(song);
        }

        public void delete(SongData song){ds.remove(song);}
    }

