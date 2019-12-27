package com.veno_studio.venex.model

data class ChattingModel (

    var users: MutableMap<String, Boolean> = HashMap(),
    var comments: MutableMap<String, Comment> = HashMap()

){
data class Comment(
    var uid: String? = null,
    var message: String? = null
)}