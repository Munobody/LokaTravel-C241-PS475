package com.example.lokatravel.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.lokatravel.R
import com.example.lokatravel.databinding.FragmentProfileBinding
import com.example.lokatravel.ui.login.LoginActivity

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

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

        // Ambil nama dan email dari SharedPreferences
        val sharedPreferences = requireActivity().getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE
        )
        val name = sharedPreferences.getString(getString(R.string.saved_name_key), "")
        val email = sharedPreferences.getString(getString(R.string.saved_email_key), "")

        // Set nama dan email ke TextView
        binding.tvName.text = name
        binding.tvEmail.text = email

        // Listener untuk tombol logout
        binding.tvLogout.setOnClickListener {
            // Panggil fungsi logout
            logout()
        }

        // Listener untuk tombol melaporkan bug
        binding.tvReport.setOnClickListener {
            // Implementasi logika untuk melaporkan bug
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun logout() {
        val sharedPreferences = requireActivity().getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE
        )
        // Hapus semua data login dari SharedPreferences
        with(sharedPreferences.edit()) {
            remove(getString(R.string.saved_name_key))
            remove(getString(R.string.saved_email_key))
            remove(getString(R.string.saved_login_status_key))
            apply()
        }

        // Redirect ke halaman login
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        requireActivity().finish()
    }
}
