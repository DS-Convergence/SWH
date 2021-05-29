package com.example.squirrelwarehouse

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.squirrelwarehouse.models.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.activity_chat_log.back_btn
import kotlinx.android.synthetic.main.activity_my_page.*
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*
import java.text.SimpleDateFormat
import java.util.*


class ChatLogActivity : AppCompatActivity() {
    companion object {
        //var currentUser: User? = null
        val TAG = "chatLog"
    }

    // private lateinit var user: UserModelFS
    val adapter = GroupAdapter<ViewHolder>()//새로운 어뎁터
    // var toUser: User? = null
    private var toUser: UserModelFS? = null
    private var firestore : FirebaseFirestore? = null
    private var storage : FirebaseStorage? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var touserid : String //유저아이디
    private lateinit var toimgUri : String //프로필 이미지


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        back_btn.setOnClickListener {
            finish()
        }

        recyclerView_chat_log.adapter = adapter //새로운 object를 add할 수 있게 해주고 그럴 때마다 새롭게 refresh해줌.

        //ProductDetailActivity에서 데이터 받아오기
        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()

        var intent = intent
        var prod = intent.getStringExtra("ProductID")
        touserid = intent.getStringExtra("UserId").toString()


        if (prod != null) {
            firestore?.collection("Product")?.document(prod!!)?.get()?.addOnCompleteListener { // 넘겨온 물건 id를 넣어주면 됨.
                    task ->
                if(task.isSuccessful) { // 데이터 가져오기를 성공하면
                    var product = task.result.toObject(Product::class.java)

                    chat_log_textview_productname_up.setText(product?.productName)
                    chat_log_textview_username.setText(product?.userName)
                    //userid = product?.userId.toString()


                    // 유저 데이터 가져오기
                    firestore?.collection("Users")?.document(touserid!!)?.get()?.addOnCompleteListener {
                        // 넘겨온 물건 id를 넣어주면 됨.
                            task ->
                        if(task.isSuccessful){
                            toUser = task.result.toObject(UserModelFS::class.java)
                            //var userName = user?.nickname.toString()
                            toimgUri = toUser?.userProPic.toString()

                            /*
                            // 사진 불러오기
                            var storageRef = storage?.reference?.child("product")?.child(user?.userProPic.toString())
                            storageRef?.downloadUrl?.addOnSuccessListener { uri ->
                                /*
                                Glide.with(applicationContext)
                                    .load(uri)
                                    .into(img)
                                img.visibility = View.VISIBLE
                                //beforeURI = uri
                                //Log.v("IMAGE",uri.toString())
                                 */
                                imgUri = uri.toString()


                            }?.addOnFailureListener { //이미지 로드 실패시
                                Toast.makeText(applicationContext, "실패", Toast.LENGTH_SHORT).show()
                                Log.v("IMAGE","failed")
                            }*/
                        }
                    }

                }
            }
        }

        //상단 바이름 설정
        //NewMessageActivity에서 받아온 키 값으로 username 받아오기
        //username 뿐만아니라 전체 user받아올 수 있음
        //toUser = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        //chat_log_textview_username.text = toUser?.username //optional이니까 ?써줘야함.
        //supportActionBar?.title = toUser?.username //optional이니까 ?써줘야함.

        listenForMessages() //지금까지 대화 한 내열 나열, 내가 보낸 마지막 쪽으로 커서 있게 만듦.
        //fetchCurrentUser() //지금 currentUser setting하는 메소드인데 이부분 다시 찾아보기.
        //보내기 버튼 누리면 보내지게
        send_button_chat_log.setOnClickListener {
            Log.d(TAG, " Attempt to send message.....")
            performSendMessage() // 새로운 메소드. 어떻게 firebase의 메세지를 보낼지
        }

        qr_button_chat_log.setOnClickListener {
            val intent = Intent(this, ChatLogMoreActivity::class.java)
            startActivity(intent) //NewMessageActivity창 뜸.
        }

    }

    private fun listenForMessages() { //지금까지 보낸 메세지 나열하기
        var myUser: UserModelFS? = null
        val fromId = FirebaseAuth.getInstance().uid //나
        Log.d("listenForMessages Test", "fromId : " +fromId)
        //val toId = toUser?.uid //상대방
        val toId = touserid
        Log.d("listenForMessages Test", "touserid : " +touserid)

        //쓴 메세지를 들을 수 있게
        val ref = FirebaseDatabase.getInstance().getReference("/user-message/$fromId/$toId")

        var auth : FirebaseAuth = FirebaseAuth.getInstance()
        var firestore : FirebaseFirestore? = FirebaseFirestore.getInstance()
        var uid : String? = auth.currentUser?.uid
        storage = FirebaseStorage.getInstance()

        firestore?.collection("Users")?.document("user_${uid}")?.get()?.addOnCompleteListener {// 넘겨온 물건 id를 넣어주면 됨.
                task ->
            if(task.isSuccessful) { // 데이터 가져오기를 성공하면
                var myuser = task.result.toObject(UserModelFS::class.java)
                myUser = myuser
            }
        }

        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java)
                if (chatMessage != null) {
                    Log.d("listenForMessages Test", "chatMessage is not null")
                    //채팅 메세지가 null이 아니라면
                    Log.d("listenForMessages Test", " chatMessage.text" + chatMessage.text)//로그 창에 보내줘
                    if (chatMessage.fromId == FirebaseAuth.getInstance().uid) { //chatMessage가 보낸 사람일 경우
                        Log.d("listenForMessages Test", "보낸 사람일경우")
                        //val currentUser = currentUser ?: return
                        adapter.add(ChatFromItem(chatMessage, fromId))
                        Log.d("listenForMessages Test", "currentUser" + fromId)
                        //지금 로그인한 user의 아이디 : FirebaseAuth.getInstance().uid

                        //adapter.add(ChatFromItem(chatMessage, currentUser))
                        Log.d("listenForMessages Test", "adapter.add(ChatFromItem(chatMessage, currentUser)) 실행" )
                    } else {//chatMessage가 받은 사람일 경우
                        Log.d("listenForMessages Test", "받는 사람일경우")
                        adapter.add(ChatToItem(chatMessage, toUser))
                        Log.d("listenForMessages Test", "adapter.add(ChatToItem(chatMessage, currentUser)) 실행" )
                    }
                }
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


    private fun performSendMessage() {
        //how do we actually send a messaage to firebase
        val text = editText_chat_log.text.toString() //우리가 쓴 메세지를 text로 얻어와

        val fromId = FirebaseAuth.getInstance().uid //나는 보내는 사람이니까 from
        val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        //val toId = user!!.uid
        val toId = touserid

        //firebase에 user-message만듦.
        //두번 올려 줘야 하니까 파이어 베이스에
        //메세지 보낸 사람( current Uer )은 보낸 메세지로 올리고_reference
        //받은 사람은 받은 메세지로 upload되야 하니까_toReference
        val reference = FirebaseDatabase.getInstance().getReference("/user-message/$fromId/$toId").push()
        val toReference = FirebaseDatabase.getInstance().getReference("/user-message/$toId/$fromId").push()
        if (fromId == null) return //보내는 ID없으면 그냥 return

        val chatMessage = ChatMessage(reference.key!!, text, fromId, toId, Calendar.getInstance().time) //class변수 만들기
        //val chatMessage = ChatMessage(reference.key!!, text, fromId, toId, Calendar.getInstance().time) //class변수 만들기

        reference.setValue(chatMessage)
            .addOnSuccessListener {
                Log.d(TAG, "Saved our chat message: ${reference.key}")
                editText_chat_log.text.clear() //보내면 내용 지우기
                recyclerView_chat_log.scrollToPosition(adapter.itemCount - 1) //보내면 가장 최근 보낸 메세지 쪽으로 스크롤 위치
            }
        toReference.setValue(chatMessage) //이메일로 로그인 했을 때도 여전히 뜰수 있게

        //새로보낸메세지를 위해서
        val latestMessageRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId/$toId")
        latestMessageRef.setValue(chatMessage)

        val latestMessageToRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$toId/$fromId")
        latestMessageToRef.setValue(chatMessage)
    }

    class ChatFromItem(val chatmessage: ChatMessage?, val user: String?) : Item<ViewHolder>() {
        private var firestore : FirebaseFirestore? = null
        private var storage : FirebaseStorage? = null

        //var userId = FirebaseAuth.getInstance().currentUser!!.uid

        override fun bind(viewHolder: ViewHolder, position: Int) {
            //text받아와서 뛰우기
            //access to view holder
            viewHolder.itemView.textView_from_row.text = chatmessage!!.text
            setTimeText(viewHolder)
            //이미지를 로드하기. load our user image into the User image icon
            val targetImageView = viewHolder.itemView.imageview_chat_to_row
            // val uri = user?.userProPic
            // val targetImageView = viewHolder.itemView.imageview_chat_from_row
            firestore?.collection("Users")?.document("user_${user}")?.get()
                ?.addOnSuccessListener { doc ->
                    var storageRef = storage?.reference?.child("images")
                        ?.child(doc?.data?.get("userProPic").toString())
                    storageRef?.downloadUrl?.addOnSuccessListener { uri ->
                        Glide.with(viewHolder.itemView.context)
                            .load(uri)
                            .into(targetImageView)
                        Log.v("IMAGE", "Success")
                    }?.addOnFailureListener { //이미지 로드 실패시
                        Toast.makeText(viewHolder.itemView.context, "실패", Toast.LENGTH_SHORT).show()
                        Log.v("IMAGE", "failed")

                    }
                    /*
            firestore?.collection("Users")?.document("user_${user}")?.get()?.addOnCompleteListener {  // Users에서 현재 userId를 가진 데이터를 가져옴
                    task ->
                if(task.isSuccessful) { // 데이터 가져오기를 성공하면
                    var user = task.result.toObject(UserModelFS::class.java)
                    val targetImageView = viewHolder.itemView.imageview_chat_to_row
                    storage = FirebaseStorage.getInstance()
                    var storageRef = storage?.reference?.child("images")?.child(user?.userProPic.toString())
                    storageRef?.downloadUrl?.addOnSuccessListener { uri ->
                        Glide.with(viewHolder.itemView.context)
                            .load(uri)
                            .into(targetImageView)
                        Log.v("IMAGE", "Success")
                    }?.addOnFailureListener { //이미지 로드 실패시
                        Toast.makeText(viewHolder.itemView.context, "실패", Toast.LENGTH_SHORT).show()
                        Log.v("IMAGE", "failed")
                    }
                }
            }*/
                    //Picasso.get().load(uri).into(targetImageView) //imageview_chat_from_row  내쪽 프로필 설정
                }
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

        override fun bind(viewHolder: ViewHolder, position: Int) {
            viewHolder.itemView.textView_to_row.text = chatmessage!!.text
            setTimeText(viewHolder)

            //이미지를 로드하기. load our user image into the User image icon
            //val uri = ImageView.setImageURI(Uri.parse(File(user?.userProPic).toString()));
            //val uri = user?.userProPic
            // 사진 불러오기
            firestore?.collection("Users")?.document("user_${user!!.uid}")?.get()?.addOnCompleteListener { // 넘겨온 물건 id를 넣어주면 됨.
                    task ->
                if (task.isSuccessful) { // 데이터 가져오기를 성공하면
                    var user = task.result.toObject(UserModelFS::class.java)
                    val targetImageView = viewHolder.itemView.imageview_chat_to_row
                    storage = FirebaseStorage.getInstance()
                    var storageRef = storage?.reference?.child("images")?.child(user?.userProPic.toString())
                    storageRef?.downloadUrl?.addOnSuccessListener { uri ->
                        Glide.with(viewHolder.itemView.context)
                            .load(uri)
                            .into(targetImageView)
                        Log.v("IMAGE", "Success")

                    }?.addOnFailureListener { //이미지 로드 실패시
                        Toast.makeText(viewHolder.itemView.context, "실패", Toast.LENGTH_SHORT).show()
                        Log.v("IMAGE", "failed")
                    }


                }
            }

            //val targetImageView = viewHolder.itemView.imageview_chat_to_row
           // Picasso.get().load(uri).into(targetImageView) //imageview_chat_to_row  tkdeoqkd 프로필 설정

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
}
