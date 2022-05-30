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


class AccountFragment : Fragment() {

    private lateinit var accountViewModel: AccountViewModel
    private var _binding : FragmentAccountBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        setViewModel()

        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        val root = binding.root
        return root
    }

    private fun setViewModel() {

        val dataStore = (activity as MainHostActivity)?.getDataStore()
        val preferences = LoginPreferences.getInstance(dataStore)

        accountViewModel = ViewModelProvider(
            this,
            ViewModelFactory(preferences)
        ) [AccountViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        Glide.with(activity!!).load(R.drawable.placeholder).circleCrop().into(binding.accountImageProfile)

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

        logoutBuilder.setPositiveButton("Yes") { dialog, id ->
            accountViewModel.saveLoginStatus(false)
            startActivity(Intent(activity, MainActivity::class.java))
            activity?.finish()
        }

        logoutBuilder.setNegativeButton("No") { dialog, id ->
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}