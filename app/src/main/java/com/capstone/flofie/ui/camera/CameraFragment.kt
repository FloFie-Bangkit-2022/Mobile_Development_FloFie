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
import androidx.camera.core.impl.utils.ContextUtil.getApplicationContext
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
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.File
import java.nio.ByteOrder

import java.nio.ByteBuffer
import java.io.IOException
import java.util.Scanner

import java.util.ArrayList




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
                        showLoading(false)
                        val bitmap = decodeFileToBitmap(cameraViewModel.getFile)
                        getOutput(bitmap)
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
        val model = Flower.newInstance(activity!!)

        try {
            // Creates inputs for reference.
            val image = TensorImage.fromBitmap(bitmap).apply {
                DataType.FLOAT32
            }
            val tensorImage = tfImageProcessor.process(image)
            val tensorBuffer = tensorImage.tensorBuffer

            // Runs model inference and gets result.
            val outputs = model.process(tensorBuffer)
            val outputFeature0 = outputs.outputFeature0AsTensorBuffer

            val confidences : FloatArray = outputFeature0.floatArray
            val classes = arrayListOf<String>("Dandelion", "Daisy", "Tulip", "Sunflower", "Rose")
//            val classes = ArrayList<String>()
//            val scan = Scanner("label.txt")
//            while (scan.hasNextLine()) {
//                classes.add(scan.nextLine())
//            }
//            scan.close()

            var maxPos = 0
            var maxConfidences : Float = 0F
            for (x in 0..confidences.size-1) {
                Log.d("CEK_CONFIDENCES1", confidences[x].toString())
                if (confidences[x] > maxConfidences) {
                    maxConfidences = confidences[x]
                    maxPos = x
                }
            }

            setResult(classes[maxPos], bitmap)

//            val sorted = bubbleShort(confidences)
//
//            setResult(sorted[sorted.size-1], bitmap)

        } catch (e : Exception) {
            Toast.makeText(activity, e.message, Toast.LENGTH_LONG).show()
            Log.d("CEK_BUFFER", e.message.toString())
        }

        model.close()
    }

    private val tfImageProcessor by lazy {
        ImageProcessor.Builder()
            .add(ResizeOp(240, 240, ResizeOp.ResizeMethod.BILINEAR))
            .add(NormalizeOp(5F, 1F))
            .build()
    }
//
//    private fun bubbleShort(floatArray: FloatArray) : ArrayList<String> {
//        val classes = arrayListOf<String>("Dandelion", "Daisy", "Tulip", "Sunflower", "Rose")
//        var sort = floatArray
//
//        for (x in 0..floatArray.size-1) {
//            for (i in 0..floatArray.size - 2 - x) {
//                if (sort[i] > sort[i + 1]) {
//                    val temp = sort[i]
//                    sort[i] = sort[i + 1]
//                    sort[i + 1] = temp
//
//                    val resTemp = classes[i]
//                    classes[i] = classes[i + 1]
//                    classes[i + 1] = resTemp
//                }
//            }
//            Log.d("CEK_SORT", sort[x].toString())
//        }
//
//        var sortedWithResult = arrayListOf<String>()
//        for (x in 0..sort.size-1) {
//            sortedWithResult.add(x, classes[x] + " : " + sort[x].toString())
//            Log.d("CEK_SORTED", sortedWithResult[x])
//        }
//
//        return sortedWithResult
//    }

    private fun setResult(flowerResult : String?, flowerBitmap: Bitmap) {

        val flowerImage = when (flowerResult) {
            "Daisy" -> "https://asset-a.grid.id/crop/0x0:0x0/x/photo/2021/11/02/pexels-pixabay-45901jpg-20211102041046.jpg"
            "Dandelion" -> "https://asset-a.grid.id/crop/0x0:0x0/780x800/photo/bobofoto/original/9108_benih-dandelion.JPG"
            "Tulip" -> "https://cdn.pixabay.com/photo/2013/06/01/02/44/tulip-115331_640.jpg"
            "Sunflower" -> "https://cdn.shopify.com/s/files/1/2235/4833/files/How_to_Grow_Sunflowers_2.jpg?v=1557932844"
            "Rose" -> "https://i.pinimg.com/originals/fc/95/88/fc95887d0b1ab9f8d12fc468d1ff861e.jpg"
            else -> null
        }

        val result = Resultbox(
                flowerImage,
                flowerBitmap,
                flowerResult)
            cameraViewModel.result = result
            setResultBox(result)
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
//        binding.cameraFragmentResultBoxImage.setImageBitmap(result.imageBitmap)
        binding.cameraFragmentResultBoxFlowerName.text = result.flowerName
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}