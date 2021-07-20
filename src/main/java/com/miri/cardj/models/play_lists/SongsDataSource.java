package com.miri.cardj.models.play_lists;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.miri.cardj.models.SongData;
import java.util.List;

public class SongsDataSource {
    private SongDataBase db;

    public SongsDataSource(Context context){
        db = Room.databaseBuilder(context, SongDataBase.class, "SongDataBase").build();
    }
    public void add(SongData song){
        new Thread(()->{
            db.getDao().add(song);
        }).start();
    }

    public LiveData<List<SongData>> getSongs(){
            return db.getDao().getSongs();
    }

    public void remove(SongData song){
        new Thread(()->{
            db.getDao().remove(song);
        }).start();
    }

    public void deleteAll(){
        new Thread(()->{
            db.clearAllTables();
        }).start();
    }
    public SongDataBase getDb() {
        return db;
    }
}
