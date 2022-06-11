package com.capstone.flofie.ui.account

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.capstone.flofie.R
import com.capstone.flofie.ViewModelFactory
import com.capstone.flofie.databinding.FragmentAccountBinding
import com.capstone.flofie.ui.account.profile.ProfileSettingsActivity
import androidx.appcompat.app.AlertDialog
import com.capstone.flofie.MainHostActivity
import com.capstone.flofie.database.loginPreferences.LoginPreferences
import com.capstone.flofie.ui.main.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class AccountFragment : Fragment() {

    private lateinit var accountViewModel: AccountViewModel
    private var _binding : FragmentAccountBinding? = null

    private val binding get() = _binding!!

    private lateinit var mFirebaseAuth: FirebaseAuth
    private val mFirebaseUser = FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        setViewModel()

        accountViewModel.userProfile.observe(this, {
            binding.accountNameProfile.setText(it)
        })

        readUserProfile()

        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        val root = binding.root
        return root
    }

    private fun setViewModel() {

        val dataStore = (activity as MainHostActivity).getDataStore()
        val preferences = LoginPreferences.getInstance(dataStore)

        accountViewModel = ViewModelProvider(
            this,
            ViewModelFactory(preferences, mFirebaseUser)
        ) [AccountViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Glide.with(activity!!).load(R.drawable.placeholder).circleCrop().into(binding.accountImageProfile)

        mFirebaseAuth = FirebaseAuth.getInstance()

        setupButton()
    }

    private fun setupButton() {

        binding.accountMenu1.setOnClickListener {
            moveWithAnimation()
        }

        binding.accountLogout.setOnClickListener {
            showAlertDialog()
        }
    }

    private fun showAlertDialog() {
        val logoutBuilder = AlertDialog.Builder(context!!, R.style.LogoutDialogBackground)
        logoutBuilder.setMessage("Yakin ingin keluar?")
        logoutBuilder.setCancelable(true)

        logoutBuilder.setPositiveButton("Yes") { _, _ ->

            mFirebaseAuth.signOut()

            accountViewModel.saveLoginStatus(false)
            startActivity(Intent(activity, MainActivity::class.java))
            activity?.finish()
        }

        logoutBuilder.setNegativeButton("No") { dialog, _ ->
            dialog.cancel()
        }

        val logoutAlret : AlertDialog = logoutBuilder.create()
        logoutAlret.show()
    }

    private fun moveWithAnimation() {
        val optionCompact : ActivityOptionsCompat =
            ActivityOptionsCompat.makeSceneTransitionAnimation(
                requireActivity(),
                Pair(binding.accountTopContainer, "topContainerTransaction"),
                Pair(binding.accountImageProfile, "imageProfileTransaction")
            )
        val profileSettingsIntent = Intent(activity, ProfileSettingsActivity::class.java)
        startActivity(profileSettingsIntent, optionCompact.toBundle())
    }

    private fun readUserProfile() {
        val database = FirebaseDatabase.getInstance().getReference("User Profile").child(mFirebaseUser!!).get()
        database.addOnSuccessListener {
            if (it != null) {
                val name = it.child("accountName").value

                accountViewModel._userProfile.value = name.toString()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}