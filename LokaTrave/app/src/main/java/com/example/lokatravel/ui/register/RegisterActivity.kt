package com.example.lokatravel.ui.register

import com.example.lokatravel.ui.login.LoginActivity
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.lokatravel.R
import com.example.lokatravel.databinding.ActivityRegisterBinding
import com.example.lokatravel.ui.home.HomeActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[RegisterViewModel::class.java]

        val tvLogin = binding.tvLogin
        val tvFirstPart = getString(R.string.INFO_LOGIN_TEXT)
        val tvSecondPart = " " + getString(R.string.LOGIN_TEXT)
        val loginSpannable = generateSpannableString(tvFirstPart, tvSecondPart)
        tvLogin.text = loginSpannable
        tvLogin.movementMethod = LinkMovementMethod.getInstance()

        binding.btnRegister.setOnClickListener {
            val name = binding.etName.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPass.text.toString()
            val confirmPassword = binding.etConfirmPass.text.toString()

            val nameError = if (name.isEmpty()) {
                getString(R.string.ERROR_NAME_EMPTY)
            } else if (name.length > 25) {
                getString(R.string.ERROR_NAME_TOOLONG)
            } else null

            val emailError = if (email.isEmpty()) {
                getString(R.string.ERROR_EMAIL_EMPTY)
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                getString(R.string.ERROR_EMAIL_INVALID_FORMAT)
            } else null

            val passwordError = if (password.isEmpty()) {
                getString(R.string.ERROR_PASSWORD_EMPTY)
            } else if (password.length < 8) {
                getString(R.string.ERROR_PASSWORD_LENGTH)
            } else null

            val confirmPasswordError = if (confirmPassword.isEmpty()) {
                getString(R.string.ERROR_PASSWORD_EMPTY)
            } else if (password != confirmPassword) {
                getString(R.string.ERROR_PASSWORD_MISMATCH)
            } else null

            if (nameError == null && emailError == null && passwordError == null && confirmPasswordError == null) {
                // Register user
                viewModel.registerUser(name, email, password, confirmPassword)
            }

            binding.apply {
                etName.error = nameError
                etEmail.error = emailError
                etPass.error = passwordError
                etConfirmPass.error = confirmPasswordError
            }
        }

        viewModel.registerResponse.observe(this) { response ->
            if (response != null) {
                // Handle successful registration here
                startActivity(Intent(this@RegisterActivity, HomeActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                })
            }
        }
    }

    private fun generateSpannableString(firstPart: String, secondPart: String): Spannable {
        val spannable = SpannableString(firstPart + secondPart)
        val boldStyleSpan = StyleSpan(Typeface.BOLD)

        val clickableSpan2 = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.isUnderlineText = false
            }
        }
        val blueColor = ContextCompat.getColor(this, R.color.Navy)
        spannable.setSpan(object : UnderlineSpan() {
            override fun updateDrawState(ds: TextPaint) {
                ds.isUnderlineText = false
            }
        }, 0, firstPart.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        val startSecondPart = firstPart.length
        val endSecondPart = spannable.length
        spannable.setSpan(
            clickableSpan2,
            startSecondPart,
            endSecondPart,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannable.setSpan(
            ForegroundColorSpan(blueColor),
            startSecondPart,
            endSecondPart,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannable.setSpan(
            boldStyleSpan,
            startSecondPart,
            endSecondPart,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        return spannable
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.isVisible = isLoading
    }
}
