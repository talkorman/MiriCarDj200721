//Automatic songs data fetching software for filling Songs data base.
//Each song data contains: title, small photo URL and Youtube URL.
//The songs are colloected from a search website and are of English and Hebrew songs.
//This program was operated for filling the data base for the 1st time and was uswd only once for a period of a week.

const http = require('http');
const puppeteer = require('puppeteer');
const mongoose = require("mongoose");
const express = require('express');
const Post = require("./models/song");
const router = express.Router();


const port = process.env.PORT || 8080;

const server = http.createServer((req, res) => {
    
    const searchWord = req.url.split('/')[1];
    console.log(searchWord);
  res.statusCode = 200; 
const allTimesSongsUrl = ['http://www.acclaimedmusic.net/061024/1948-09art.htm',
'http://www.acclaimedmusic.net/061024/1948-09art2.htm',
'http://www.acclaimedmusic.net/061024/1948-09art3.htm',
'http://www.acclaimedmusic.net/061024/1948-09art4.htm',
'http://www.acclaimedmusic.net/061024/1948-09art5.htm'];

// const baseUrl = 'https://duckduckgo.com/?q=';
// const extraUrl = '&t=h_&iax=videos&ia=videos';
const baseUrl = "https://video.onesearch.com/yhs/search";

let artists = [];
let songs = [];
let pageNum = 4; //accorfing to the available pages
let artistIndex = 187;

mongoose.connect("/*here comes the code for MongoDB data base*/", { useNewUrlParser: true,  useUnifiedTopology: true})
.then(() => {
    console.log('Connected to database!')
})
.catch((e) => {
    console.log("Connection failed! " + e);
});
fetchListAndProcess();

async function fetchDataFromAWeb(url){
    let browser;
    let page;
    let artists;
browser = await puppeteer.launch({headless: false});
page = await browser.newPage();
await page.goto(url);
 artists = await page.evaluate(() => {
        let arr = [];
        const tag = document.querySelectorAll("td:nth-child(2) a");
        for(let i = 0; i < tag.length; i++){
                arr[i] = tag[i].innerText;
            }
        return arr;
 })
await browser.close();
return artists;
}

async function fetchVideosFromWeb(artist){
    if(artist == "" || artist == null) return;   
        let browser;
        let page;
        let songs; 
        let photoUrl;
        let video;
        let title;
        let description;
        // const url = baseUrl 
        // + artist.replace(" ", "+")
        // + "hq" 
        // + extraUrl;
        const url = baseUrl;
    browser = await puppeteer.launch({
        headless: false,
        slowMo: 100
    });
    page = await browser.newPage();
    await page.goto(url);
    await page.waitForSelector("#yschsp");
    await page.type("#yschsp", artist + " hq vinyl rip");
    await page.click("#sbx_y");
    await page.waitForSelector("li > a.ng");
    songs = await page.evaluate(() => {
        let arr = [];
        // const tag = document.querySelectorAll(".tile");
            const tag = document.querySelectorAll("li > a.ng");
        for(let i = 0; i < tag.length; i++){
            try{
             photoUrl = tag[i].querySelector("img").getAttribute("src");          
             video = tag[i].getAttribute("data-rurl");
             title = tag[i].querySelector("h3").innerText;
             description = " ";
            }catch(e){console.log(e)}
            if(video.includes("www.youtube.com")){
            const videoId = video.split("=")[1];
            arr.push({videoId: videoId, title: title, description: description, photoUrl: photoUrl});
        }
    }
    let chose = parseInt(Math.random() * tag.length);
    let glancing = (Math.random() + 1) * 5000;
    setTimeout(async() =>{
      await page.click(tag[chose]);  
    }, glancing);
    
         return arr;
})
    
    await browser.close();
    return songs;
}

async function postSongsToDB(songs){
    for(let i = 0; i < songs.length; i++){
        let song = songs[i];
        const post = new Post({
            videoId: song.videoId,
            title: song.title,
            description: song.description,
            photoUrl: song.photoUrl
    });
    post.save().then(result=>{    
    })
    }
    console.log(songs[0]);
     console.log("saved");
}

async function fetchAndPost(artist){
songs = await fetchVideosFromWeb(artist);
await postSongsToDB(songs);
console.log("Page: " + pageNum 
        + "  Artist number: " + artistIndex 
        + "  Artist name: " + artists[artistIndex]
        + "  Results number: " + songs.length);
}

async function fetchListAndProcess(){
artists = await fetchDataFromAWeb(allTimesSongsUrl[pageNum]);
 const process = setInterval(async() => {
     let randomDelay = Math.random() * 20000;
     setTimeout(async ()=>{
         await fetchAndPost(artists[artistIndex]);
    artistIndex++;
    if(artistIndex > artists.length - 1){
        artistIndex = 0;
        if(pageNum < allTimesSongsUrl.length){
        pageNum++;
        artists = await fetchDataFromAWeb(allTimesSongsUrl[pageNum]);
        }
    }
    }, randomDelay)
    
}, 60000);
}


function sendBackSongsData(mainSongTag){
    res.writeHead(200,{'Content-Type': 'application/json'});
res.write(JSON.stringify({items: mainSongTag}));
res.end();
console.log(mainSongTag);
};
});
server.listen(port, () => {
  console.log(`Server running at PORT:${port}`);
});
