package com.example.squirrelwarehouse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.CaptureManager
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.journeyapps.barcodescanner.DecoratedBarcodeView.TorchListener

class ProductRentalCameraActivity : AppCompatActivity(), TorchListener {

    private var m_captureManager: CaptureManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_rental_camera)

        val decoratedBarcodeView =
            findViewById<DecoratedBarcodeView>(R.id.qr_reader_view)
        decoratedBarcodeView.setTorchListener(this)
        m_captureManager = CaptureManager(this, decoratedBarcodeView)
        m_captureManager!!.initializeFromIntent(intent, savedInstanceState)
        m_captureManager!!.decode()

    }

    override fun onTorchOn() {}
    override fun onTorchOff() {}
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        m_captureManager!!.onSaveInstanceState(outState)
    }

    override fun onPause() {
        super.onPause()
        m_captureManager!!.onPause()
    }

    override fun onResume() {
        super.onResume()
        m_captureManager!!.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        m_captureManager!!.onDestroy()
    }
}