package com.miri.cardj.models;
/**************************************************************************************
The model of a song for search results, play list and for SQL Room
 **************************************************************************************/
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class SongData {

   @ColumnInfo(name = "song_id")
    private int songId;
    @PrimaryKey
    @NonNull
    private String videoId;
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "description")
    private String description;
    @ColumnInfo(name = "photoUrl")
    private String photoUrl;

    public SongData(String videoId, String title, String description, String photoUrl) {
        this.videoId = videoId;
        this.title = title;
        this.description = description;
        this.photoUrl = photoUrl;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public int getSongId() {
        return songId;
    }

    public String getVideoId() {
        return videoId;
    }
    public String getDescription(){ return description; }
    public String getTitle() {
        return title;
    }
    public String getPhotoUrl(){
        return photoUrl;
    }

    @Override
    public String toString() {
        return "SongData{" +
                "videoId='" + videoId + '\'' +
                ", title='" + title + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                '}';
    }
}
