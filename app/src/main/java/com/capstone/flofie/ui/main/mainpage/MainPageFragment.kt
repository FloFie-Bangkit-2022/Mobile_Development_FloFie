package com.capstone.flofie.ui.main.mainpage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.capstone.flofie.R
import com.capstone.flofie.databinding.FragmentMainPageBinding

class MainPageFragment : Fragment() {

    private var _binding : FragmentMainPageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMainPageBinding.inflate(inflater, container, false)
        val root = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mainSigninButton.setOnClickListener {
            moveSiginAnimation()
        }

        binding.mainSignupButton.setOnClickListener {
            moveSigupAnimation()
        }
    }

    private fun moveSiginAnimation() {
        val navController = findNavController()

        val animationExtra = FragmentNavigatorExtras(
            binding.mainSigninButton to "fragmentSigninButtonTransaction",
            binding.fragmentMainPageLogoText to "fragmentLogoTextTransaction"
        )
        navController.navigate(R.id.action_mainPageFragment_to_signinFragment2, null, null, animationExtra)
    }

    private fun moveSigupAnimation() {
        val navController = findNavController()

        val animationExtra = FragmentNavigatorExtras(
            binding.mainSigninButton to "fragmentSignupButtonTransaction",
            binding.fragmentMainPageLogoText to "fragmentLogoTextTransaction"
        )
        navController.navigate(R.id.action_mainPageFragment_to_signupFragment, null, null, animationExtra)
    }
}