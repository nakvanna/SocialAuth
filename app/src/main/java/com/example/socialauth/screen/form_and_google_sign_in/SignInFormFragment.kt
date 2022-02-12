package com.example.socialauth.screen.form_and_google_sign_in

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.socialauth.HomeActivity
import com.example.socialauth.databinding.FragmentSignInFormBinding
import com.example.socialauth.utils.GET_CLIENT_ID
import com.example.socialauth.utils.RC_SIGN_IN
import com.example.socialauth.utils.makeToast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class SignInFormFragment : Fragment() {
    private lateinit var binding: FragmentSignInFormBinding

    private lateinit var phoneLoginBtn: Button
    private lateinit var googleLoginBtn: Button

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignInFormBinding.inflate(inflater)

        phoneLoginBtn = binding.phoneLoginBtn
        googleLoginBtn = binding.googleLoginBtn
        auth = FirebaseAuth.getInstance()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(GET_CLIENT_ID))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        phoneLoginBtn.setOnClickListener {
            findNavController().navigate(SignInFormFragmentDirections.actionLoginFormFragmentToPhoneLoginFragment())
        }

        googleLoginBtn.setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                val message = "Google sign in failed"
                message.makeToast(requireContext())
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    if (user != null) {
                        //Switch to HomeActivity
                        val intend = Intent(requireContext(), HomeActivity::class.java)
                        intend.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        requireActivity().startActivity(intend)
                        requireActivity().finish()
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    val message = "Sign In With Credential failure"
                    message.makeToast(requireContext())
                }
            }
    }

}