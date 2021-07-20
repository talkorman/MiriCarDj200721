package com.miri.cardj.models;
/**************************************************************************************
The static configuration for the Youtube API.
 **************************************************************************************/
public class YoutubeConfig {
    public YoutubeConfig(){

    }
    private static final String API_KEY = "AIzaSyBqECp8c8JgCjbeOq80j3uJpHI148URZyA";
    private static final String YOUTUBE_API = "https://www.googleapis.com/youtube/v3/search?part=snippet&order=rating&type=video&videoDefinition=any&videoDuration=short&videoEmbeddable=any&key=";
    private static final String CARDJ_API = "https://cardjserver.herokuapp.com/api/requestsong/";
    public static String getYoutubeApi() {
        return YOUTUBE_API + API_KEY + "&q=HQ+Audio+HD+Official+";
    }
    public static String getCardjApi(){
        return CARDJ_API;
    }
}

