package com.capstone.flofie.ui.main.signin

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.transition.TransitionInflater
import com.capstone.flofie.MainHostActivity
import com.capstone.flofie.databinding.FragmentSigninBinding

class SigninFragment : Fragment() {

    private var _binding : FragmentSigninBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSigninBinding.inflate(inflater, container, false)
        val root = binding.root
        return root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context!!).inflateTransition(android.R.transition.move)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playAnimation()

        binding.fragmentSigninBackButton.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.fragmentSigninButton.setOnClickListener {
            startActivity(Intent(activity, MainHostActivity::class.java))
        }
    }

    private fun playAnimation() {
        val email = ObjectAnimator.ofFloat(binding.fragmentSigninEmailContainer, View.ALPHA, 1F).setDuration(200)
        val password = ObjectAnimator.ofFloat(binding.fragmentSigninPaswordContainer, View.ALPHA, 1F).setDuration(200)

        AnimatorSet().apply {
            playSequentially(email, password)
            start()
        }
    }
}