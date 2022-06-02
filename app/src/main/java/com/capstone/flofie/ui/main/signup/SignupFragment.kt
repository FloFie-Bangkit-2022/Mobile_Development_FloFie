package com.capstone.flofie.ui.main.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.transition.TransitionInflater
import com.capstone.flofie.MainHostActivity
import com.capstone.flofie.R
import com.capstone.flofie.ViewModelFactory
import com.capstone.flofie.databinding.FragmentSignupBinding
import com.capstone.flofie.ui.account.profile.ProfileSettingsViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

class SignupFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    private var _binding : FragmentSignupBinding? = null
    private val binding get() = _binding!!

    private lateinit var signupViewModel: SignupViewModel

    private lateinit var mFirebaseAuth : FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        val root = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fragmentSignupMainScroll.isVerticalScrollBarEnabled = false
        playAnimation()

        mFirebaseAuth = FirebaseAuth.getInstance()

        setupButton()
    }

    private fun setupViewModel() {
        signupViewModel = ViewModelProvider(
            this,
            ViewModelFactory()
        ) [SignupViewModel::class.java]
    }

    private fun setupButton() {
        binding.fragmentSignupBackButton.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.fragmentSignupButton.setOnClickListener {

            try {

                showLoading(true)
                setLoadingState(true)
                activeOrInactiveButton(false)
                setActive(false)

                mFirebaseAuth.createUserWithEmailAndPassword(
                    binding.fragmentSignupInputTextEmail.text.toString(),
                    binding.fragmentSignupInputTextPassword.text.toString()
                )
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            showLoading(false)
                            activeOrInactiveButton(true)
                            setLoadingState(false)
                            setActive(true)
                            Toast.makeText(activity, "Akun berhasil dibuat, login untuk lanjut", Toast.LENGTH_SHORT).show()
                            activity?.onBackPressed()
                        } else {
                            showLoading(false)
                            activeOrInactiveButton(true)
                            setLoadingState(false)
                            setActive(true)
                            Toast.makeText(activity, "Gagal membuat akun", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener {
                        showLoading(false)
                        activeOrInactiveButton(true)
                        setLoadingState(false)
                        setActive(true)
                        Toast.makeText(activity, "Gagal karena " + it.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                    .addOnCanceledListener {
                        showLoading(false)
                        activeOrInactiveButton(true)
                        setLoadingState(false)
                        setActive(true)
                        Toast.makeText(activity, "Canceled", Toast.LENGTH_SHORT).show()
                    }

            } catch (e : Exception) {

                showLoading(false)
                activeOrInactiveButton(true)
                setLoadingState(false)
                setActive(true)

                Toast.makeText(activity, "Error : " + e.message, Toast.LENGTH_SHORT).show()
            }
        }

        binding.fragmentSignupInputTextDate.setOnClickListener {
            showDatePickerDialog()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewModel()

        sharedElementEnterTransition = TransitionInflater.from(context!!).inflateTransition(android.R.transition.move)

        signupViewModel.date.observe(this, {
            binding.fragmentSignupInputTextDate.setText(it)
        })

        signupViewModel.isLoading.observe(activity!!, {
            showLoading(it)
        })
        signupViewModel.isActive.observe(activity!!, {
            activeOrInactiveButton(it)
        })
    }

    private fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(
            activity as Context,
            this,
            Calendar.getInstance()[Calendar.YEAR],
            Calendar.getInstance()[Calendar.MONTH],
            Calendar.getInstance()[Calendar.DAY_OF_MONTH]
        )
        datePickerDialog.show()
    }

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        signupViewModel.myCalendar.set(Calendar.YEAR, p1)
        signupViewModel.myCalendar.set(Calendar.MONTH, p2)
        signupViewModel.myCalendar.set(Calendar.DAY_OF_MONTH, p3)
        val myFormat = "dd/MMM/yyyy"
        val dateFormat = SimpleDateFormat(myFormat, Locale.US)

        binding.fragmentSignupInputTextDate.setText(dateFormat.format(signupViewModel.myCalendar.time))
        signupViewModel._date.value = binding.fragmentSignupInputTextDate.text.toString()
    }

    private fun playAnimation() {
        val name = ObjectAnimator.ofFloat(binding.fragmentSignupNameContainer, View.ALPHA, 1F).setDuration(200)
        val username = ObjectAnimator.ofFloat(binding.fragmentSignupUsernameContainer, View.ALPHA, 1F).setDuration(200)
        val email = ObjectAnimator.ofFloat(binding.fragmentSignupEmailContainer, View.ALPHA, 1F).setDuration(200)
        val password = ObjectAnimator.ofFloat(binding.fragmentSignupPasswordContainer, View.ALPHA, 1F).setDuration(200)
        val date = ObjectAnimator.ofFloat(binding.fragmentSignupDateContainer, View.ALPHA, 1F).setDuration(200)

        AnimatorSet().apply {
            playSequentially(name, username, email, password, date)
            start()
        }
    }

    private fun showLoading(isLoading : Boolean) {
        if (isLoading){
            binding.fragmentSignupLoading.visibility = View.VISIBLE
//            setLoadingState(isLoading)
        }
        else {
            binding.fragmentSignupLoading.visibility = View.GONE
//            setLoadingState(isLoading)
        }
    }

    private fun setLoadingState(isLoading: Boolean) {
        signupViewModel._isLoading.value = isLoading
    }

    private fun activeOrInactiveButton(isActive : Boolean) {
        if (isActive) {
            binding.fragmentSignupButton.isEnabled = isActive
//            signupViewModel._isActive.value = isActive
        }
        else {
            binding.fragmentSignupButton.isEnabled = isActive
//            signupViewModel._isActive.value = isActive
        }
    }

    private fun setActive(isActive: Boolean) {
        signupViewModel._isActive.value = isActive
    }
}