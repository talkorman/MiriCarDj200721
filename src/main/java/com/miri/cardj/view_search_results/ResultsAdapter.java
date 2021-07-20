package com.miri.cardj.view_search_results;
/**************************************************************************************
The results recycler adapter
 **************************************************************************************/
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.miri.cardj.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import com.miri.cardj.models.SongData;

public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.ViewHolder>{
    public static ArrayList<SongData> songs;
    Context context;

    public ResultsAdapter(ArrayList<SongData> songs, Context context) throws IOException, JSONException {
        this.songs = songs;
        this.context = context;
    }

    @NonNull
    @Override
    public ResultsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View songView = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_result_item, parent, false);
        return new ViewHolder(songView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int pos = position % songs.size();
        SongData songData = songs.get(pos);
        Picasso.get().load(songData.getPhotoUrl()).into(holder.img);
        holder.songName.setText(songData.getTitle());
        holder.songNum.setText(String.valueOf(pos + 1));
        setFadeAnimation(holder.songNum);
    }
    private void setFadeAnimation(View view) {
        AlphaAnimation anim0 = new AlphaAnimation(0.0f, 1.0f);
        anim0.setDuration(2000);
        view.startAnimation(anim0);
        AlphaAnimation anim1 = new AlphaAnimation(1.0f, 0.0f);
        anim1.setDuration(2000);
        view.startAnimation(anim1);
    }
    @Override
    public int getItemCount() {
        return 10000;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
      public ImageView img;
      public TextView songName;
      public TextView songNum;

        public ViewHolder(@NonNull View songView) {
            super(songView);
            this.img = songView.findViewById(R.id.ivImage);
            this.songName = songView.findViewById(R.id.tvName);
            this.songNum =songView.findViewById(R.id.songNum);
        }
    }
}

