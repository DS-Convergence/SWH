package com.example.squirrelwarehouse

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.squirrelwarehouse.models.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log_picture_more.*


class ChatLogPictureMoreActivity : AppCompatActivity() {

    val adapter = GroupAdapter<ViewHolder>()//새로운 어뎁터
    var toUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log_picture_more)
        var intent = intent
        var chatimageuri = intent.getStringExtra("chatimage").toString()
        Log.d("newcheck" , "ChatLogPictureMoreActivity 안으러 들어옴")
        var firestore = FirebaseFirestore.getInstance()

        var storage : FirebaseStorage? = FirebaseStorage.getInstance()
        var photoUri: Uri? = null
        photoUri = Uri.parse(chatimageuri)
        /* val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, photoUri)
         if(bitmap != null){
             viewHolder.itemView.imageImageView_from_row.setImageBitmap(bitmap)
         }else{
         }*/

        //Picasso.get().load(photoUri).into(viewHolder.itemView.imageImageView_from_row)
        var storageReff = storage?.reference?.child("chatting")?.child(chatimageuri)
        //var storageRef = storage?.reference?.child("chatting")?.child(imgFileName)
        Log.d("newcheck", "storageReff" + storageReff)
        storageReff?.downloadUrl?.addOnSuccessListener { uri ->
            Log.d("newcheck" , "storageReff 안으러 들어옴")
            Log.d("newcheck" , "chatImage!!.Imageuri " + chatimageuri)
            Log.d("newcheck" , "uri " +uri)
            Picasso.get().load(uri).into(image_more_bigger_than_before)
        }

    }

}