const app = require('express')();
const http = require('http').createServer(app);
const request = require('request');
const puppeteer = require('puppeteer');
const bodyParser = require('body-parser');
const io = require('socket.io')(http);

app.use(bodyParser());
let clientResponseRef;
app.get('/*', (req, res) =>{
const pathName = url.parse(req.url).pathName;
const obj = {
    pathname: pathName,
    method: 'GET',
    params: req.query
};
io.emit('page-request', obj);
clientResponseRef = res;
});

app.post('/*', (req, res) => {
    const pathName = url.parse(req.url).pathName;
    const obj = {
        pathname: pathName,
        method: 'POST',
        params: req.body
    }
    io.emit('page-request', obj);
    clientResponseRef = res;

});

io.on('connection', (socket => {
    console.log('a node connected');
    socket.on('page-response', (response) => {
        clientResponseRef.send(response);
    })
}))
const port = process.env.YOUR_PORT || process.env.PORT || 3000;
http.listen(port, ()=>{
    console.log('listening at: ' + port);
})
// const server = http.createServer((req, res) => {
//     const searchWord = req.url.split('/')[1];
//     console.log(searchWord);
//   res.statusCode = 200; 
// const baseUrl = 'https://duckduckgo.com/?q=';
// const extraUrl = '&t=h_&iax=videos&ia=videos';
// const urlSearch = baseUrl + searchWord + extraUrl;

// try{
// (async () => {
//     let browser;
//     let page;
//     let mainSongTag;
// try{
// browser = await puppeteer.launch({headless: false});
// }catch(e){
//     res.write("<h1>e</h1>");
// }
// try{
// page = await browser.newPage();
// }catch(e){
//     res.write("<h1>e</h1>");
// }
// try{
// await page.goto(urlSearch);
// }catch(e){
//     res.write("<h1>e</h1>");
// }
// try{
// mainSongTag = await page.evaluate(() => {
// let arr = [];
// const tag = document.querySelectorAll(".tile");
// for(let i = 0; i < tag.length; i++){
//     try{
//     const photoUrl = "https:" + tag[i].querySelector("img").getAttribute("src");
//     const video = tag[i].querySelector("a").getAttribute("href");
//     const title = tag[i].querySelector("a").innerText;
//     const description = "";
//     if(video.includes("www.youtube.com")){
//     const videoId = video.split("=")[1];
//     arr.push({videoId: videoId, title: title, description: description, photoUrl: photoUrl});
//     }
//     }catch{
//         console.log('problem to fetch videos data');
//     }
// }

// return arr;
// })
// }catch(e){
//     res.write("<h1>e</h1>");
// }
// res.writeHead(200,{'Content-Type': 'application/json'});
// res.write(JSON.stringify({items: mainSongTag}));
// res.end();
// console.log(mainSongTag);
// try{
// await browser.close();
// }catch(e){
//     res.write("<h1>e</h1>");
// }
// })();
// }catch(e){
//     res.write("<h1>e</h1>");
// }

// });

// server.listen(port, () => {
//   console.log(`Server running at PORT:${port}`);
// });
