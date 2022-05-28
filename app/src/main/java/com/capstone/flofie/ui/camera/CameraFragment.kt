package com.capstone.flofie.ui.camera

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
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
import com.capstone.flofie.ViewModelFactory
import com.capstone.flofie.databinding.FragmentCameraBinding
import com.capstone.flofie.ui.detail.DetailActivity
import java.io.File

class CameraFragment : Fragment() {

    private lateinit var cameraViewModel : CameraViewModel
    private var _binding : FragmentCameraBinding? = null

    private val binding get() = _binding!!

    private var getFile : File? = null

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
    }

    private fun setupButton() {
        binding.cameraFragmentCamera.setOnClickListener {
            startCamera()
        }

        binding.cameraFragmentCheckBtn.setOnClickListener {
            if (getFile != null) {
                showLoading(true)
                Handler().postDelayed({
                    showLoading(false)
                    binding.cameraFragmentResultBox.visibility = View.VISIBLE
                }, 2000)
            }
        }

        binding.cameraFragmentResultBox.setOnClickListener {
            startActivity(Intent(activity, DetailActivity::class.java))
        }

        binding.cameraFragmentClearBtn.setOnClickListener {
            if (getFile != null) {
                getFile = null
                binding.cameraFragmentResultBox.visibility = View.GONE
                binding.cameraFragmentPreviewContainer.setImageBitmap(null)
                binding.cameraFragmentCameraGuide.visibility = View.VISIBLE
                binding.cameraFragmentCameraGuideCheck.visibility = View.VISIBLE
                binding.cameraFragmentCheckBtn.isEnabled = false
            }
        }
    }

    private fun startCamera() {
        val cameraIntent = Intent(activity, MainCameraActivity::class.java)
        launcherIntentCamera.launch(cameraIntent)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_RESULT) {

            binding.cameraFragmentResultBox.visibility = View.GONE
            binding.cameraFragmentCameraGuide.visibility = View.GONE
            binding.cameraFragmentCameraGuideCheck.visibility = View.GONE

            val myFile = it.data?.getSerializableExtra("picture") as? File
            Log.d("CEK_FILE", myFile.toString())
            getFile = myFile
            val result = BitmapFactory.decodeFile(myFile?.path)
            binding.cameraFragmentPreviewContainer.setImageBitmap(result)
        }
        if (it.resultCode == GALERY_RESULT) {

            binding.cameraFragmentResultBox.visibility = View.GONE
            binding.cameraFragmentCameraGuide.visibility = View.GONE
            binding.cameraFragmentCameraGuideCheck.visibility = View.GONE

            Log.d("CEK_FILE_RESULTCODE", "From galery trigered, result : "+ it.resultCode)
            val myUriFile = it.data?.getSerializableExtra("imageGaleryFile") as? File

            Log.d("CEK_FILE_RESULT", myUriFile.toString())

            getFile = myUriFile
            val result = BitmapFactory.decodeFile(myUriFile?.path)

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

    override fun onResume() {
        super.onResume()
        if (getFile != null) {
            binding.cameraFragmentCheckBtn.isEnabled = true
        } else {
            binding.cameraFragmentCheckBtn.isEnabled = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}