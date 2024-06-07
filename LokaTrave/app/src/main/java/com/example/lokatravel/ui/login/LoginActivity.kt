package com.example.lokatravel.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.lokatravel.R
import com.example.lokatravel.databinding.ActivityLoginBinding
import com.example.lokatravel.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        val tvRegister = binding.tvRegister
        val tvFirstPart = getString(R.string.INFO_REGISTER_TEXT)
        val tvSecondPart = " " + getString(R.string.REGISTER_TEXT)
        val registerSpannable = generateSpannableString(tvFirstPart, tvSecondPart)
        tvRegister.text = registerSpannable
        tvRegister.movementMethod = LinkMovementMethod.getInstance()

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPass.text.toString()
            viewModel.login(email, password)
        }

        viewModel.loginResponse.observe(this) { response ->
            if (response != null) {
                val message = response.message
                if (message != null) {
                    val welcomeMessage = "Welcome $message, To LokaTravel"
                    Toast.makeText(this, welcomeMessage, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "An error occurred", Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        viewModel.error.observe(this) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun generateSpannableString(firstPart: String, secondPart: String): Spannable {
        val spannable = SpannableString(firstPart + secondPart)

        val clickableSpan2 = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
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
        return spannable
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.isVisible = isLoading
    }
}
