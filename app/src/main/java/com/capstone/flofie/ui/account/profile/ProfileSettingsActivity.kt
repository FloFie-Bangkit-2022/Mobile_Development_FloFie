package com.capstone.flofie.ui.account.profile

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import com.capstone.flofie.databinding.ActivityProfileSettingsBinding
import java.util.*
import android.widget.DatePicker
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.capstone.flofie.R
import com.capstone.flofie.ViewModelFactory
import java.text.SimpleDateFormat
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView

class ProfileSettingsActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    private lateinit var binding : ActivityProfileSettingsBinding
    private lateinit var profileSettingsViewModel: ProfileSettingsViewModel

    companion object {
        const val GALERY_RQUEST_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setupViewModel()
        setupButton()
        setupDropDownMenu()

        profileSettingsViewModel.date.observe(this, {
            binding.profileAccountDateOfBirthInput.text = it
        })
    }

    private fun setupViewModel() {
        profileSettingsViewModel = ViewModelProvider(
            this,
            ViewModelFactory()
        ) [ProfileSettingsViewModel::class.java]
    }

    private fun setupButton() {
        Glide.with(this).load(R.drawable.placeholder).circleCrop().into(binding.profileImageProfile)

        binding.profileAccountName.setOnClickListener {
            val nameInput = binding.profileAccountNameinput
            nameInput.requestFocus()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(nameInput, InputMethodManager.SHOW_IMPLICIT)
        }

        binding.profileAccountUsername.setOnClickListener {
            val usernameInput = binding.profileAccountUsernameInput
            usernameInput.requestFocus()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(usernameInput, InputMethodManager.SHOW_IMPLICIT)
        }

        binding.profileAccountEmail.setOnClickListener {
            val emailInput = binding.profileAccountEmailInput
            emailInput.requestFocus()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(emailInput, InputMethodManager.SHOW_IMPLICIT)
        }

        binding.profileAccountPassword.setOnClickListener {
            val passwordInput = binding.profileAccountPasswordInput
            passwordInput.requestFocus()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(passwordInput, InputMethodManager.SHOW_IMPLICIT)
        }

        binding.profileAccountDateOfBirth.setOnClickListener {
            showDatePickerDialog()
        }

        binding.profileAccountGender.setOnClickListener {
            binding.profileAccountGenderInput.performClick()
        }

        binding.profileBackButton.setOnClickListener {
            onBackPressed()
        }

        binding.profileImageProfile.setOnClickListener {
            startChooseFromGalery()
        }
    }

    private fun startChooseFromGalery() {
        val imagePickerIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        imagePickerIntent.type = "image/*"
        val mimeTypes = arrayOf("image/jpeg", "image/png", "image/jpg")
        imagePickerIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        imagePickerIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivityForResult(imagePickerIntent, GALERY_RQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            GALERY_RQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    data?.data?.let {
                        launchImageCrop(it)
                    }
                }
                else {
                    Toast.makeText(this, "Error: Failed to start crop", Toast.LENGTH_SHORT).show()
                }
            }
            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                val result = CropImage.getActivityResult(data)
                if (resultCode == Activity.RESULT_OK) {
                    result.uri?.let {
                        setImage(it)
                    }
                }
                else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Toast.makeText(this, "Crop error: "+ result.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setImage(uri: Uri) {
        Glide.with(this).load(uri).circleCrop().into(binding.profileImageProfile)
    }

    private fun launchImageCrop(uri : Uri) {
        CropImage.activity(uri)
            .setGuidelines(CropImageView.Guidelines.ON)
            .setAspectRatio(1080, 1080)
            .setCropShape(CropImageView.CropShape.RECTANGLE)
            .start(this)
    }

    private fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(
            this,
            this,
            Calendar.getInstance()[Calendar.YEAR],
            Calendar.getInstance()[Calendar.MONTH],
            Calendar.getInstance()[Calendar.DAY_OF_MONTH]
        )
        datePickerDialog.show()
    }

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        profileSettingsViewModel.myCalendar.set(Calendar.YEAR, p1)
        profileSettingsViewModel.myCalendar.set(Calendar.MONTH, p2)
        profileSettingsViewModel.myCalendar.set(Calendar.DAY_OF_MONTH, p3)

        val myFormat = "dd/MM/yy"
        val dateFormat = SimpleDateFormat(myFormat, Locale.US)

        binding.profileAccountDateOfBirthInput.setText(dateFormat.format(profileSettingsViewModel.myCalendar.time))
        profileSettingsViewModel._date.value = binding.profileAccountDateOfBirthInput.text.toString()
    }

    private fun setupDropDownMenu() {
        val gender = resources.getStringArray(R.array.genders)
        val generAdapter = ArrayAdapter(this@ProfileSettingsActivity, R.layout.profile_gender_layout, gender)
        binding.profileAccountGenderInput.setAdapter(generAdapter)
    }
}