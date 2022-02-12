package com.example.socialauth.screen.welcome

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.socialauth.LoginActivity
import com.example.socialauth.R
import com.example.socialauth.databinding.FragmentWelcomeBinding
import com.google.firebase.auth.FirebaseAuth

class WelcomeFragment : Fragment() {
    private lateinit var binding: FragmentWelcomeBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentWelcomeBinding.inflate(inflater)
        auth = FirebaseAuth.getInstance()

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.overflow_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logoutMenuItem) {
            logout()
        }
        val navigateToOrNot = NavigationUI.onNavDestinationSelected(item, this.findNavController())
        return navigateToOrNot || super.onOptionsItemSelected(item)
    }

    private fun logout() {
        auth.signOut()
        val intend = Intent(requireContext(), LoginActivity::class.java)
        requireActivity().startActivity(intend)
        requireActivity().finish()
    }

}