package com.example.squirrelwarehouse.models

import java.util.*

class ChatImage(val id: String, val imageuri: String, val fromId: String, val toId: String, val time: String){
    constructor()   :   this("","","","", Date(0).toString())
}