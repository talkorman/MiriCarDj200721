package com.miri.cardj.models;
/**************************************************************************************
 States of the app on different actions for more accurate communication control between the fragments
 **************************************************************************************/
public enum State {
    GREETING,               //will be active 5 sec after INTRO and after the speac will change to REJECT
    ASKING_FOR_SEARCH,      //after request for search, after the speech will change to SEARCH
    SEARCH,
    SHOW_RESULTS,
    NO_RESULTS,
    INTRO,                  //the app begin with this state
    SONG_STARTED,
    USER_SAID_YES,
    USER_SAID_NO,
    USER_DIDNT_CHOSE,
    BEFORE_ADD_PLAYLIST,
    ADDTO_PLAYLIST,
    DISPLAY_PLAYLIST,
    START,                   //after click mic, if EMPTY_DISPLAY or GREETING
    REJECT,                  //when this is chosen the player will change to EMPTY_DISPLAY after 5 sec
    REPEAT_RESULTS,
    DELETE_ALL,             //deleting the whole play list
    DELETE,                 //deleting single song from play list
    SONG_ENDED,
    DEVICE_ALREADY_SAID_RESULT, //when active, mic is on
    OPENING,                //when active, mic is on, active 5 sec after start
    USER_SPEAK,
    EMPTY_DISPLAY,           //the state after 5 sec from beginning will put the tutorial
    AGAIN,                   //repeating the search request
    NEXT,                   //next song on playing the list
    GO_BACK,                 //previous song on playing the list
    ENTER_INPUT,            //enter words on input label
    SEARCH_INPUT,            //search for words from input label
    RELEASE,                 //releasing the player from listening to microphone
    EMPTY_LIST              //after calling for playing an empty list
}
