package com.example.lokatravel.ui.register

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.HideReturnsTransformationMethod
import android.text.method.LinkMovementMethod
import android.text.method.PasswordTransformationMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.lokatravel.MainActivity
import com.example.lokatravel.R
import com.example.lokatravel.databinding.ActivityRegisterBinding
import com.example.lokatravel.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        // Observe registerResponse LiveData
        viewModel.registerResponse.observe(this, Observer { response ->
            if (response != null) {
                val message = response.message
                val successMessage = "Registration successful: $message"
                Toast.makeText(this, successMessage, Toast.LENGTH_SHORT).show()

                // Save name and email to SharedPreferences
                saveNameAndEmail(binding.etName.text.toString(), binding.etEmail.text.toString())

                startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
                finish()
            }
        })

        // Observe error LiveData
        viewModel.error.observe(this, Observer { errorMessage ->
            if (errorMessage != null) {
                // Handle error message
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })

        // Setup login text with spannable
        setupLoginText()

        // Setup register button click listener
        binding.btnRegister.setOnClickListener {
            val name = binding.etName.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val confirmPassword = binding.etConfirmPass.text.toString()

            val nameError = if (name.isEmpty()) {
                getString(R.string.ERROR_NAME_EMPTY)
            } else if (name.length > 25) {
                getString(R.string.ERROR_NAME_TOOLONG)
            } else null

            val emailError = if (email.isEmpty()) {
                getString(R.string.ERROR_EMAIL_EMPTY)
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
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

            // Set error messages if fields are empty or invalid
            if (nameError == null && emailError == null && passwordError == null && confirmPasswordError == null) {
                // Register user
                viewModel.registerUser(name, email, password, confirmPassword)
            } else {
                // Set error messages for invalid fields
                binding.apply {
                    etName.error = nameError
                    etEmail.error = emailError
                    etPassword.error = passwordError
                    etConfirmPass.error = confirmPasswordError
                }
            }
        }

        // Setup agreement dialog checkbox listener
        binding.cbLocation.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                showAgreementDialog()
            }
        }

        // Setup show password checkbox listener
        binding.cbShowPassword.setOnCheckedChangeListener { _, isChecked ->
            binding.etPassword.transformationMethod =
                if (isChecked) HideReturnsTransformationMethod.getInstance()
                else PasswordTransformationMethod.getInstance()
        }

        // Setup show confirm password checkbox listener
        binding.cbShowConfirmPassword.setOnCheckedChangeListener { _, isChecked ->
            binding.etConfirmPass.transformationMethod =
                if (isChecked) HideReturnsTransformationMethod.getInstance()
                else PasswordTransformationMethod.getInstance()
        }
    }

    private fun setupLoginText() {
        val tvLogin = binding.tvLogin
        val tvFirstPart = getString(R.string.INFO_LOGIN_TEXT)
        val tvSecondPart = " " + getString(R.string.LOGIN_TEXT)
        val loginSpannable = generateSpannableString(tvFirstPart, tvSecondPart)
        tvLogin.text = loginSpannable
        tvLogin.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun showAgreementDialog() {
        val builder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this)
        val dialogView = inflater.inflate(R.layout.agreement_dialog, null)

        builder.setView(dialogView)
        val alertDialog = builder.create()

        dialogView.findViewById<Button>(R.id.btnAgree).setOnClickListener {
            alertDialog.dismiss()
            Toast.makeText(this, "You agreed to the terms and conditions.", Toast.LENGTH_SHORT).show()
        }

        alertDialog.show()
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

        val blueColor = ContextCompat.getColor(this, R.color.blue)
        spannable.setSpan(object : UnderlineSpan() {
            override fun updateDrawState(ds: TextPaint) {
                ds.isUnderlineText = false
            }
        }, 0, firstPart.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        val startSecondPart = firstPart.length
        val endSecondPart = spannable.length

        spannable.setSpan(clickableSpan2, startSecondPart, endSecondPart, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(ForegroundColorSpan(blueColor), startSecondPart, endSecondPart, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(boldStyleSpan, startSecondPart, endSecondPart, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        return spannable
    }

    private fun saveNameAndEmail(name: String, email: String) {
        val sharedPreferences = getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE
        )
        with(sharedPreferences.edit()) {
            putString(getString(R.string.saved_name_key), name)
            putString(getString(R.string.saved_email_key), email)
            apply()
        }
    }
}
