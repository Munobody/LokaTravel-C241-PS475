package com.example.lokatravel.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.lokatravel.R
import com.example.lokatravel.databinding.FragmentProfileBinding
import com.example.lokatravel.ui.login.LoginActivity

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var etName: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etName = binding.root.findViewById(R.id.et_name)

        // Ambil nama dan email dari SharedPreferences
        val sharedPreferences = requireActivity().getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE
        )
        val name = sharedPreferences.getString(getString(R.string.saved_name_key), "")
        val email = sharedPreferences.getString(getString(R.string.saved_email_key), "")

        // Set nama dan email ke EditText dan TextView
        etName.setText(name)
        binding.tvEmail.text = email

        // Listener untuk tombol logout
        binding.tvLogout.setOnClickListener {
            showLogoutConfirmationDialog()
        }

        // Listener untuk tombol melaporkan bug
        binding.tvReport.setOnClickListener {
            // Implementasi logika untuk melaporkan bug
        }

        // Listener untuk mengubah nama
        etName.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                showChangeNameConfirmationDialog(etName.text.toString())
            }
        }

        // Listener untuk tombol mengganti password
        binding.tvChangePassword.setOnClickListener {
            showChangePasswordDialog()
        }

        // Tombol "Simpan Perubahan"
        binding.btnSaveChanges.setOnClickListener {
            val newName = etName.text.toString()
            showChangeNameConfirmationDialog(newName)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLogoutConfirmationDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Apakah anda yakin untuk keluar?")
            .setPositiveButton("Ya") { _, _ ->
                // Panggil fungsi logout
                performLogout()
            }
            .setNegativeButton("Tidak", null)
        builder.create().show()
    }

    private fun performLogout() {
        val sharedPreferences = requireActivity().getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE
        )
        // Hapus semua data login dari SharedPreferences
        with(sharedPreferences.edit()) {
            remove(getString(R.string.saved_login_status_key))
            apply()
        }

        // Redirect ke halaman login
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        requireActivity().finish()
    }

    private fun saveName(newName: String) {
        val sharedPreferences = requireActivity().getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE
        )
        with(sharedPreferences.edit()) {
            putString(getString(R.string.saved_name_key), newName)
            apply()
        }
    }

    private fun getPassword(): String? {
        val sharedPreferences = requireActivity().getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE
        )
        return sharedPreferences.getString(getString(R.string.saved_password_key), null)
    }

    private fun showChangePasswordDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.dialog_change_password, null)
        val etOldPassword = dialogLayout.findViewById<EditText>(R.id.et_old_password)
        val etNewPassword = dialogLayout.findViewById<EditText>(R.id.et_new_password)
        val etConfirmPassword = dialogLayout.findViewById<EditText>(R.id.et_confirm_password)

        builder.setView(dialogLayout)
            .setPositiveButton("Simpan") { _, _ ->
                val oldPassword = etOldPassword.text.toString()
                val newPassword = etNewPassword.text.toString()
                val confirmPassword = etConfirmPassword.text.toString()

                val currentPassword = getPassword()

                if (oldPassword == currentPassword) {
                    if (newPassword == confirmPassword) {
                        savePassword(newPassword)
                        Toast.makeText(requireContext(), "Password berhasil diubah", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "Konfirmasi password tidak cocok", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Password lama tidak cocok", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Batal", null)
        builder.create().show()
    }

    private fun savePassword(newPassword: String) {
        val sharedPreferences = requireActivity().getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE
        )
        with(sharedPreferences.edit()) {
            putString(getString(R.string.saved_password_key), newPassword)
            apply()
        }
    }

    private fun showChangeNameConfirmationDialog(newName: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Apakah anda yakin untuk mengubah nama?")
            .setPositiveButton("Ya") { _, _ ->
                saveName(newName)
                Toast.makeText(requireContext(), "Nama berhasil diubah", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Tidak", null)
        builder.create().show()
    }
}
