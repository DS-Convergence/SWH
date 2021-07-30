package com.example.squirrelwarehouse

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.squirrelwarehouse.LatestMessageActivity.Companion.currentUser
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
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_from_row.view.chat_from_row_time
import kotlinx.android.synthetic.main.chat_from_row.view.imageview_chat_from_row
import kotlinx.android.synthetic.main.chat_image_from_row.view.*
import kotlinx.android.synthetic.main.chat_image_to_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.chat_to_row_time
import kotlinx.android.synthetic.main.chat_to_row.view.imageview_chat_to_row
import kotlinx.android.synthetic.main.user_row_new_message.*
import kotlinx.android.synthetic.main.user_row_new_message.view.*
import java.text.SimpleDateFormat
import java.util.*

class ChatLogListActivity : AppCompatActivity() {

    companion object {
        //var currentUser: User? = null
        val TAG = "chatLog"
    }
    private var touser: UserModelFS? = null
    val adapter = GroupAdapter<ViewHolder>()//새로운 어뎁터
    var toUser: User? = null
    private var firestore : FirebaseFirestore? = null
    private lateinit var prodUserId : String  // 물건 주인 id
    private lateinit var toimgUri : String //프로필 이미지
    private var storage : FirebaseStorage? = null
    private var uri : Uri? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        back_btn.setOnClickListener {
            //뒤로가기 버튼
            finish()
        }

        recyclerView_chat_log.adapter = adapter //새로운 object를 add할 수 있게 해주고 그럴 때마다 새롭게 refresh해줌.

        //상단 바이름 설정

        //NewMessageActivity에서 받아온 키 값으로 username 받아오기
        //username 뿐만아니라 전체 user받아올 수 있음
        toUser = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        chat_log_textview_username.text = toUser?.username //optional이니까 ?써줘야함.
        //supportActionBar?.title = toUser?.username //optional이니까 ?써줘야함.
        firestore = FirebaseFirestore.getInstance()
        var prod = intent.getStringExtra("prod").toString()
        Log.d("Eunbae","prod : " + prod)
        if (prod != null) {
            firestore?.collection("Product")?.document(prod!!)?.get()?.addOnCompleteListener { // 넘겨온 물건 id를 넣어주면 됨.
                    task ->
                if(task.isSuccessful) { // 데이터 가져오기를 성공하면
                    var product = task.result.toObject(Product::class.java)
                    chat_log_textview_productname_up.text = product?.productName.toString()
                    prodUserId = product?.userId.toString()
                    Log.v("ProdUserId",prodUserId)
                }
            }
        }

        listenForMessages() //지금까지 대화 한 내열 나열, 내가 보낸 마지막 쪽으로 커서 있게 만듦.
        //fetchCurrentUser() //지금 currentUser setting하는 메소드인데 이부분 다시 찾아보기.
        //보내기 버튼 누리면 보내지게
        camera_chat_log_more.setOnClickListener {
            //이미지 불러오기기(갤러리 접근)
            //왜 이 화면에서 이 코드가 실행이 안될까...
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = MediaStore.Images.Media.CONTENT_TYPE
            startActivityForResult(intent, 1) //PICK_IMAGE에는 본인이 원하는 상수넣으면된다.
        }

        send_button_chat_log.setOnClickListener {
            Log.d(TAG, " Attempt to send message.....")
            performSendMessage() // 새로운 메소드. 어떻게 firebase의 메세지를 보낼지
        }

        qr_button_chat_log.setOnClickListener {
            val intent = Intent(this, ChatLogMoreActivity::class.java)
            startActivityForResult(intent, 0)
        }

        val fromId = FirebaseAuth.getInstance().uid //나
        val touserid = toUser?.uid

        qr_button_chat_log.setOnClickListener {
            //ChatLogMore띄울때 정보 같이 넘겨주기
            val intent = Intent(this, ChatLogMoreActivity::class.java)
            intent.putExtra("userId1", prodUserId) //빌려주는 사람_게시글 올린 사람_QR코드 띄우기
            Log.d("CHECK_INTENT", "userId1 " + touserid)

            // 예은 코드. 확인하면 주석 지우삼
            if(fromId.equals(prodUserId)) { // 현재 이용자가 물건 주인이면 null
                intent.putExtra("userId2", touserid) //빌리는 사람_카메라 띄우기
            }
            else { // 아니면 현재 이용자 id 넘긴다
                intent.putExtra("userId2", fromId) //빌리는 사람_카메라 띄우기
            }
            Log.d("CHECK_INTENT", "userId2 " + fromId)
            intent.putExtra("productId", prod)
            Log.d("CHECK_INTENT", "productId " + prod)
            startActivityForResult(intent, 0)
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == Activity.RESULT_OK) {
                try {
                    // 사진이 돌아가는 문제가 발생하여 Glide를 이용함.

                    uri = data!!.data
                    var timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                    var imgFileName = "IMAGE_" + timeStamp + "_.jpg"
                    var storageRef = storage?.reference?.child("chatting")?.child(imgFileName)

                    storageRef?.putFile(uri!!)?.addOnSuccessListener {
                        Toast.makeText(applicationContext,"Send the photo", Toast.LENGTH_SHORT).show() //나중에 주석처리하기
                    }

                    //Glide.with().load(chatImage!!.Imageuri).into(viewHolder.itemView.imageImageView_from_row)
                    //val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                    Log.d("CHECK", "갤러리 화면 넘어옴")
                    Log.d("CHECK", "uri : "+uri)
                    Log.d("CHECK", "uri.toString() : "+uri.toString())
                    //Glide.with(this).load(uri).into(img)

                    //start here
                    //how do we actually send a messaage to firebase

                    val fromId = FirebaseAuth.getInstance().uid //나는 보내는 사람이니까 from
                    Log.d("CHECK", "보내는 사람 : " + fromId)
                    //val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
                    //val toId = user!!.uid
                    val toId = toUser?.uid
                    Log.d("CHECK", "받는 사람 : " + toId)
                    var prod =  intent.getStringExtra("prod").toString()
                    //var prod = intent.getStringExtra("prod").toString()
                    Log.d("CHECK", "prod : " + prod)
                    //firebase에 user-message만듦.
                    //두번 올려 줘야 하니까 파이어 베이스에
                    //메세지 보낸 사람( current Uer )은 보낸 메세지로 올리고_reference
                    //받은 사람은 받은 메세지로 upload되야 하니까_toReference

                    //2021_07_31수정
                    var toidprod = toId + "_" +prod
                    var fromidprod = fromId + "_" + prod

                    val reference = FirebaseDatabase.getInstance().getReference("/user-message/$fromId/$toidprod").push()
                    val toReference = FirebaseDatabase.getInstance().getReference("/user-message/$toId/$fromidprod").push()
                    if (fromId == null) return //보내는 ID없으면 그냥 return


                    val chatImage = ChatImage(prod!!, imgFileName, fromId, toId!!, Calendar.getInstance().time) //class변수 만들기

                    reference.setValue(chatImage)
                        .addOnSuccessListener {
                            Log.d("CHECK", "chatImage올림 referece는 "+ reference)
                            Log.d(ChatLogListActivity.TAG, "Saved our chat message: ${reference.key}")
                            //editText_chat_log.text.clear() //보내면 내용 지우기
                            recyclerView_chat_log.scrollToPosition(adapter.itemCount - 1) //보내면 가장 최근 보낸 메세지 쪽으로 스크롤 위치
                        }
                    toReference.setValue(chatImage) //이메일로 로그인 했을 때도 여전히 뜰수 있게
                    Log.d("CHECK", "chatImage올림 toreferece는 "+ toReference)


                    //새로보낸메세지를 위해서 사진은 안올리게 바꿈 2021_07_31
                    /*
                    //val latestMessageRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId/$toidprod")
                    //latestMessageRef.setValue(chatImage)
                    //Log.d("CHECK", "chatImage올림 latestMessageRef "+ latestMessageRef)

                    //val latestMessageToRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$toId/$fromidprod")
                    //latestMessageToRef.setValue(chatImage)
                    //Log.d("CHECK", "chatImage올림 latestMessageToRef "+ latestMessageToRef)
                    */


                    //end here


                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }


    private fun listenForMessages() { //지금까지 보낸 메세지 나열하기
        var myUser: UserModelFS? = null
        val fromId = FirebaseAuth.getInstance().uid //나
        val toId = toUser?.uid //상대방
        //쓴 메세지를 들을 수 있게
        var prod = intent.getStringExtra("prod").toString()

        var toidprod = toId + "_" +prod
        var fromidprod = fromId + "_" + prod

        val ref = FirebaseDatabase.getInstance().getReference("/user-message/$fromId/$toidprod")


        var auth : FirebaseAuth = FirebaseAuth.getInstance()
        var firestore : FirebaseFirestore? = FirebaseFirestore.getInstance()
        var uid : String? = auth.currentUser?.uid
        storage = FirebaseStorage.getInstance()



        firestore?.collection("Users")?.document("user_${uid}")?.get()?.addOnCompleteListener {// 넘겨온 물건 id를 넣어주면 됨.
                task ->
            if(task.isSuccessful) { // 데이터 가져오기를 성공하면
                var myuser = task.result.toObject(UserModelFS::class.java)
                myUser = myuser
                Log.d("listenForMessages Test", "myuser의 uid : "+ myuser!!.uid)
                Log.d("listenForMessages Test", "myuser : "+ myuser)

                ref.addChildEventListener(object : ChildEventListener {
                    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                        //이부분이 안담김 2021_07_19
                        //왜 chatImage에 안담길까
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
                                //채팅 메세지가 null이 아니라면
                                Log.d("listenForMessages Test", " chatMessage.text" + chatMessage.text)//로그 창에 보내줘
                                if (chatMessage.fromId == FirebaseAuth.getInstance().uid) { //chatMessage가 보낸 사람일 경우
                                    Log.d("listenForMessages Test", "보낸 사람일경우 chatMessage.fromId " +chatMessage.fromId)
                                    Log.d("listenForMessages Test", "FirebaseAuth.getInstance().uid " + FirebaseAuth.getInstance().uid)
                                    //val currentUser = currentUser ?: return
                                    adapter.add(ChatFromItem(chatMessage, myuser))
                                    Log.d("listenForMessages Test", "currentUser" + myuser)
                                    //지금 로그인한 user의 아이디 : FirebaseAuth.getInstance().uid

                                    //adapter.add(ChatFromItem(chatMessage, currentUser))
                                    Log.d("listenForMessages Test", "adapter.add(ChatFromItem(chatMessage, currentUser)) 실행" )
                                } else {//chatMessage가 받은 사람일 경우
                                    Log.d("listenForMessages Test", "받는 사람일경우")
                                    Log.d("listenForMessages Test", "touserid " +toId)

                                    // 내가 채팅 보내는 상대(즉 글 오린 사람) 유저 데이터 가져오기
                                    firestore?.collection("Users")?.document("user_${toId}")?.get()?.addOnCompleteListener {
                                        // 넘겨온 물건 id를 넣어주면 됨.
                                            task ->
                                        if(task.isSuccessful){
                                            touser = task.result.toObject(UserModelFS::class.java)
                                            //var userName = user?.nickname.toString()
                                            toimgUri = touser?.userProPic.toString()
                                            adapter.add(ChatToItem(chatMessage, touser))
                                        }
                                    }

                                }
                            }else{
                                // chatImage
                                if (chatImage!!.fromId == FirebaseAuth.getInstance().uid) { //chatMessage가 보낸 사람일 경우
                                    Log.d("listenForMessages Test", "chatImage" + chatImage)
                                    Log.d("listenForMessages Test", "보낸 사람일경우 chatImage.fromId " +chatImage.fromId)
                                    Log.d("listenForMessages Test", "보낸 사람일경우 chatImage.Imageuri" +chatImage.imageuri)
                                    //Log.d("listenForMessages Test", "보낸 사람일경우 chatImage.text" +chatImage.)
                                    Log.d("listenForMessages Test", "FirebaseAuth.getInstance().uid " + FirebaseAuth.getInstance().uid)
                                    //val currentUser = currentUser ?: return
                                    adapter.add(ChatImageFromItem(chatImage, myuser))
                                    Log.d("listenForMessages Test", "chatImage의 currentUser" + myuser)
                                    //지금 로그인한 user의 아이디 : FirebaseAuth.getInstance().uid

                                    //adapter.add(ChatFromItem(chatMessage, currentUser))
                                    Log.d("listenForMessages Test", "adapter.add(ChatImageFromItem(chatImage, currentUser)) 실행" )
                                } else {//chatMessage가 받은 사람일 경우
                                    Log.d("listenForMessages Test", "받는 사람일경우")
                                    Log.d("listenForMessages Test", "chatImage touserid " +toId)

                                    // 내가 채팅 보내는 상대(즉 글 오린 사람) 유저 데이터 가져오기
                                    firestore?.collection("Users")?.document("user_${toId}")?.get()?.addOnCompleteListener {
                                        // 넘겨온 물건 id를 넣어주면 됨.
                                            task ->
                                        if(task.isSuccessful){
                                            touser = task.result.toObject(UserModelFS::class.java)
                                            //var userName = user?.nickname.toString()
                                            toimgUri = touser?.userProPic.toString()
                                            adapter.add(ChatImageToItem(chatImage, touser))
                                        }
                                    }

                                }
                            }}
                        recyclerView_chat_log.scrollToPosition(adapter.itemCount - 1)
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
        val text = editText_chat_log.text.toString() //우리가 쓴 메세지를 text로 얻어와

        val fromId = FirebaseAuth.getInstance().uid //나는 보내는 사람이니까 from
        val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        val toId = user!!.uid
        var prod = intent.getStringExtra("prod").toString()


        //2021_07_31수정
        var toidprod = toId + "_" +prod
        var fromidprod = fromId + "_" + prod

        //firebase에 user-message만듦.
        //두번 올려 줘야 하니까 파이어 베이스에
        //메세지 보낸 사람( current Uer )은 보낸 메세지로 올리고_reference
        //받은 사람은 받은 메세지로 upload되야 하니까_toReference
        val reference = FirebaseDatabase.getInstance().getReference("/user-message/$fromId/$toidprod").push()
        val toReference = FirebaseDatabase.getInstance().getReference("/user-message/$toId/$fromidprod").push()
        if (fromId == null) return //보내는 ID없으면 그냥 return

        val chatMessage = ChatMessage(prod, text, fromId, toId, Calendar.getInstance().time) //class변수 만들기

        reference.setValue(chatMessage)
            .addOnSuccessListener {
                Log.d(TAG, "Saved our chat message: ${reference.key}")
                editText_chat_log.text.clear() //보내면 내용 지우기
                recyclerView_chat_log.scrollToPosition(adapter.itemCount - 1) //보내면 가장 최근 보낸 메세지 쪽으로 스크롤 위치
            }
        toReference.setValue(chatMessage) //이메일로 로그인 했을 때도 여전히 뜰수 있게

        //새로보낸메세지를 위해서
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
            //text받아와서 뛰우기
            //access to view holder
            viewHolder.itemView.textView_from_row.text = chatmessage!!.text
            setTimeText(viewHolder)
            //이미지를 로드하기. load our user image into the User image icon
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
                }?.addOnFailureListener { //이미지 로드 실패시
                    Toast.makeText(targetImageView.context, "실패", Toast.LENGTH_SHORT).show()
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
        //text받아와서 뛰우기
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
            Log.d("listenForMessages Test", "bind함수 안 들어옴")
            //text받아와서 뛰우기
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
                Log.d("listenForMessages Test" , "storageReff 안으러 들어옴")
                Log.d("listenForMessages Test" , "chatImage!!.Imageuri " + chatImage.imageuri)
                Log.d("listenForMessages Test" , "uri " +uri)
                Picasso.get().load(uri).into(viewHolder.itemView.imageImageView_from_row)
            }
            /*
            Glide.with(applicationContext)
                    .load(photoUri)
                    .into(viewHolder.itemView.imageImageView_from_row)*/

            setTimeText(viewHolder)
            //이미지를 로드하기. load our user image into the User image icon
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
        //text받아와서 뛰우기
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
                Log.d("listenForMessages Test" , "storageReff 안으로 들어옴")
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
}
