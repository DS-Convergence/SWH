package com.example.squirrelwarehouse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView

class ProductFormActivity : AppCompatActivity() {

    private lateinit var etProdName : EditText
    private lateinit var etCategory : EditText
    private lateinit var etProdDetail : EditText
    private lateinit var etRentalFee : EditText
    private lateinit var etDeposit : EditText

    private lateinit var cbRentalFee : CheckBox
    private lateinit var cbDeposit : CheckBox
    private lateinit var cbLocation : CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_form)

        etProdName = findViewById(R.id.et_prodName)
        etCategory = findViewById(R.id.et_category)
        etProdDetail = findViewById(R.id.et_prodDetail)
        etRentalFee = findViewById(R.id.et_rentalFee)
        etDeposit = findViewById(R.id.et_deposit)

        cbRentalFee = findViewById(R.id.cb_rentalFee)
        cbDeposit = findViewById(R.id.cb_deposit)
        cbLocation = findViewById(R.id.cb_location)

        val btnUpload : Button = findViewById(R.id.btn_upload)
        val backButton : TextView = findViewById(R.id.back_btn)

        cbRentalFee.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) etRentalFee.isEnabled = true
            else etRentalFee.isEnabled = false
        }

        cbDeposit.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) etDeposit.isEnabled = true
            else etDeposit.isEnabled = false
        }

        backButton.setOnClickListener() {
            finish()
        }

    }
}