let socketServerUrl = "https://cardjserver.herokuapp.com";
let hostToLive = "http://localhost:3000";

let socket = require('socket.io-client')(socketServerUrl);
const superAgent = require('superagent');

socket.on('connect', ()=>{
    console.log('connected at:' + Date.now());
});


socket.on('disconnect', ()=>{
    console.log('disconnected at:' + Date.now());
});

socket.on('page-request', function (data){
    let path = data.pathname;
    let method = data.method;
    let params = data.params;
    let localHostUrl = hostToLive + path;
    if(method == "get")executeGet(localHostUrl, params)
    else if(method == "post")executePost(localHostUrl, params);
});

function executeGet(url, params){
    superAgent.get(url)
    .query(params)
    .end((err, res) =>{
        if(err){return console.log(err);};
        socket.emit('page-response', res.text);

    })

}

function executePost(url, params){
    superAgent.post(url)
    .query(params)
    .end((err, res) =>{
        if(err){return console.log(err);}
        socket.emit('page-response', response.text);
        
    })

}