package com.miri.cardj.models.play_lists;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.miri.cardj.models.SongData;

import java.util.List;

@Dao
public interface SongDao {

    @Query("SELECT * FROM SongData")
    LiveData<List<SongData>> getSongs();

    @Query("DELETE FROM SongData")
    void deleteAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void add(SongData song);

    @Delete
    void remove(SongData song);

    @Update
    void update(SongData song);


}
