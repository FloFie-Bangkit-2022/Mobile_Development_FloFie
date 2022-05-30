package com.capstone.flofie.ui.camera

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.capstone.flofie.databinding.ActivityMainCameraBinding
import android.view.MotionEvent
import android.widget.ImageView
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.capstone.flofie.createFile
import com.capstone.flofie.uriToFile
import java.io.File

class MainCameraActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainCameraBinding

    private var imageCapture: ImageCapture? = null
    private var getFile : File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        setupCamButton()

        startCamera()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupCamButton() {
        binding.apply {
            mainCamCaptureBtn.setOnTouchListener { v, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        val view = v as ImageView
                        view.drawable.setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP)
                        view.invalidate()
                    }
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        val view = v as ImageView
                        view.drawable.clearColorFilter()
                        view.invalidate()
                    }
                }
                false
            }
            mainCamCaptureBtn.setOnClickListener {
                takePhoto()
            }

            mainCamChooseFile.setOnTouchListener { v, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        val view = v as ImageView
                        view.drawable.setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP)
                        view.invalidate()
                    }
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        val view = v as ImageView
                        view.drawable.clearColorFilter()
                        view.invalidate()
                    }
                }
                false
            }
            mainCamChooseFile.setOnClickListener {
                startGallery()
            }

            mainCamBackBtn.setOnTouchListener { v, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        val view = v as ImageView
                        view.drawable.setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP)
                        view.invalidate()
                    }
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        val view = v as ImageView
                        view.drawable.clearColorFilter()
                        view.invalidate()
                    }
                }
                false
            }
            mainCamBackBtn.setOnClickListener {
                onBackPressed()
            }

            mainCamTorchBtn.drawable.setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP)
        }
    }

    private fun startCamera() {
        val providedCamera = ProcessCameraProvider.getInstance(this)

        providedCamera.addListener({
            val cameraProvider : ProcessCameraProvider = providedCamera.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.mainCamCaptureView.surfaceProvider)
            }

            imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                val camera = cameraProvider.bindToLifecycle(
                    this,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview,
                    imageCapture)

                if (camera.cameraInfo.hasFlashUnit()) {
                    val cameraControl = camera.cameraControl
                    binding.mainCamTorchBtn.setOnClickListener {
                        if (camera.cameraInfo.torchState.value == TorchState.OFF) {
                            cameraControl.enableTorch(true)
                            val view = it as ImageView
                            view.drawable.clearColorFilter()
                            view.invalidate()
                        }
                        else if (camera.cameraInfo.torchState.value == TorchState.ON){
                            cameraControl.enableTorch(false)
                            val view = it as ImageView
                            view.drawable.setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP)
                            view.invalidate()
                        }
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(this, "Gagal membuka kamera, due : " + e.message, Toast.LENGTH_SHORT).show()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        val photoFile = createFile(application)

        val outpuOption = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture.takePicture(outpuOption, ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    Toast.makeText(this@MainCameraActivity, "Berhasil mengambil gambar", Toast.LENGTH_SHORT).show()

                    val intentBackToCameraFragment = Intent()
                    Log.d("CEK_PHOTOFILE_PATH", photoFile.path.toString())
                    Log.d("CEK_PHOTOFILE", photoFile.toString())
                    intentBackToCameraFragment.putExtra("picture", photoFile)

                    setResult(CameraFragment.CAMERA_RESULT, intentBackToCameraFragment)
                    finish()
                }

                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(this@MainCameraActivity, "Gagal mengambil gambar due : " + exception.message, Toast.LENGTH_LONG).show()
                }
            }
        )
    }

    private fun startGallery() {
        val intentGalery = Intent()
        intentGalery.action = Intent.ACTION_GET_CONTENT
        intentGalery.type = "image/*"
        val chooser = Intent.createChooser(intentGalery, "Choose a picture")
        launcherIntentGaleri.launch(chooser)
    }

    private val launcherIntentGaleri = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val selectedImage : Uri = it?.data?.data as Uri

            val myFile = uriToFile(selectedImage, this)
            getFile = myFile

            val intentBackToCameraFragment = Intent()

            intentBackToCameraFragment.putExtra("imageGaleryFile", getFile)
            setResult(CameraFragment.GALERY_RESULT, intentBackToCameraFragment)
            finish()
        }
    }

    private fun hideSystemUI() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

    override fun onResume() {
        super.onResume()
        hideSystemUI()
        startCamera()
    }
}