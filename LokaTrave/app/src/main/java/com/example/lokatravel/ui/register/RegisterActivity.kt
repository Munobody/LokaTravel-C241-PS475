package com.example.lokatravel.ui.register

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.lokatravel.MainActivity
import com.example.lokatravel.R
import com.example.lokatravel.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

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

            // Hanya menetapkan pesan kesalahan jika bidang kosong
            if (nameError == null && emailError == null && passwordError == null && confirmPasswordError == null) {
                // Register user
                viewModel.registerUser(name, email, password, confirmPassword)
            } else {
                // Menetapkan pesan kesalahan untuk bidang yang tidak valid
                binding.apply {
                    etName.error = nameError
                    etEmail.error = emailError
                    etPassword.error = passwordError
                    etConfirmPass.error = confirmPasswordError
                }
            }
        }

        // Observers untuk view model
        viewModel.registerResponse.observe(this) { response ->
            if (response != null) {
                val message = response.message
                val successMessage = "Registration successful: $message"
                Toast.makeText(this, successMessage, Toast.LENGTH_SHORT).show()

                // Simpan nama dan email ke SharedPreferences
                saveNameAndEmail(binding.etName.text.toString(), binding.etEmail.text.toString())

                startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
                finish()
            }
        }

        viewModel.error.observe(this) { errorMessage ->
            if (errorMessage != null) {
                // Handle error message
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        // Listener untuk checkbox persetujuan
        binding.cbLocation.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                showAgreementDialog()
            }
        }

        // Listener untuk checkbox show password
        binding.cbShowPassword.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                binding.etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }

        // Listener untuk checkbox show confirm password
        binding.cbShowConfirmPassword.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.etConfirmPass.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                binding.etConfirmPass.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }
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
