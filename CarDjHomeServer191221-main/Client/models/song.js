
const mongoose = require('mongoose');


const songSchema = mongoose.Schema({
videoId: {type: String, required: true},
title:{type: String, require: true},
description: {type: String, required: true},
photoUrl: {type: String, required: true} //to reffer the post to the specific user who created it
});


module.exports = mongoose.model("Post", songSchema);
