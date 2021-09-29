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
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class MyPageActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private var firestore : FirebaseFirestore? = null
    var uid : String? = null
    var nickname : String?= null
    var introduce : String? = null
    var storage : FirebaseStorage?=null
    var user : FirebaseUser? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page)
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        user = auth.currentUser

        var storageReference : StorageReference? = null
        var pathReference : StorageReference? = null


        uid = auth.currentUser?.uid
        firestore?.collection("Users")?.document("user_${uid}")?.get()?.addOnSuccessListener { doc ->
            nickname = doc?.data?.get("nickname").toString()
            introduce = doc?.data?.get("introduce").toString()
            id_sign_up_txt.text = nickname
            rating_txt.text = doc?.data?.get("rating").toString()
            introduce_txt.text = introduce
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
        powerRamgi_btn.setOnClickListener {
            val intent = Intent(this,PowerRamgiApplicationActivity::class.java)
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
        //TODO 조건 걸기 파워람쥐, 대여 상태 확인 test
        //파워람쥐
        firestore?.collection("PowerRamgi")?.whereEqualTo("uid",uid)?.get()?.addOnSuccessListener { documents->
            Log.d("DELETE", "여기로 오는 지(succ)${documents.isEmpty}")
            if(documents.isEmpty){
                Log.d("DELETE", "여기로 오는 지(null)")
                deleteDates()
                Toast.makeText(this, "Successful membership withdrawal", Toast.LENGTH_LONG).show()
            }
            else{
                Toast.makeText(this, "파워람쥐에 등록되어 있는 사용자는 탈퇴할 수 없습니다. 문제가 있다면 관리자에게 문의해주세요", Toast.LENGTH_LONG).show()
            }
        }
                ?.addOnFailureListener {
                    Log.d("DELETE", "여기로 오는 지(fail)")
                }

    }
    //실제 데이터들 삭제하는 함수
    private fun deleteDates(){
        firestore?.collection("Users")?.document("user_${uid}")?.get()?.addOnSuccessListener {  // Users에서 현재 물건 imageURI 가져옴
            doc ->
            var image = doc.data?.get("userProPic").toString()
            Log.d("DELETE", "$image")

            // Users 데이터 삭제
            firestore?.collection("Users")?.document("user_${uid}")?.delete()?.addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    // Storage에 있는 사진 데이터 삭제
                    var storageRef = storage?.reference?.child("images")?.child(image)
                    storageRef?.delete()?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("DELETE", "Success")
                            //Toast.makeText(applicationContext, "삭제 완료", Toast.LENGTH_SHORT).show()

                        }
                    }
                }

            }
        }

        firestore?.collection("StayTime")?.document("$uid")?.delete()

        firestore?.collection("Favorite")?.document("$uid")?.delete()

        user?.delete()
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("DELETE", "여기로 오는 지(delete데이터 함수)")
                    }
                }
    }
}