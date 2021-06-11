package com.example.squirrelwarehouse

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.squirrelwarehouse.models.Product
import com.example.squirrelwarehouse.models.UserModelFS
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import kotlin.collections.List

class ProductFormActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var etProdName : EditText
    private lateinit var etCategory : EditText
    private lateinit var etProdDetail : EditText
    private lateinit var etRentalFee : EditText
    private lateinit var etDeposit : EditText

    private lateinit var cbRentalFee : CheckBox
    private lateinit var cbDeposit : CheckBox
    private lateinit var cbLocation : CheckBox

    private lateinit var map : FrameLayout
    private lateinit var img : ImageView

    private lateinit var spCategory : Spinner

    private var mMap: GoogleMap? = null

    private var firestore : FirebaseFirestore? = null
    private var storage : FirebaseStorage? = null

    private var uri : Uri? = null

    private lateinit var auth: FirebaseAuth

    private var text : String? = null

    private var beforeURI : Uri? = null     // 이전 사진
    private var imageChange = false


    // tensorflow Lite
    private val MODEL_PATH = "mobilenet_quant_v1_224.tflite"
    private val QUANT = true
    private val LABEL_PATH = "labels.txt"
    private val INPUT_SIZE = 224

    private var classifier: Classifier? = null

    private val executor: Executor = Executors.newSingleThreadExecutor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_form)

        // 변수 초기화
        etProdName = findViewById(R.id.et_prodName)
        etCategory = findViewById(R.id.et_category)
        etProdDetail = findViewById(R.id.et_prodDetail)
        etRentalFee = findViewById(R.id.et_rentalFee)
        etDeposit = findViewById(R.id.et_deposit)

        cbRentalFee = findViewById(R.id.cb_rentalFee)
        cbDeposit = findViewById(R.id.cb_deposit)
        cbLocation = findViewById(R.id.cb_location)

        map = findViewById(R.id.layout_map)
        img = findViewById(R.id.img)

        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()

        val btnUpload : Button = findViewById(R.id.btn_upload)
        val btnBack : TextView = findViewById(R.id.back_btn)
        val btnImg : Button = findViewById(R.id.btn_img)

        // 스패너
        spCategory = findViewById(R.id.sp_category)
        val category = resources.getStringArray(R.array.category)
        var adapterCate : ArrayAdapter<String>
        adapterCate = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, category)
        spCategory.adapter = adapterCate


        // 수정할 경우
        val intent = intent
        text = intent.getStringExtra("ModifyProduct")

        if(text!=null) {

            btnUpload.setText("수정")

            firestore?.collection("Product")?.document(text!!)?.get()?.addOnCompleteListener { // 넘겨온 물건 id를 넣어주면 됨.
                    task ->
                if(task.isSuccessful) { // 데이터 가져오기를 성공하면
                    var product = task.result.toObject(Product::class.java)
                    etProdName.setText(product?.productName)
                    //etCategory.setText(product?.category)
                    spCategory.setSelection(getCateNum(product?.category))


                    etProdDetail.setText(product?.productDetail)
                    // 보증금 체크박스랑 다 고려해주어야함. null이 아니면 체크박스 체크하고, 금액 표시
                    // 대여료

                    if(!product?.deposit.equals("")){
                        cbDeposit.isChecked = true;
                        etDeposit.setText(product?.deposit)
                    }

                    if(!product?.rentalFee.equals("")){
                        cbRentalFee.isChecked = true;
                        etRentalFee.setText(product?.rentalFee)
                    }

                    // 사진 불러오기
                    var storageRef = storage?.reference?.child("product")?.child(product?.imageURI.toString())
                    storageRef?.downloadUrl?.addOnSuccessListener { uri ->
                        Glide.with(applicationContext)
                            .load(uri)
                            .into(img)
                        img.visibility = View.VISIBLE
                        //beforeURI = uri
                        //Log.v("IMAGE",uri.toString())
                    }?.addOnFailureListener { //이미지 로드 실패시
                        Toast.makeText(applicationContext, "실패", Toast.LENGTH_SHORT).show()
                        Log.v("IMAGE","failed")
                    }


                }
            }
        }



        // 지도
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)


        // 지도안에서 터치가 일어나면 스크롤뷰는 움직이지 않도록 설정함.
        val sv = findViewById<ScrollView>(R.id.sv)
        val containerMap = findViewById<TouchFrameLayout>(R.id.layout_map)
        containerMap.setTouchListener(object : TouchFrameLayout.OnTouchListener {
            override fun onTouch() {
                sv.requestDisallowInterceptTouchEvent(true)
            }
        })


        // 대여료 체크박스가 체크되어야 금액 작성 가능.
        cbRentalFee.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) etRentalFee.isEnabled = true
            else etRentalFee.isEnabled = false
        }

        // 보증금 체크박스가 체크되어야 금액 작성 가능
        cbDeposit.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) etDeposit.isEnabled = true
            else etDeposit.isEnabled = false
        }

        // 위치 체크박스가 체크되어야 지도 보임.
        cbLocation.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) map.visibility = View.VISIBLE
            else map.visibility = View.GONE

        }

        // 뒤로가기 버튼
        btnBack.setOnClickListener() {
            finish()
        }

        // 사진을 가져오는 버튼
        btnImg.setOnClickListener {
            //이미지 불러오기기(갤러리 접근)
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = MediaStore.Images.Media.CONTENT_TYPE
            startActivityForResult(intent, 0) //PICK_IMAGE에는 본인이 원하는 상수넣으면된다.
        }

        // 글 업로드 버튼
        btnUpload.setOnClickListener {

            var pName = etProdName.text.toString()
            //var pCate = etCategory.text.toString()
            var pCate = spCategory.selectedItem.toString()
            var pDetail = etProdDetail.text.toString()
            var pDeposit = etDeposit.text.toString()
            var pRental = etRentalFee.text.toString()

            /*
            if(pDeposit.equals("")) pDeposit = "0"
            if(pRental.equals("")) pRental = "0"
            */

            var timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            var imgFileName = "IMAGE_" + timeStamp + "_.jpg"
            var storageRef = storage?.reference?.child("product")?.child(imgFileName)


            // 처음 올리는 경우. 즉 수정이 아닌 경우
            if(btnUpload.text.equals("업로드")) {

                // 데이터베이스에 데이터 삽입
                // 현재 사용자 userId를 통해 Users에 있는 데이터를 가져옴.
                // 가져온 데이터에서 nickname을 가져온다.
                // 작성된 데이터를 Product 컬렉션에 넣어 데이터베이스에 삽입.
                var userId = FirebaseAuth.getInstance().currentUser!!.uid
                var document = "user_" + userId
                firestore?.collection("Users")?.document(document)?.get()?.addOnCompleteListener {  // Users에서 현재 userId를 가진 데이터를 가져옴
                        task ->
                    if(task.isSuccessful) { // 데이터 가져오기를 성공하면
                        var user = task.result.toObject(UserModelFS::class.java)
                        var userName = user?.nickname.toString()

                        if (userName != null) { // 닉네임이 null이 아닐 경우 Product 컬렉션을 만들어 firestore에 삽입.
                            //Log.v("UserName",userName)

                            // 나중에 timestamp 형식 바꿀일 있을 때 아래 링크 참고
                            // http://blog.naver.com/PostView.nhn?blogId=traeumen927&logNo=221493556497&parentCategoryNo=&categoryNo=&viewDate=&isShowPopularPosts=false&from=postView
                            // 책 210쪽 보고 수정하기
                            var product = Product(userId,userName,pName, pCate, pDetail, imgFileName, pDeposit, pRental, timeStamp,null, "대여 전")
                            firestore?.collection("Product")?.document()?.set(product)?.addOnCompleteListener {
                                    task ->
                                if(task.isSuccessful) { // Product 컬렉션에 성공적으로 삽입되었을 경우, 사진을 storage에 넣어야함.
                                    // 사진을 데이터베이스로 넘겨야함.
                                    // https://riapapa-collection.tistory.com/42
                                    // 그냥하면 권한 없어서 에러남. storage 규칙 변경해야함.
                                    storageRef?.putFile(uri!!)?.addOnSuccessListener {
                                        Toast.makeText(applicationContext,"Uploaded",Toast.LENGTH_SHORT).show() // 잘 들어갔나 확인 하려고 적어 놓음.
                                        // 먼저 메인화면으로 돌아 갔다가
                                        // ProductDetailActivity로 넘어가도록.
                                        // 뒤로 가는 버튼으로 글쓰기 화면이 다시 나오지 않도록

                                        finish()

                                    }
                                }
                            }
                        }
                    }
                }

            }

            // 수정인 경우
            if(btnUpload.text.equals("수정")) {
                var map = mutableMapOf<String,Any>()
                map["productName"] = pName
                map["category"] = pCate
                map["productDetail"] = pDetail

                if(!cbDeposit.isChecked) pDeposit = ""
                if(!cbRentalFee.isChecked) pRental = ""

                map["deposit"] = pDeposit
                map["rentalFee"] = pRental

                if(imageChange) {
                    // 사진첩에 들어갔을 경우. 사진을 바꿀 경우 imageChange가 true가 됨
                    // true일 때만 imageURI를 업데이트
                    map["imageURI"] = imgFileName
                }

                // 대여료
                // 보증금
                if(text!=null) {
                    firestore?.collection("Product")?.document(text!!)?.update(map)?.addOnCompleteListener {
                            task ->
                        if(task.isSuccessful) {
                            Log.v("Update","Success")

                            if(imageChange) {
                                storageRef?.putFile(uri!!)?.addOnSuccessListener {
                                    // imageChange가 true일 때만 사진 추가
                                    // 성공적으로 추가했으면 imageChange 다시 false로
                                    Log.v("Update", "SuccessIMAGE") // 잘 들어갔나 확인 하려고 적어 놓음.
                                    imageChange = false

                                    // 물건 상세 페이지로 넘어가야함.

                                }
                            }

                        }
                    }
                }

                finish()
            }



        }

        // tensorflow Lite 초기화
        initTensorFlowAndLoadModel()

    }

    //갤러리에서 이미지 불러온 후 행동
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0) {
            // Make sure the request was successful
            if (resultCode == Activity.RESULT_OK) {
                try {
                    // 선택한 이미지를 가져옴.
                    // 사진이 돌아가는 문제가 발생하여 Glide를 이용함.
                    uri = data!!.data
                    Glide.with(this).load(uri).into(img)
                    img.visibility = View.VISIBLE

                    //imgUri = absolutelyPath(uri)

                    // 사진이 바뀌면 true
                    imageChange = true;


                    // 사진 비트맵으로 변환
                    // 모델에 넣어서 결과 가져오기
                    var bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                    bitmap = Bitmap.createScaledBitmap(bitmap!!, INPUT_SIZE, INPUT_SIZE, false)
                    val results: List<Classifier.Recognition> = classifier!!.recognizeImage(bitmap)
                    // 밑에 이코드는 나중에 매핑하고 7개의 카테고리가 뜨게 할 것임.
                    // 나중에 바뀔 코드
                    etCategory.setText(results.get(0).toString())    // 가장 퍼센트가 높은 물건 하나만 가져오기
                    var cate = Category(results.get(0).toString())
                    spCategory.setSelection(cate.category)


                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val SEOUL = LatLng(37.56, 126.97)
        val markerOptions = MarkerOptions()
        markerOptions.position(SEOUL)
        markerOptions.title("서울")
        markerOptions.snippet("한국의 수도")
        mMap!!.addMarker(markerOptions)


        // 기존에 사용하던 다음 2줄은 문제가 있습니다.

        // CameraUpdateFactory.zoomTo가 오동작하네요.
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));
        //mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
        mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(SEOUL, 10f))
    }

    // 사진을 절대경로로 저장 >>>>> 사용 x 삭제바람
    fun absolutelyPath(path: Uri): String? {

        var proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        var c: Cursor? = contentResolver.query(path, proj, null, null, null)
        var index = c?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        if (c != null) {
            c.moveToFirst()
        }

        var result = index?.let { c?.getString(it) }

        return result
    }

    fun getCateNum(cate: String?) : Int {

        when(cate) {
            "디지털/가전" -> return 0
            "생활/인테리어" -> return 1
            "스포츠/레저" -> return 2
            "패션/뷰티/미용" -> return 3
            "문구/완구" -> return 4
            "도서" -> return 5
            "음악" -> return 6
            "기타" -> return 7
            else -> return 7
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        executor.execute { classifier!!.close() }
    }

    private fun initTensorFlowAndLoadModel() {
        executor.execute {
            classifier = try {
                TensorFlowImageClassifier.create(
                    assets,
                    MODEL_PATH,
                    LABEL_PATH,
                    INPUT_SIZE,
                    QUANT)
                //makeButtonVisible();
            } catch (e: java.lang.Exception) {
                throw RuntimeException("Error initializing TensorFlow!", e)
            }
        }
    }

}


// 지도를 확대하는데 스크롤이 움직이는 현상을 막기 위함.
class TouchFrameLayout(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {
    var listener: OnTouchListener? = null

    interface OnTouchListener {
        fun onTouch()
    }

    fun setTouchListener(listener: OnTouchListener) {
        this.listener = listener
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_UP -> listener?.onTouch()
        }
        return super.dispatchTouchEvent(event)
    }
}