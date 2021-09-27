package com.example.squirrelwarehouse.models

import java.util.*

class ChatMessage(val id: String, val text: String, val fromId: String, val toId: String, val time: String){
    constructor()   :   this("","","","", Date(0).toString())
}