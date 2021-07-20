package com.miri.cardj.models;
/**************************************************************************************
 This Dummy song object is used for a situation that Youtube API not responding
 The screen will show results with photos of a cow.
 **************************************************************************************/
public class JsonSongDummy {

public static SongData getDummyResult1(){

    return new SongData(
            "no.1",
            "",
            "",
            "");
}

public static SongData getDummyResult2(){
    return new SongData(
            "no.2",
            "",
            "",
            "");
}

}
