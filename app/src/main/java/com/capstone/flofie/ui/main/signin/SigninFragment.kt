package com.capstone.flofie.ui.main.signin

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.transition.TransitionInflater
import com.capstone.flofie.ViewModelFactory
import com.capstone.flofie.database.loginPreferences.LoginPreferences
import com.capstone.flofie.databinding.FragmentSigninBinding
import com.capstone.flofie.ui.main.MainActivity
import com.google.firebase.auth.FirebaseAuth

class SigninFragment : Fragment() {

    private var _binding : FragmentSigninBinding? = null
    private val binding get() = _binding!!

    private lateinit var signinViewModel: SigninViewModel

    private lateinit var mFirebaseAuth: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSigninBinding.inflate(inflater, container, false)
        val root = binding.root
        return root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setViewModel()

        sharedElementEnterTransition = TransitionInflater.from(context!!).inflateTransition(android.R.transition.move)

        signinViewModel.isLoading.observe(this, {
            showLoading(it)
        })
        signinViewModel.isActive.observe(this, {
            activeOrInactiveButton(it)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playAnimation()

        mFirebaseAuth = FirebaseAuth.getInstance()

        setupButton()
    }

    private fun setupButton() {
        binding.fragmentSigninBackButton.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.fragmentSigninButton.setOnClickListener {

            try {

                showLoading(true)
                setLoadingState(true)
                activeOrInactiveButton(false)
                setActive(false)

                mFirebaseAuth.signInWithEmailAndPassword(
                    binding.fragmentSigninInputTextEmail.text.toString(),
                    binding.fragmentSigninInputTextPassword.text.toString()
                )
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            showLoading(false)
                            activeOrInactiveButton(true)
                            setLoadingState(false)
                            setActive(true)
                            signinViewModel.saveLoginStatus(true)
                        }
                        else {
                            showLoading(false)
                            activeOrInactiveButton(true)
                            setLoadingState(false)
                            setActive(true)
                            Toast.makeText(activity, "Gagal Login", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener { error ->
                        showLoading(false)
                        activeOrInactiveButton(true)
                        setLoadingState(false)
                        setActive(true)
                        Toast.makeText(activity, "Kesalahan karena : " + error.message, Toast.LENGTH_SHORT).show()
                    }
                    .addOnCanceledListener {
                        showLoading(false)
                        activeOrInactiveButton(true)
                        setLoadingState(false)
                        setActive(true)
                        Toast.makeText(activity, "Login dibatalkan", Toast.LENGTH_SHORT).show()
                    }
            } catch (e : Exception) {
                showLoading(false)
                activeOrInactiveButton(true)
                setLoadingState(false)
                setActive(true)
                Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setViewModel() {

        val dataStore = (activity as MainActivity?)?.getDataStore()
        val preferences = LoginPreferences.getInstance(dataStore!!)

        signinViewModel = ViewModelProvider(
            this,
            ViewModelFactory(preferences)
        ) [SigninViewModel::class.java]
    }

    private fun playAnimation() {
        val email = ObjectAnimator.ofFloat(binding.fragmentSigninEmailContainer, View.ALPHA, 1F).setDuration(200)
        val password = ObjectAnimator.ofFloat(binding.fragmentSigninPaswordContainer, View.ALPHA, 1F).setDuration(200)

        AnimatorSet().apply {
            playSequentially(email, password)
            start()
        }
    }

    private fun showLoading(isLoading : Boolean) {
        if (isLoading) {
            binding.fragmentSigninLoading.visibility = View.VISIBLE
        }
        else {
            binding.fragmentSigninLoading.visibility = View.GONE
        }
    }

    private fun setLoadingState(isLoading: Boolean) {
        signinViewModel._isLoading.value = isLoading
    }

    private fun activeOrInactiveButton(isActive : Boolean) {
        if (isActive) {
            binding.fragmentSigninButton.isEnabled = isActive
        }
        else {
            binding.fragmentSigninButton.isEnabled = isActive
        }
    }

    private fun setActive(isActive: Boolean) {
        signinViewModel._isActive.value = isActive
    }
}