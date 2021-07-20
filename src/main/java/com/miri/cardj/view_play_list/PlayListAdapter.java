package com.miri.cardj.view_play_list;
/**************************************************************************************
Play list recycle view adapter
 **************************************************************************************/
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import com.miri.cardj.R;
import com.miri.cardj.models.SongData;
import com.miri.cardj.models.audio_view_models.AppState;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.logging.Handler;

public class PlayListAdapter extends RecyclerView.Adapter<PlayListAdapter.ViewHolder> {
    public List<SongData> songs;
    Context context;


    public PlayListAdapter(List<SongData> songs, Context context) {
        this.context = context;
        this.songs = songs;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View songView = LayoutInflater.from(parent.getContext()).inflate(R.layout.play_list_item, parent, false);
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

    static class ViewHolder extends RecyclerView.ViewHolder{
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
