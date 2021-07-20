package com.miri.cardj.models.play_lists;
import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.miri.cardj.models.SongData;

@Database(entities = {SongData.class}, version = 1, exportSchema = false)
public abstract class SongDataBase extends RoomDatabase {
    public abstract SongDao getDao();
}
