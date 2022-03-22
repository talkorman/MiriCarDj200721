const express = require('express');
const bodyParser = require('body-parser');
const mongoose = require("mongoose");
const postSong = require('./routes/postSong');
const app = express();

mongoose.connect("mongodb+srv://taltalkorman:baMHt5bLUiOBJOpW@cluster0.o4459.mongodb.net/Cluster0?retryWrites=true", { useNewUrlParser: true,  useUnifiedTopology: true})
.then(() => {
    console.log('Connected to database!')
})
.catch((e) => {
    console.log("Connection failed! " + e);
});
app.use(bodyParser.json()); //for parsing json data
app.use(bodyParser.urlencoded({extended: true})); //for parsing url data

//to prevent CORS which is an error of host address missmatch
app.use((req, res, next) => {
    res.setHeader("Access-Control-Allow-Origin", "*");
    res.setHeader("Access-Control-Allow-Headers", "Origin, X-Request-With, Content-Type, Accept, Authorization");
    res.setHeader("Access-Control-Allow-Methods", "GET, POST, PATCH, PUT, DELETE, OPTIONS");
    next(); // to be able to continue to the next one
});

app.get('/', (req, res) =>{
    console.log("Hello");
})

app.use("/api/requestsong", postSong);
module.exports = app;       //connecting the express with the nodeJS