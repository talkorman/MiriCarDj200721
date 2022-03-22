const mongoose = require('mongoose');

const postSongSchema = new mongoose.Schema({
    videoId: {type: String, required: true},
    title: {type: String, required: true},
    description: {type: String, required: true},
    photoUrl: {type: String, required: true}
})

module.exports = mongoose.model("Post", postSongSchema);