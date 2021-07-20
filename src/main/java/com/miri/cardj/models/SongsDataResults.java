package com.miri.cardj.models;
/**************************************************************************************
 Act as a View model for the search requests
 **************************************************************************************/
import androidx.lifecycle.MutableLiveData;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SongsDataResults {
    public ArrayList<SongData> songs;
    private final StringBuilder apiAddress;
    public int timing;
    private boolean responded;
    private final ArrayList<SongData> noResults;
    private final String usedApi = "CarDj";

    public SongsDataResults(String s){
        if(s.equals(" ") || s.equals("")) s = "one+day+ill+fly+away";
        apiAddress = new StringBuilder();
        String sSearch = s.replaceAll(" ", "+");
        this.apiAddress.append(YoutubeConfig.getCardjApi()).append(sSearch);
        noResults = new ArrayList<>();
        noResults.add(JsonSongDummy.getDummyResult1());
        timing = 30;
    }

    public void readSearch(MutableLiveData<ArrayList<SongData>>callback){
        System.out.println(apiAddress.toString());
        OkHttpClient httpClient = new OkHttpClient().newBuilder()
                .connectTimeout(timing, TimeUnit.SECONDS)
                .readTimeout(timing, TimeUnit.SECONDS)
                .writeTimeout(timing, TimeUnit.SECONDS)
                .retryOnConnectionFailure(false)
                .cache(null)
                .build();

        Request request = new Request.Builder().url(apiAddress.toString()).build();
        Call call = httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                //if(responded)return;
                //responded = true;
                System.out.println("response");
                ArrayList<SongData> songs = new ArrayList<>();
                String json = response.body().string();
                try{
                    JSONObject rootObject = new JSONObject(json);
                    JSONArray resultsJson = rootObject.getJSONArray("items");
                    if(usedApi != "Youtube"){
                    for (int i = 0; i < resultsJson.length(); i++) {
                        JSONObject songJsonObject = resultsJson.getJSONObject(i);
                        String videoId = songJsonObject.getString("videoId"),
                                title = songJsonObject.getString("title"),
                                description = songJsonObject.getString("description"),
                                photoUrl = songJsonObject.getString("photoUrl");
                        songs.add(new SongData(videoId, title, description, photoUrl));
                    }
                    }else {
                        for (int i = 0; i < resultsJson.length(); i++) {
                            JSONObject songJsonObject = resultsJson.getJSONObject(i);
                            String videoId = songJsonObject.getJSONObject("id").getString("videoId"),
                                    title = songJsonObject.getJSONObject("snippet").getString("title"),
                                    description = songJsonObject.getJSONObject("snippet").getString("description"),
                                    photoUrl = songJsonObject.getJSONObject("snippet").getJSONObject("thumbnails").getJSONObject("medium").getString("url");
                            songs.add(new SongData(videoId, title, description, photoUrl));
                        }
                    }
                        if(songs.size() != 0)
                        callback.postValue(songs);


            } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
    });
}
}
