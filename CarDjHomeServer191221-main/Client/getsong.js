const http = require('http');
const request = require('request');
const puppeteer = require('puppeteer');
const port = process.env.PORT || 3000;

let currentSearchWord;

const server = http.createServer((req, res) => {
const searchWord = req.url.split('/')[1];
    console.log(searchWord);
  res.statusCode = 200; 
const baseUrl = 'https://duckduckgo.com/?q=';
const extraUrl = '&t=h_&iax=videos&ia=videos';
const urlSearch = baseUrl + searchWord + extraUrl;
const reqTime = setInterval(()=>{
    if(searchWord != currentSearchWord)
    currentSearchWord = "";
}, 10000);
if(
searchWord != currentSearchWord
&& searchWord != "gaocc" 
&& searchWord != "favicon.ico" 
&& searchWord != "guu4" 
&& searchWord != "1" 
&& searchWord != "?id=2"
&& searchWord.length > 2)
getSongs()
.catch(e => console.log(e));

async function getSongs(){
    currentSearchWord = searchWord; //to prevent multiple requests handling
    let browser;
    let page;
    let mainSongTag;
try{
browser = await puppeteer.launch({headless: false});
}catch(e){
    console.log(e);
}
try{
page = await browser.newPage();
}catch(e){
   console.log(e);
}
try{
await page.goto(urlSearch);
}catch(e){
    console.log(e);
}
try{
mainSongTag = await page.evaluate(() => {
let arr = [];
const tag = document.querySelectorAll(".tile");
for(let i = 0; i < tag.length; i++){
    try{
    const photoUrl = "https:" + tag[i].querySelector("img").getAttribute("src");
    const video = tag[i].querySelector("a").getAttribute("href");
    const title = tag[i].querySelector("a").innerText;
    const description = "";
    if(video.includes("www.youtube.com")){
    const videoId = video.split("=")[1];
    if( !title.includes("cover")
    && !title.includes("Cover")
    && !title.includes("concert")
    && !title.includes("Concert")
    && !title.includes("live at")
    && !title.includes("Live at")    
        ) 
    arr.push({videoId: videoId, title: title, description: description, photoUrl: photoUrl});
    }
    }catch{
        console.log('problem to fetch videos data');
    }
}

return arr;
})
}catch(e){
    console.log(e);
}
res.writeHead(200,{'Content-Type': 'application/json'});
res.write(JSON.stringify({items: mainSongTag}));
res.end();
console.log(mainSongTag);
try{
await browser.close();
}catch(e){
    res.write("<h1>e</h1>");
}
};


});

server.listen(port, () => {
  console.log(`Server running at PORT:${port}`);
});
