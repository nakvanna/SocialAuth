package com.example.socialauth.screen.phone_sign_in

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.example.socialauth.HomeActivity
import com.example.socialauth.R
import com.example.socialauth.databinding.FragmentPhoneSignInBinding
import com.google.android.material.textfield.TextInputLayout
import com.hbb20.CountryCodePicker

class PhoneSignInFragment : Fragment() {

    private lateinit var binding: FragmentPhoneSignInBinding
    private lateinit var viewModel: PhoneSignInViewModel
    private lateinit var ccp: CountryCodePicker
    private lateinit var phoneSignInBtn: Button
    private lateinit var phoneNumberEdit: EditText
    private lateinit var phoneNumberLayout: TextInputLayout
    private lateinit var optEdit: EditText
    private lateinit var outlinedTextField: TextInputLayout

    override
    fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPhoneSignInBinding.inflate(inflater)
        viewModel = ViewModelProvider(this)[PhoneSignInViewModel::class.java]

        ccp = binding.ccp
        phoneSignInBtn = binding.phoneSignInBtn
        phoneNumberLayout = binding.phoneNumberLayout
        phoneNumberEdit = binding.phoneNumberEdit
        optEdit = binding.otpEdit
        outlinedTextField = binding.outlinedTextField

        return binding.root
    }

    private fun submitForm() {
        phoneNumberLayout.helperText = validatePhone()
        val validPhone = phoneNumberLayout.helperText == null
        if (validPhone) {
            resetForm()
        } else {
            invalidForm()
        }
    }

    private fun invalidForm() {
        val cpp = ccp.selectedCountryCodeWithPlus
        val phone = phoneNumberLayout.helperText
        val message = getString(R.string.phone_submit_dialog, cpp, phone)
        if (phoneNumberEdit.text != null) {
            AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.invalid_form))
                .setMessage(message)
                .setPositiveButton(getString(R.string.cancel)) { _, _ ->
                    //Do nothing
                }.show()
        }

    }

    private fun resetForm() {
        val cpp = ccp.selectedCountryCodeWithPlus
        val phone = phoneNumberEdit.text
        val fullPhone = "$cpp $phone"
        val message = getString(R.string.phone_submit_dialog, cpp, phone)
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.submit_form))
            .setMessage(message)
            .setPositiveButton(getString(R.string.ok)) { _, _ ->
                ccp.visibility = View.GONE
                phoneNumberLayout.visibility = View.GONE
                outlinedTextField.visibility = View.VISIBLE
                phoneSignInBtn.text = getString(R.string._continue)
                viewModel.onVerifyPhoneNumber(requireActivity(), fullPhone)
            }.show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.signInSuccess.observe(viewLifecycleOwner, {
            if (it) {
                val intend = Intent(requireContext(), HomeActivity::class.java)
                intend.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                requireActivity().startActivity(intend)
                requireActivity().finish()
            }
        })


        phoneSignInBtn.setOnClickListener {
            if (viewModel.isPhoneInput.value == true) {
                submitForm()
            } else {
                viewModel.onVerifyOtpCode(optEdit.text.toString())
            }
        }

        phoneNumberEdit.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                phoneNumberLayout.helperText = validatePhone()
            }
        }
    }

    private fun validatePhone(): String? {
        val phoneInput = phoneNumberEdit.text
        if (!phoneInput.matches(".*[0-9].*".toRegex())) {
            return getString(R.string.must_be_all_digits)
        }
        if (phoneInput.length !in 9..10) {
            return getString(R.string.must_be_9_or_10_digits)
        }
        return null
    }
}