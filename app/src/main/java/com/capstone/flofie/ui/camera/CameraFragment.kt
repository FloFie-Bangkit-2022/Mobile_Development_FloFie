package com.capstone.flofie.ui.camera

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.capstone.flofie.ViewModelFactory
import com.capstone.flofie.databinding.FragmentCameraBinding
import com.capstone.flofie.ml.Flower
import com.capstone.flofie.model.Resultbox
import com.capstone.flofie.ui.detail.DetailActivity
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.File
import java.nio.ByteOrder

import java.nio.ByteBuffer

class CameraFragment : Fragment() {

    private lateinit var cameraViewModel : CameraViewModel
    private var _binding : FragmentCameraBinding? = null

    private val binding get() = _binding!!

    companion object {
        const val CAMERA_RESULT = 200
        const val GALERY_RESULT = 100

        private val REQUIRED_PERMISSION = arrayOf(android.Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSION = 10
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (!allPermissionGranted()) {
                Toast.makeText(activity, "Ijin tidak diberikan", Toast.LENGTH_SHORT).show()
                activity?.finish()
            }
        }
    }

    private fun allPermissionGranted() = REQUIRED_PERMISSION.all {
        activity?.baseContext?.let { it1 -> ContextCompat.checkSelfPermission(it1, it) } == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        setViewModel()

        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        val root = binding.root
        return root
    }

    private fun setViewModel() {
        cameraViewModel = ViewModelProvider(
            this,
            ViewModelFactory()
        )[CameraViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(!allPermissionGranted()) {
            ActivityCompat.requestPermissions(activity as Activity, REQUIRED_PERMISSION, REQUEST_CODE_PERMISSION)
        }

        setupButton()

        if (cameraViewModel.getFile != null) {
            binding.cameraFragmentCheckBtn.isEnabled = true
            existStat()
        } else {
            binding.cameraFragmentCheckBtn.isEnabled = false
            emptyStat()
        }
    }

    private fun setupButton() {
        binding.cameraFragmentCamera.setOnClickListener {
            startCamera()
        }

        binding.cameraFragmentCheckBtn.setOnClickListener {
            if (cameraViewModel.getFile != null) {
                if (binding.cameraFragmentPreviewContainer.drawable != null) {
                    showLoading(true)
                    Handler().postDelayed({
                        showLoading(false)
                        val bitmap = decodeFileToBitmap(cameraViewModel.getFile)
                        getOutput(bitmap)
//                    val result = Resultbox(
//                        "https://cdn.pixabay.com/photo/2013/07/21/13/00/rose-165819__340.jpg",
//                        "Tulip")
//                    cameraViewModel.result = result
//                    setResultBox(result)
                    }, 2000)
                }
            }
        }

        binding.cameraFragmentResultBox.setOnClickListener {
            startActivity(Intent(activity, DetailActivity::class.java))
        }

        binding.cameraFragmentClearBtn.setOnClickListener {
            if (cameraViewModel.getFile != null) {
                emptyStat()
            }
        }
    }

    private fun getOutput(bitmap : Bitmap) {

        val fileName = "label.txt"
        val inputSting = activity?.application?.assets?.open(fileName)?.bufferedReader().use {
            it?.readText()
        }
        val label = inputSting?.split("\n")

        try {
            val resized = Bitmap.createScaledBitmap(bitmap, 240, 240, true)
            val model = Flower.newInstance(requireContext())

            // Creates inputs for reference.
            val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 240, 240, 3), DataType.FLOAT32)

            val image = TensorImage.fromBitmap(resized).apply {
                DataType.FLOAT32
            }
            image.load(resized)
            val byteBuffer = image.buffer

//            val tfBuffer = TensorImage.fromBitmap(resized)
//            val byteBuffer = tfBuffer.buffer

            inputFeature0.loadBuffer(byteBuffer)

            // Runs model inference and gets result.
            val outputs = model.process(inputFeature0)
            val outputFeature0 = outputs.outputFeature0AsTensorBuffer

            val max = getMax(outputFeature0.floatArray)

            binding.textResult.setText(label?.get(max))
        } catch (e : Exception) {
            Toast.makeText(activity, e.message, Toast.LENGTH_LONG).show()
            Log.d("CEK_BUFFER", e.message.toString())
        }

        // Releases model resources if no longer used.
//        model.close()
    }

    private fun getMax(arr : FloatArray) : Int {
        var mid = 0
        var min = 0.0f

        for (i in 0..4) {
            if (arr[i] > min) {
                mid = i
                min = arr[i]
            }
        }
        return mid
    }

    private fun startCamera() {
        val cameraIntent = Intent(activity, MainCameraActivity::class.java)
        launcherIntentCamera.launch(cameraIntent)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_RESULT) {

            val myFile = it.data?.getSerializableExtra("picture") as? File
            cameraViewModel.getFile = myFile

            existStat()

            val result = decodeFileToBitmap(myFile)
            binding.cameraFragmentPreviewContainer.setImageBitmap(result)
        }
        if (it.resultCode == GALERY_RESULT) {

            val myUriFile = it.data?.getSerializableExtra("imageGaleryFile") as? File

            cameraViewModel.getFile = myUriFile

            existStat()

            val result = decodeFileToBitmap(myUriFile)

            binding.cameraFragmentPreviewContainer.setImageBitmap(result)
        }
    }

    private fun showLoading(isLoading : Boolean) {
        if (isLoading) {
            binding.cameraFragmentProgressBar.visibility = View.VISIBLE
        }
        else {
            binding.cameraFragmentProgressBar.visibility = View.GONE
        }
    }

    private fun emptyStat() {
        binding.cameraFragmentResultBox.visibility = View.GONE
        binding.cameraFragmentPreviewContainer.setImageBitmap(null)
        binding.cameraFragmentCameraGuide.visibility = View.VISIBLE
        binding.cameraFragmentCameraGuideCheck.visibility = View.VISIBLE

        cameraViewModel.getFile = null
        cameraViewModel.result = null
        binding.cameraFragmentCheckBtn.isEnabled = false
    }

    private fun existStat() {
        binding.cameraFragmentCheckBtn.isEnabled = true
        binding.cameraFragmentPreviewContainer.setImageBitmap(decodeFileToBitmap(cameraViewModel.getFile))
        binding.cameraFragmentCameraGuide.visibility = View.GONE
        binding.cameraFragmentCameraGuideCheck.visibility = View.GONE
        if (cameraViewModel.result != null) {
            binding.cameraFragmentResultBox.visibility = View.VISIBLE
            setResultBox(cameraViewModel.result!!)
        }
    }

    private fun decodeFileToBitmap(file : File?) : Bitmap {
        return BitmapFactory.decodeFile(file?.path)
    }

    private fun setResultBox(result : Resultbox) {
        binding.cameraFragmentResultBox.visibility = View.VISIBLE
        Glide.with(activity!!).load(result.image).into(binding.cameraFragmentResultBoxImage)
        binding.cameraFragmentResultBoxFlowerName.text = result.flowerName
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}