const express = require('express');
const router = express.Router();
const Post = require('../models/postSong');
const bodyParser = require('body-parser');
const https = require('https');



let currentSearchWord = '';
let searchWord = '';

const reqTime = setInterval(()=>{
    currentSearchWord = "";
    }, 10000);

router.get("/:songDetail", (req, res, next) => {   
    let songsList = new Set();
    searchWord = req.params.songDetail;    
    
    if(
        searchWord != currentSearchWord
        && searchWord != "gaocc" 
        && searchWord != "favicon.ico" 
        && searchWord != "guu4" 
        && searchWord != "1" 
        && searchWord.length > 2){
    currentSearchWord = searchWord;
    const reg = /\+/g;
    const songData = searchWord.replace(reg, ' ');
    
    //console.log(songData);
    Post.find({title: {$regex : songData, $options: 'i'}}, {_id: 0, __v: 0})
    .then(results => {
       for(let i = 0; i < results.length; i++){
           songsList.add(results[i]);
       }
       const songs1 = Array.from(songsList);
       const songs2 = removeDuplicates(songs1, 'videoId');
       console.log('songs from DB: ' + songs2);
       afterDB(songs2, songData);
    })
   

       //console.log(songs);
async function afterDB(songs, searchWord){
    let extraSongs = [];
    if(songs.length < 5){
        console.log('not enough results from DB');
        extraSongs = await getFromHome(searchWord);
        return;
    }
        const totalSongs = await songs.concat(extraSongs);
        await console.log('total songs: ' + totalSongs.length);
        await responseSongs(totalSongs);
}

function getFromHome(searchWord){
    console.log('getting from home');
    https.get('https://cardjserver.herokuapp.com/' + searchWord, res => {
        console.log('requesting from home');
        let data = '';
        res.on('data', chunk => {
            data += chunk;
        });
        res.on('end', async () => {
        const results = await JSON.parse(data).items;
        await console.log('results from home: ' + results);
        await postExtraOnDB(results);
        await responseSongs(results);
        return results;
    });
    res.on('error', err => {
        console.log('fetch from home error');
        return [];
    });
    })
    
async function postExtraOnDB(songs){
    let numOfPosts = 0;
    for(let i = 0; i < songs.length; i++){
        if(i > 10) break;
        let song = songs[i];
        const post = new Post({
            videoId: song.videoId,
            title: song.title,
            description: ' ',
            photoUrl: song.photoUrl
    });
    await post.save().then(result=>{   
        numOfPosts++; 
    })
    }
    console.log(songs[0]);
     console.log("saved " + numOfPosts + " songs");
}
    
}
 function responseSongs(songs){
       setTimeout(() => {
            res.writeHead(200,{'Content-Type': 'application/json'});
res.write(JSON.stringify({items: songs}));
res.end();
       }, 2000)
    }
    
}
})
    function removeDuplicates(myArr, prop) {
        return myArr.filter((obj, pos, arr) => {
            return arr.map(mapObj => mapObj[prop]).indexOf(obj[prop]) == pos;
        })
    }

    module.exports = router;