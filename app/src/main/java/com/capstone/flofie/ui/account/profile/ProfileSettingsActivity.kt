package com.capstone.flofie.ui.account.profile

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
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
import com.capstone.flofie.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView

class ProfileSettingsActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    private lateinit var binding : ActivityProfileSettingsBinding
    private lateinit var profileSettingsViewModel: ProfileSettingsViewModel

    private lateinit var mFirebaseDatabase: FirebaseDatabase
    private val mFirebaseUser = FirebaseAuth.getInstance().currentUser?.uid

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
        readUserProfile()

        mFirebaseDatabase = Firebase.database

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

        val email = FirebaseAuth.getInstance().currentUser?.email
        binding.profileAccountEmailInput.setText(email)

        binding.profileAccountName.setOnClickListener {
            val nameInput = binding.profileAccountNameinput
            nameInput.requestFocus()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(nameInput, InputMethodManager.SHOW_IMPLICIT)
        }

        binding.profileAccountEmail.setOnClickListener {
            val emailInput = binding.profileAccountEmailInput
            emailInput.requestFocus()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(emailInput, InputMethodManager.SHOW_IMPLICIT)
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

    private fun saveUserProfile() {
        val userRef = mFirebaseDatabase.reference.child("User Profile")

        try {
            val userSetting = User(
                mFirebaseUser,
                binding.profileAccountNameinput.text.toString(),
                binding.profileAccountUsernameInput.text.toString(),
                binding.profileAccountEmailInput.text.toString(),
                binding.profileAccountDateOfBirthInput.text.toString(),
                binding.profileAccountGenderInput.selectedItem.toString()
            )
            userRef.child(mFirebaseUser!!).setValue(userSetting) { error, _ ->
                if (error != null) {
                    Toast.makeText(this, "Error" + error.message, Toast.LENGTH_SHORT).show()
                }
                else {
                    Toast.makeText(this, "Profile berhasil diupdate", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Kesalahan" + e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUserProfile() {
        val userRef = mFirebaseDatabase.getReference("User Profile")

        try {
            val userSetting = mapOf<String, String?>(
                "uuid" to mFirebaseUser,
                "accountName" to binding.profileAccountNameinput.text.toString(),
                "username" to binding.profileAccountUsernameInput.text.toString(),
                "email" to binding.profileAccountEmailInput.text.toString(),
                "date" to binding.profileAccountDateOfBirthInput.text.toString(),
                "gender" to binding.profileAccountGenderInput.selectedItem.toString()
            )
            userRef.child(mFirebaseUser!!).updateChildren(userSetting) { error, _ ->
                if (error != null) {
                    Toast.makeText(this, "Error" + error.message, Toast.LENGTH_SHORT).show()
                }
                else {
                    Toast.makeText(this, "Profile berhasil diupdate", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Kesalahan" + e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun readUserProfile() {
        val database = FirebaseDatabase.getInstance().getReference("User Profile").child(mFirebaseUser!!).get()
        database.addOnSuccessListener {
            if (it != null) {
                val name = it.child("accountName").value
                val username = it.child("username").value
                val date = it.child("date").value

                binding.profileAccountNameinput.setText(name.toString())
                binding.profileAccountUsernameInput.setText(username.toString())
                binding.profileAccountDateOfBirthInput.setText(date.toString())

                binding.profileCheckButton.setOnClickListener {
                    updateUserProfile()
                }
            }
            else if (it == null) {
                binding.profileCheckButton.setOnClickListener {
                    saveUserProfile()
                }
            }
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

        val myFormat = "dd MMMM yyyy"
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