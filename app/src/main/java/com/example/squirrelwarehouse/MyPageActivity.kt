package com.example.squirrelwarehouse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_my_page.*
import android.util.Log
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class MyPageActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private var firestore : FirebaseFirestore? = null
    var uid : String? = null
    var nickname : String?= null
    var storage : FirebaseStorage?=null
    var uri : String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page)
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()

        var storageReference : StorageReference? = null
        var pathReference : StorageReference? = null


        uid = auth.currentUser?.uid
        firestore?.collection("Users")?.document("user_${uid}")?.get()?.addOnSuccessListener { doc ->
            nickname = doc?.data?.get("nickname").toString()
            uri = doc?.data?.get("userProPic").toString()
            id_sign_up_txt.text = nickname
            var storageRef = storage?.reference?.child("images")?.child(doc?.data?.get("userProPic").toString())
            storageRef?.downloadUrl?.addOnSuccessListener { uri ->
                Glide.with(applicationContext)
                    .load(uri)
                    .into(user_propic_img)
                Log.v("IMAGE","Success")
            }?.addOnFailureListener { //이미지 로드 실패시
                Toast.makeText(applicationContext, "실패", Toast.LENGTH_SHORT).show()
                Log.v("IMAGE","failed")

            }

            Log.d("로그-1-success-record받기-","nickname ${nickname}")
        }

        terms_btn.setOnClickListener { // 개인정보 정책 으로 이동하는 버튼
            val intent = Intent(this, PrivacyActivity::class.java)
            startActivity(intent)
            //TermsAndConditionsActivity
        }

        my_list_btn.setOnClickListener {
            val intent = Intent(this, MyListActivity::class.java)
            startActivity(intent)
        }
        transaction_list_btn.setOnClickListener {
            val intent = Intent(this, TransactionListActivity::class.java)
            startActivity(intent)
        }
        my_fav_list_btn.setOnClickListener {
            val intent = Intent(this, MyFavoriteListActivity::class.java)
            startActivity(intent)
        }
        user_setting_btn.setOnClickListener {
            val intent = Intent(this, UserSettingActivity::class.java)
            startActivity(intent)
            finish()
        }
        back_btn.setOnClickListener {
            finish()
        }
        set_location_btn.setOnClickListener {
            val intent = Intent(this, SetLocationInformationActivity::class.java)
            startActivity(intent)
        }
        logout_btn.setOnClickListener {
            auth.signOut()
            val intent = Intent(this,LogInActivity::class.java)
            startActivity(intent)
            //finish() 로그아웃하면 정보를 다 날려야 함
        }
        withdrawal_btn.setOnClickListener {
            val mAlertDialog = AlertDialog.Builder(this)
            mAlertDialog.setTitle("회원탈퇴")
            mAlertDialog.setMessage("삭제된 계정은 복구할 수 없으며 해당 계정의 게시물과 정보는 완전히 삭제됩니다. " +
                    "정말 탈퇴하겠습니까?")
            mAlertDialog.setPositiveButton("Yes") { dialog, id ->
                //perform some tasks here
                deleteId()
                auth.signOut()
                val intent = Intent(this, LogInActivity :: class.java )//로그인화면으로 넘어감
                startActivity(intent)
            }
            mAlertDialog.setNegativeButton("No") { dialog, id ->
                //perform som tasks here
            }
            mAlertDialog.show()
        }
    }

    // 회원탈퇴 함수
    fun deleteId() {
        auth?.currentUser?.delete()
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Successful membership withdrawal", Toast.LENGTH_LONG).show()
                    }
                }
    }
}