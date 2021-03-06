package com.example.squirrelwarehouse

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.squirrelwarehouse.models.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.activity_chat_log_more.*
import kotlinx.android.synthetic.main.activity_latest_message.*
import kotlinx.android.synthetic.main.activity_my_page.*
import kotlinx.android.synthetic.main.activity_new_message.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_from_row.view.chat_from_row_time
import kotlinx.android.synthetic.main.chat_from_row.view.imageview_chat_from_row
import kotlinx.android.synthetic.main.chat_image_from_row.view.*
import kotlinx.android.synthetic.main.chat_image_to_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.chat_to_row_time
import kotlinx.android.synthetic.main.chat_to_row.view.imageview_chat_to_row
import kotlinx.android.synthetic.main.chat_to_row.view.textView_to_row
import kotlinx.android.synthetic.main.main_page.*
import kotlinx.android.synthetic.main.product_form.*
import kotlinx.android.synthetic.main.user_row_new_message.view.*
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*


class ChatLogActivity : AppCompatActivity() {
    companion object {
        //var currentUser: User? = null
        val TAG = "chatLog"
    }

    // private lateinit var user: UserModelFS
    val adapter = GroupAdapter<ViewHolder>()//????????? ?????????
    // var toUser: User? = null
    private var toUser: UserModelFS? = null
    private var firestore : FirebaseFirestore? = null
    private var storage : FirebaseStorage? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var touserid : String //???????????????
    private lateinit var toimgUri : String //????????? ?????????
    private var uri : Uri? = null
    private lateinit var prodUserId : String  // ?????? ?????? id

    //val fromId = FirebaseAuth.getInstance().uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        back_btn_chatting.setOnClickListener {
            finish()
        }


        recyclerView_chat_log_activity.adapter = adapter //????????? object??? add??? ??? ?????? ????????? ?????? ????????? ????????? refresh??????.

        //ProductDetailActivity?????? ????????? ????????????
        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        val fromId = FirebaseAuth.getInstance().uid
        // var intent = intent
        var prod = intent.getStringExtra("ProductID")
        touserid = intent.getStringExtra("UserId").toString()


        if (prod != null) {
            firestore?.collection("Product")?.document(prod!!)?.get()?.addOnCompleteListener { // ????????? ?????? id??? ???????????? ???.
                    task ->
                if(task.isSuccessful) { // ????????? ??????????????? ????????????
                    var product = task.result.toObject(Product::class.java)

                    chat_log_textview_productname.setText(product?.productName)
                    chat_log_activity_textview_username.setText(product?.userName)
                    //userid = product?.userId.toString()
                    prodUserId = product?.userId.toString()
                }
            }
        }

        //?????? ????????? ??????
        //NewMessageActivity?????? ????????? ??? ????????? username ????????????
        //username ??????????????? ?????? user????????? ??? ??????
        //toUser = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        //chat_log_textview_username.text = toUser?.username //optional????????? ?????????????.
        //supportActionBar?.title = toUser?.username //optional????????? ?????????????.

        listenForMessages() //???????????? ?????? ??? ?????? ??????, ?????? ?????? ????????? ????????? ?????? ?????? ??????.
        //fetchCurrentUser() //?????? currentUser setting?????? ??????????????? ????????? ?????? ????????????.
        //????????? ?????? ????????? ????????????

        chat_log_camera_chat_log_more.setOnClickListener {
            //????????? ???????????????(????????? ??????)
            //??? ??? ???????????? ??? ????????? ????????? ?????????...
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = MediaStore.Images.Media.CONTENT_TYPE
            startActivityForResult(intent, 1) //PICK_IMAGE?????? ????????? ????????? ?????????????????????.
        }

        chat_log_send_button_chat_log.setOnClickListener {
            Log.d(TAG, " Attempt to send message.....")
            performSendMessage() // ????????? ?????????. ????????? firebase??? ???????????? ?????????
        }


        qr_button.setOnClickListener {
            //ChatLogMore????????? ?????? ?????? ????????????
            val intent = Intent(this, ChatLogMoreActivity::class.java)
            intent.putExtra("userId1", prodUserId) //???????????? ??????_????????? ?????? ??????_QR?????? ?????????
            Log.d("CHECK_INTENT", "userId1 " + touserid)

            // ????????????.
            if(fromId.equals(prodUserId)) { // ?????? ???????????? ?????? ???????????? null
                intent.putExtra("userId2", touserid) //????????? ??????_????????? ?????????
            }
            else { // ????????? ?????? ????????? id ?????????
                intent.putExtra("userId2", fromId) //????????? ??????_????????? ?????????
            }
            Log.d("CHECK_INTENT", "userId2 " + fromId)
            intent.putExtra("productId", prod)
            Log.d("CHECK_INTENT", "productId " + prod)
            startActivityForResult(intent, 0)
        }
    }

    //??????????????? ????????? ???????????? ???????????? ?????????
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == Activity.RESULT_OK) {
                try {
                    uri = data!!.data
                    var timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                    var imgFileName = "IMAGE_" + timeStamp + "_.jpg"
                    var storageRef = storage?.reference?.child("chatting")?.child(imgFileName)

                    storageRef?.putFile(uri!!)?.addOnSuccessListener {
                        Toast.makeText(applicationContext,"Send the photo", Toast.LENGTH_SHORT).show() //????????? ??????????????????
                    }
                    //Glide.with().load(chatImage!!.Imageuri).into(viewHolder.itemView.imageImageView_from_row)
                    //val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                    Log.d("CHECK", "????????? ?????? ?????????")
                    Log.d("CHECK", "uri : "+uri)
                    Log.d("CHECK", "uri.toString() : "+uri.toString())
                    //Glide.with(this).load(uri).into(img)

                    //start here
                    //how do we actually send a messaage to firebase

                    val fromId = FirebaseAuth.getInstance().uid //?????? ????????? ??????????????? from
                    Log.d("CHECK", "????????? ?????? : " + fromId)
                    //val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
                    //val toId = user!!.uid
                    val toId = touserid
                    Log.d("CHECK", "?????? ?????? : " + toId)
                    var prod = intent.getStringExtra("ProductID")
                    //var prod = intent.getStringExtra("prod").toString()
                    Log.d("CHECK", "prod : " + prod)
                    //firebase??? user-message??????.
                    //?????? ?????? ?????? ????????? ????????? ????????????
                    //????????? ?????? ??????( current Uer )??? ?????? ???????????? ?????????_reference
                    //?????? ????????? ?????? ???????????? upload?????? ?????????_toReference

                    //2021_07_31?????? ?????? ?????? ?????? ??????
                    var toidprod = toId + "_" +prod
                    var fromidprod = fromId + "_" + prod
                    val reference = FirebaseDatabase.getInstance().getReference("/user-message/$fromId/$toidprod").push()
                    val toReference = FirebaseDatabase.getInstance().getReference("/user-message/$toId/$fromidprod").push()

                    //????????????
                    //val reference = FirebaseDatabase.getInstance().getReference("/user-message/$fromId/$idprod").push()
                    //val toReference = FirebaseDatabase.getInstance().getReference("/user-message/$toId/$fromId").push()
                    if (fromId == null) return //????????? ID????????? ?????? return


                    val chatImage = ChatImage(prod!!, imgFileName, fromId, toId, Calendar.getInstance().time) //class?????? ?????????

                    reference.setValue(chatImage)
                        .addOnSuccessListener {
                            Log.d("CHECK", "chatImage?????? referece??? "+ reference)
                            Log.d(ChatLogListActivity.TAG, "Saved our chat message: ${reference.key}")
                            //editText_chat_log.text.clear() //????????? ?????? ?????????
                            recyclerView_chat_log_activity.scrollToPosition(adapter.itemCount - 1) //????????? ?????? ?????? ?????? ????????? ????????? ????????? ??????
                        }
                    toReference.setValue(chatImage) //???????????? ????????? ?????? ?????? ????????? ?????? ??????
                    Log.d("CHECK", "chatImage?????? toreferece??? "+ toReference)
                    //???????????????????????? ?????????
                    val latestMessageRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId/$toidprod")
                    latestMessageRef.setValue(chatImage)
                    Log.d("CHECK", "chatImage?????? latestMessageRef "+ latestMessageRef)

                    val latestMessageToRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$toId/$$fromId")
                    latestMessageToRef.setValue(chatImage)
                    Log.d("CHECK", "chatImage?????? latestMessageToRef "+ latestMessageToRef)
                    //end here


                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun listenForMessages() { //???????????? ?????? ????????? ????????????
        var myUser: UserModelFS? = null
        val fromId = FirebaseAuth.getInstance().uid //???
        Log.d("listenForMessages Test", "fromId : " +fromId)
        //val toId = toUser?.uid //?????????
        val toId = touserid

        Log.d("listenForMessages Test", "touserid : "+touserid)

        //??? ???????????? ?????? ??? ??????
        var prod = intent.getStringExtra("ProductID")


        var toidprod = toId + "_" + prod
        var fromidprod = fromId + "_" + prod

        val ref = FirebaseDatabase.getInstance().getReference("/user-message/$fromId/$toidprod")

        var auth : FirebaseAuth = FirebaseAuth.getInstance()
        var firestore : FirebaseFirestore? = FirebaseFirestore.getInstance()
        var uid : String? = auth.currentUser?.uid
        storage = FirebaseStorage.getInstance()



        firestore?.collection("Users")?.document("user_${uid}")?.get()?.addOnCompleteListener {// ????????? ?????? id??? ???????????? ???.
                task ->
            if(task.isSuccessful) { // ????????? ??????????????? ????????????
                var myuser = task.result.toObject(UserModelFS::class.java)
                myUser = myuser
                Log.d("listenForMessages Test", "myuser??? uid : "+ myuser!!.uid)
                Log.d("listenForMessages Test", "myuser : "+ myuser)

                ref.addChildEventListener(object : ChildEventListener {
                    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                        //???????????? ????????? 2021_07_19
                        //??? chatImage??? ????????????
                        val chatMessage = snapshot.getValue(ChatMessage::class.java)
                        Log.d("listenForMessages Test", "chatMessage : " + chatMessage)
                        Log.d("listenForMessages Test", "chatMessage.text : " + chatMessage!!.text)
                        val chatImage = snapshot.getValue(ChatImage::class.java)
                        Log.d("listenForMessages Test", "chatImage : " + chatImage!!.id + " " +chatImage.imageuri + " " +chatImage!!.fromId + " " +chatImage!!.toId + " " +chatImage!!.time )
                        Log.d("listenForMessages Test", "chatImage.Imageuri : " + chatImage.imageuri)
                        Log.d("listenForMessages Test", "chatImage.Imageuri : " + chatImage)
                        if (chatMessage != null) {
                            if (chatMessage!!.text != "") {
                                Log.d("listenForMessages Test", "chatMessage is not null")
                                //?????? ???????????? null??? ????????????
                                Log.d("listenForMessages Test", " chatMessage.text" + chatMessage.text)//?????? ?????? ?????????
                                if (chatMessage.fromId == FirebaseAuth.getInstance().uid) { //chatMessage??? ?????? ????????? ??????
                                    Log.d("listenForMessages Test", "?????? ??????????????? chatMessage.fromId " +chatMessage.fromId)
                                    Log.d("listenForMessages Test", "FirebaseAuth.getInstance().uid " + FirebaseAuth.getInstance().uid)
                                    //val currentUser = currentUser ?: return
                                    adapter.add(ChatFromItem(chatMessage, myuser))
                                    Log.d("listenForMessages Test", "currentUser" + myuser)
                                    //?????? ???????????? user??? ????????? : FirebaseAuth.getInstance().uid

                                    //adapter.add(ChatFromItem(chatMessage, currentUser))
                                    Log.d("listenForMessages Test", "adapter.add(ChatFromItem(chatMessage, currentUser)) ??????" )
                                } else {//chatMessage??? ?????? ????????? ??????
                                    Log.d("listenForMessages Test", "?????? ???????????????")
                                    Log.d("listenForMessages Test", "touserid " +touserid)

                                    // ?????? ?????? ????????? ??????(??? ??? ?????? ??????) ?????? ????????? ????????????
                                    firestore?.collection("Users")?.document("user_${touserid}")?.get()?.addOnCompleteListener {
                                        // ????????? ?????? id??? ???????????? ???.
                                            task ->
                                        if(task.isSuccessful){
                                            toUser = task.result.toObject(UserModelFS::class.java)
                                            //var userName = user?.nickname.toString()
                                            toimgUri = toUser?.userProPic.toString()
                                            adapter.add(ChatToItem(chatMessage, toUser))
                                        }
                                    }

                                }
                            }else{
                                // chatImage
                                if (chatImage!!.fromId == FirebaseAuth.getInstance().uid) { //chatMessage??? ?????? ????????? ??????
                                    Log.d("listenForMessages Test", "chatImage" + chatImage)
                                    Log.d("listenForMessages Test", "?????? ??????????????? chatImage.fromId " +chatImage.fromId)
                                    Log.d("listenForMessages Test", "?????? ??????????????? chatImage.Imageuri" +chatImage.imageuri)
                                    //Log.d("listenForMessages Test", "?????? ??????????????? chatImage.text" +chatImage.)
                                    Log.d("listenForMessages Test", "FirebaseAuth.getInstance().uid " + FirebaseAuth.getInstance().uid)
                                    //val currentUser = currentUser ?: return
                                    adapter.add(ChatImageFromItem(chatImage, myuser))
                                    Log.d("listenForMessages Test", "chatImage??? currentUser" + myuser)
                                    //?????? ???????????? user??? ????????? : FirebaseAuth.getInstance().uid

                                    //adapter.add(ChatFromItem(chatMessage, currentUser))
                                    Log.d("listenForMessages Test", "adapter.add(ChatImageFromItem(chatImage, currentUser)) ??????" )
                                } else {//chatMessage??? ?????? ????????? ??????
                                    Log.d("listenForMessages Test", "?????? ???????????????")
                                    Log.d("listenForMessages Test", "chatImage touserid " +touserid)

                                    // ?????? ?????? ????????? ??????(??? ??? ?????? ??????) ?????? ????????? ????????????
                                    firestore?.collection("Users")?.document("user_${touserid}")?.get()?.addOnCompleteListener {
                                        // ????????? ?????? id??? ???????????? ???.
                                            task ->
                                        if(task.isSuccessful){
                                            toUser = task.result.toObject(UserModelFS::class.java)
                                            //var userName = user?.nickname.toString()
                                            toimgUri = toUser?.userProPic.toString()
                                            adapter.add(ChatImageToItem(chatImage, toUser))
                                        }
                                    }

                                }
                            }}
                        recyclerView_chat_log_activity.scrollToPosition(adapter.itemCount - 1)
                    }

                    override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

                    }
                    override fun onChildRemoved(snapshot: DataSnapshot) {
                    }
                    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    }
                    override fun onCancelled(error: DatabaseError) {
                    }
                })
            }
        }


    }


    private fun performSendMessage() {
        //how do we actually send a messaage to firebase
        val text = editText_chat_logg.text.toString() //????????? ??? ???????????? text??? ?????????

        val fromId = FirebaseAuth.getInstance().uid //?????? ????????? ??????????????? from
        val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        //val toId = user!!.uid
        val toId = touserid
        var prod = intent.getStringExtra("ProductID")
        //firebase??? user-message??????.
        //?????? ?????? ?????? ????????? ????????? ????????????
        //????????? ?????? ??????( current Uer )??? ?????? ???????????? ?????????_reference
        //?????? ????????? ?????? ???????????? upload?????? ?????????_toReference


        var toidprod = toId + "_" + prod
        var fromidprod = fromId + "_" + prod


        val reference = FirebaseDatabase.getInstance().getReference("/user-message/$fromId/$toidprod").push()
        val toReference = FirebaseDatabase.getInstance().getReference("/user-message/$toId/$fromidprod").push()
        if (fromId == null) return //????????? ID????????? ?????? return


        //????????? ????????? ????????? ??????.
        val chatMessage = ChatMessage(prod!!, text, fromId, toId, Calendar.getInstance().time) //class?????? ?????????
        //val chatMessage = ChatMessage(reference.key!!, text, fromId, toId, Calendar.getInstance().time) //class?????? ?????????


        reference.setValue(chatMessage)
            .addOnSuccessListener {
                Log.d(TAG, "Saved our chat message: ${reference.key}")
                editText_chat_logg.text.clear() //????????? ?????? ?????????
                recyclerView_chat_log_activity.scrollToPosition(adapter.itemCount - 1) //????????? ?????? ?????? ?????? ????????? ????????? ????????? ??????
            }
        toReference.setValue(chatMessage) //???????????? ????????? ?????? ?????? ????????? ?????? ??????

        //???????????????????????? ?????????
        val latestMessageRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId/$toidprod")
        latestMessageRef.setValue(chatMessage)

        val latestMessageToRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$toId/$fromidprod")
        latestMessageToRef.setValue(chatMessage)
    }



    class ChatFromItem(val chatmessage: ChatMessage?, val user: UserModelFS?) : Item<ViewHolder>() {
        private var firestore : FirebaseFirestore? = null
        private var storage : FirebaseStorage? = null

        private var uid = user!!.uid
        //var userId = FirebaseAuth.getInstance().currentUser!!.uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/${user!!.uid}")

        override fun bind(viewHolder: ViewHolder, position: Int) {
            //text???????????? ?????????
            //access to view holder
            viewHolder.itemView.textView_from_row.text = chatmessage!!.text
            setTimeText(viewHolder)
            //???????????? ????????????. load our user image into the User image icon
            ref.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(User::class.java)
                    val profilurl = user!!.profileImageUrl
                    Log.d("listenForMessages Test" , "profil " + profilurl)
                    Picasso.get().load(profilurl).into(viewHolder.itemView.imageview_chat_from_row)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
            //val profiluri = ref.child("profileImageUrl")
            //Log.d("listenForMessages Test" , "profil " + profiluri)



            /*
            firestore?.collection("Users")?.document("user_${user!!.uid}")?.get()?.addOnSuccessListener { doc ->
                var uri = doc?.data?.get("userProPic").toString()
                var storageRef = storage?.reference?.child("images")?.child(doc?.data?.get("userProPic").toString())
                val targetImageView = viewHolder.itemView.imageview_chat_from_row
                storageRef?.downloadUrl?.addOnSuccessListener { uri ->
                    /*Glide.with(targetImageView.context)
                            .load(uri)
                            .into(targetImageView)*/
                    Picasso.get().load(uri).into(targetImageView)
                    Log.v("IMAGE","Success")
                }?.addOnFailureListener { //????????? ?????? ?????????
                    Toast.makeText(targetImageView.context, "??????", Toast.LENGTH_SHORT).show()
                    Log.v("IMAGE","failed")
                }
            }*/

        }
        private fun setTimeText(viewHolder: ViewHolder){
            val dateFormat = SimpleDateFormat
                .getDateTimeInstance(SimpleDateFormat.SHORT,SimpleDateFormat.SHORT)
            viewHolder.itemView.chat_from_row_time.text = dateFormat.format(chatmessage!!.time)
        }

        override fun getLayout(): Int {
            return R.layout.chat_from_row
        }
    }

    class ChatToItem(val chatmessage: ChatMessage?, val user: UserModelFS?) : Item<ViewHolder>() {
        //text???????????? ?????????
        //access to view holder
        private var firestore : FirebaseFirestore? = null
        private var storage : FirebaseStorage? = null
        private lateinit var auth: FirebaseAuth
        val ref = FirebaseDatabase.getInstance().getReference("/users/${user!!.uid}")
        override fun bind(viewHolder: ViewHolder, position: Int) {
            viewHolder.itemView.textView_to_row.text = chatmessage!!.text
            setTimeText(viewHolder)

            ref.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(User::class.java)
                    val profilurl = user!!.profileImageUrl
                    Log.d("listenForMessages Test" , "profil " + profilurl)
                    Picasso.get().load(profilurl).into(viewHolder.itemView.imageview_chat_to_row)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

            val targetImageView = viewHolder.itemView.imageview_chat_from_row
        }

        private fun setTimeText(viewHolder: ViewHolder){
            val dateFormat = SimpleDateFormat
                .getDateTimeInstance(SimpleDateFormat.SHORT,SimpleDateFormat.SHORT)
            viewHolder.itemView.chat_to_row_time.text = dateFormat.format(chatmessage!!.time)
        }

        override fun getLayout(): Int {
            return R.layout.chat_to_row
        }
    }

    ///////////////////For New Model (ChatImage) to Send Image on Chatting Activity///////////////////////
    //here I start on July 19th
    class ChatImageFromItem(val chatImage: ChatImage?, val user: UserModelFS?) : Item<ViewHolder>() {
        private var firestore : FirebaseFirestore? = null
        private var storage : FirebaseStorage? = FirebaseStorage.getInstance()

        private var uid = user!!.uid
        //var userId = FirebaseAuth.getInstance().currentUser!!.uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/${user!!.uid}")

        override fun bind(viewHolder: ViewHolder, position: Int) {
            Log.d("listenForMessages Test", "bind?????? ??? ?????????")
            //text???????????? ?????????
            //access to view holder
            var photoUri: Uri? = null
            photoUri = Uri.parse(chatImage!!.imageuri)
            /* val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, photoUri)
             if(bitmap != null){
                 viewHolder.itemView.imageImageView_from_row.setImageBitmap(bitmap)
             }else{
             }*/
            Log.d("listenForMessages Test" , "chatImage!!.Imageuri " + chatImage.imageuri)
            Log.d("listenForMessages Test" , "photoUri " + photoUri)
            //Picasso.get().load(photoUri).into(viewHolder.itemView.imageImageView_from_row)
            var storageReff = storage?.reference?.child("chatting")?.child(chatImage.imageuri)
            //var storageRef = storage?.reference?.child("chatting")?.child(imgFileName)
            Log.d("listenForMessages Test", "storageReff" + storageReff)
            storageReff?.downloadUrl?.addOnSuccessListener { uri ->
                Log.d("listenForMessages Test" , "storageReff ????????? ?????????")
                Log.d("listenForMessages Test" , "chatImage!!.Imageuri " + chatImage.imageuri)
                Log.d("listenForMessages Test" , "uri " +uri)
                Picasso.get().load(uri).into(viewHolder.itemView.imageImageView_from_row)
            }
            /*
            Glide.with(applicationContext)
                    .load(photoUri)
                    .into(viewHolder.itemView.imageImageView_from_row)*/

            setTimeText(viewHolder)
            //???????????? ????????????. load our user image into the User image icon
            ref.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(User::class.java)
                    val profilurl = user!!.profileImageUrl
                    Log.d("listenForMessages Test" , "profil " + profilurl)
                    Picasso.get().load(profilurl).into(viewHolder.itemView.imageview_chat_from_row)

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }
        private fun setTimeText(viewHolder: ViewHolder){
            val dateFormat = SimpleDateFormat
                .getDateTimeInstance(SimpleDateFormat.SHORT,SimpleDateFormat.SHORT)
            viewHolder.itemView.chat_from_row_time.text = dateFormat.format(chatImage!!.time)
        }

        override fun getLayout(): Int {
            return R.layout.chat_image_from_row
        }
    }

    class ChatImageToItem(val chatImage: ChatImage?, val user: UserModelFS?) : Item<ViewHolder>() {
        //text???????????? ?????????
        //access to view holder
        private var firestore : FirebaseFirestore? = null
        private var storage : FirebaseStorage? = FirebaseStorage.getInstance()

        private lateinit var auth: FirebaseAuth
        val ref = FirebaseDatabase.getInstance().getReference("/users/${user!!.uid}")
        override fun bind(viewHolder: ViewHolder, position: Int) {
            //viewHolder.itemView.textView_to_row.text = chatmessage!!.text
            var photoUri: Uri? = null
            photoUri = Uri.parse(chatImage!!.imageuri)
            Log.d("listenForMessages Test" , "chatImage!!.Imageuri " + chatImage!!.imageuri)
            //Picasso.get().load(photoUri).into(viewHolder.itemView.imageImage_to_row)

            var storageReff = storage?.reference?.child("chatting")?.child(chatImage.imageuri)

            Log.d("listenForMessages Test", "storageReff : " + storageReff)
            storageReff?.downloadUrl?.addOnSuccessListener { uri ->
                Log.d("listenForMessages Test" , "storageReff ????????? ?????????")
                Log.d("listenForMessages Test" , "chatImage!!.Imageuri " + chatImage.imageuri)
                Log.d("listenForMessages Test" , "uri " +uri)
                Picasso.get().load(uri).into(viewHolder.itemView.imageImage_to_row)
            }

            //Glide.with().load(chatImage!!.Imageuri).into(viewHolder.itemView.imageImage_to_row)
            setTimeText(viewHolder)

            ref.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(User::class.java)
                    val profilurl = user!!.profileImageUrl
                    Log.d("listenForMessages Test" , "profil " + profilurl)
                    Picasso.get().load(profilurl).into(viewHolder.itemView.imageview_chat_to_row)
                    // Picasso.get().load(chatImage!!.Imageuri).into(viewHolder.itemView.imageImage_to_row)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

            val targetImageView = viewHolder.itemView.imageview_chat_from_row
        }

        private fun setTimeText(viewHolder: ViewHolder){
            val dateFormat = SimpleDateFormat
                .getDateTimeInstance(SimpleDateFormat.SHORT,SimpleDateFormat.SHORT)
            viewHolder.itemView.chat_to_row_time.text = dateFormat.format(chatImage!!.time)
        }

        override fun getLayout(): Int {
            return R.layout.chat_image_to_row
        }
    }

    class ChatImageItem(val chatImage: ChatImage, val user: UserModelFS?) : Item<ViewHolder>() {
        //text???????????? ?????????
        //access to view holder
        private var firestore : FirebaseFirestore? = null
        private var storage : FirebaseStorage? = FirebaseStorage.getInstance()

        private lateinit var auth: FirebaseAuth

        override fun bind(viewHolder: ViewHolder, position: Int) {
            //viewHolder.itemView.textView_to_row.text = chatmessage!!.text
            var photoUri: Uri? = null
            photoUri = Uri.parse(chatImage!!.imageuri)
            Log.d("listenForMessages Test" , "chatImage!!.Imageuri " + chatImage!!.imageuri)
            //Picasso.get().load(photoUri).into(viewHolder.itemView.imageImage_to_row)

            var storageReff = storage?.reference?.child("chatting")?.child(chatImage.imageuri)

            Log.d("listenForMessages Test", "storageReff : " + storageReff)
            storageReff?.downloadUrl?.addOnSuccessListener { uri ->
                Log.d("listenForMessages Test" , "storageReff ????????? ?????????")
                Log.d("listenForMessages Test" , "chatImage!!.Imageuri " + chatImage.imageuri)
                Log.d("listenForMessages Test" , "uri " +uri)
            }

        }


        override fun getLayout(): Int {
            return R.layout.chat_image_row
        }
    }
}
