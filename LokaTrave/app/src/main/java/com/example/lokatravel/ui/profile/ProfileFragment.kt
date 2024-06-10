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
        binding.tvName.text = name
        binding.tvEmail.text = email

        // Tombol log out
        binding.tvLogout.setOnClickListener {
            // Hapus status login dari SharedPreferences
            logout()

            // Redirect ke halaman login
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
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
        with(sharedPreferences.edit()) {
            remove(getString(R.string.saved_login_status_key))
            remove(getString(R.string.saved_name_key))
            remove(getString(R.string.saved_email_key))
            apply()
        }
    }
}
