package com.example.lokatravel.ui.login

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.InputType
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.lokatravel.MainActivity
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

        val sharedPreferences = getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE
        )

        // Cek status login saat aplikasi dibuka kembali
        val isLoggedIn = sharedPreferences.getBoolean(getString(R.string.saved_login_status_key), false)
        if (isLoggedIn) {
            navigateToMainActivity()
        }

        binding.tvRegister.setOnClickListener {
            navigateToRegisterActivity()
        }

        val tvRegister = binding.tvRegister
        val tvFirstPart = getString(R.string.INFO_REGISTER_TEXT)
        val tvSecondPart = " " + getString(R.string.REGISTER_TEXT)
        val registerSpannable = generateSpannableString(tvFirstPart, tvSecondPart)
        tvRegister.text = registerSpannable
        tvRegister.movementMethod = LinkMovementMethod.getInstance()

        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text?.toString() ?: ""
            val password = binding.etPassword.text?.toString() ?: ""

            // Tampilkan progress bar
            showLoading(true)

            // Panggil fungsi loginUser di ViewModel
            viewModel.loginUser(email, password)
        }

        // Observasi hasil login
        viewModel.loginResponse.observe(this) { response ->
            // Sembunyikan progress bar
            showLoading(false)

            // Jika login berhasil, tampilkan dialog konfirmasi
            if (response != null) {
                // Simpan status login menggunakan SharedPreferences
                saveLoginStatus(true)
                showSuccessDialog()
            }
        }

        // Observasi error jika terjadi
        viewModel.error.observe(this) { errorMessage ->
            // Sembunyikan progress bar
            showLoading(false)

            if (errorMessage != null) {
                // Tampilkan pesan kesalahan menggunakan dialog
                showErrorDialog(errorMessage)
            }
        }

        // Setup show/hide password functionality
        binding.cbShowPassword.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.etPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                binding.etPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            binding.etPassword.setSelection(binding.etPassword.text?.length ?: 0)
        }
    }

    private fun navigateToMainActivity() {
        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        finish()
    }

    private fun navigateToRegisterActivity() {
        val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun saveLoginStatus(isLoggedIn: Boolean) {
        val sharedPreferences = getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE
        )
        with(sharedPreferences.edit()) {
            putBoolean(getString(R.string.saved_login_status_key), isLoggedIn)
            apply()
        }
    }

    private fun showSuccessDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Login berhasil!")
            .setPositiveButton("OK") { _, _ ->
                navigateToMainActivity()
            }
        builder.create().show()
    }

    private fun showErrorDialog(errorMessage: String) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(" $errorMessage ")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
        builder.create().show()
    }

    private fun generateSpannableString(firstPart: String, secondPart: String): Spannable {
        val spannable = SpannableString(firstPart + secondPart)
        val boldStyleSpan = StyleSpan(Typeface.BOLD)

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
        val blueColor = ContextCompat.getColor(this, R.color.blue)
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
}
