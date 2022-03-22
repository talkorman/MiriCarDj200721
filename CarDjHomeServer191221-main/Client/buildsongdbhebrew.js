const http = require('http');
const puppeteer = require('puppeteer');
const mongoose = require("mongoose");
const express = require('express');
const Post = require("./models/song");


const port = process.env.PORT || 8080;

const server = http.createServer((req, res) => {
    
    const searchWord = req.url.split('/')[1];
    console.log(searchWord);
  res.statusCode = 200; 
const allTimesSongsUrl = [
    'https://he.wikipedia.org/wiki/קטגוריה:להקות_רוק_ישראליות',
    'https://he.wikipedia.org/wiki/קטגוריה:זמרי_רוק_ישראלים',
    'https://he.wikipedia.org/wiki/קטגוריה:זמרות_רוק_ישראליות',
    'https://he.wikipedia.org/wiki/קטגוריה:זמרי_פופ_ישראלים',
    'https://he.wikipedia.org/w/index.php?title=קטגוריה:זמרי_פופ_ישראלים&pagefrom=מלסה%2C+אביאור%0Aאביאור+מלסה#mw-pages',
    'https://he.wikipedia.org/wiki/קטגוריה:ראפרים_ישראלים',
    'https://he.wikipedia.org/wiki/קטגוריה:זמרי_מוזיקה_מזרחית_ישראלים',
    'https://he.wikipedia.org/wiki/קטגוריה:זמרים_חסידיים_ישראלים',
];
// const baseUrl = 'https://duckduckgo.com/?q=';
// const extraUrl = '&t=h_&iax=videos&ia=videos';
const baseUrl = "https://video.onesearch.com/yhs/search";

let artists = [];
let songs = [];
let pageNum = 3;
let artistIndex = 3;

mongoose.connect("mongodb+srv://taltalkorman:baMHt5bLUiOBJOpW@cluster0.o4459.mongodb.net/Cluster0?retryWrites=true", { useNewUrlParser: true,  useUnifiedTopology: true})
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
        let tags = document.querySelectorAll("#mw-pages > div > div > div > ul");
        tags.forEach(e => {
            bands = e.querySelectorAll("li > a");
            bands.forEach(e => {
                let name = e.getAttribute("title");
                let name0 = name.replace("(להקה)", "");
                let name1 = name0.replace("(זמר)", "");
                let name2 = name1.replace("(זמרת)", "");
                let name3 = name2.replace("(ראפר)", "");
                if(!arr.includes(name3) && name3 != "DXM" && name3 != "Staraya derevnya")
                arr.push(name3);
            })
        })
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
    await page.type("#yschsp", artist);
    await page.click("#sbx_y");
    try{
    await page.waitForSelector("li > a.ng");
    }catch(e){
        console.log(artist + " not found");
        return [];
    }
    const buttonSelector = "div > div > section > button";
    await delay(3000);
    try{
    await page.evaluate((buttonSelector) =>  document.querySelector(buttonSelector).click(), buttonSelector);
    }catch(e){console.log("no button")};
    await delay(3000);
    try{
        await page.evaluate((buttonSelector) =>  document.querySelector(buttonSelector).click(), buttonSelector);
        }catch(e){console.log("no button")};
    await delay(3000);
    try{
        await page.evaluate((buttonSelector) =>  document.querySelector(buttonSelector).click(), buttonSelector);
        }catch(e){console.log("no button")};
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
if(songs == []) return;
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

function delay(time) {
    return new Promise(function(resolve) { 
        setTimeout(resolve, time)
    });
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