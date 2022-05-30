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
import androidx.transition.TransitionInflater
import com.capstone.flofie.MainHostActivity
import com.capstone.flofie.databinding.FragmentSignupBinding
import java.text.SimpleDateFormat
import java.util.*

class SignupFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    private var _binding : FragmentSignupBinding? = null
    private val binding get() = _binding!!

    val myCalendar: Calendar = Calendar.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        val root = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fragmentSignupMainScroll.isVerticalScrollBarEnabled = false
        playAnimation()

        binding.fragmentSignupBackButton.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.fragmentSignupButton.setOnClickListener {
//            startActivity(Intent(activity, MainHostActivity::class.java))
        }

        binding.fragmentSignupInputTextDate.setOnClickListener {
            showDatePickerDialog()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context!!).inflateTransition(android.R.transition.move)
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
        myCalendar.set(Calendar.YEAR, p1)
        myCalendar.set(Calendar.MONTH, p2)
        myCalendar.set(Calendar.DAY_OF_MONTH, p3)
        val myFormat = "dd/MMM/yyyy"
        val dateFormat = SimpleDateFormat(myFormat, Locale.US)
        binding.fragmentSignupInputTextDate.setText(dateFormat.format(myCalendar.time))
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
}