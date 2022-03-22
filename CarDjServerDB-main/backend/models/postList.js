const mongoose = require('mongoose');

postListSchema = mongoose.Schema({
    label: {type: String, require: true},
    playList: {type: mongoose.Schema.Types.Array},
    creator: {type: mongoose.Schema.Types.ObjectId, ref: "User", required: true}
})

module.exports = mongoose.model("PostList", postListSchema);