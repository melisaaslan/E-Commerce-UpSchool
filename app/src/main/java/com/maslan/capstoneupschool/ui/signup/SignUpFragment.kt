package com.maslan.capstoneupschool.ui.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.maslan.capstoneupschool.R
import com.maslan.capstoneupschool.common.gone
import com.maslan.capstoneupschool.common.viewBinding
import com.maslan.capstoneupschool.common.visible
import com.maslan.capstoneupschool.databinding.FragmentSignUpBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private val binding by viewBinding(FragmentSignUpBinding::bind)

    private val viewModel by viewModels<SignUpViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            signupButton.setOnClickListener {
                viewModel.signUp(
                    signupEmail.text.toString(),
                    signupPassword.text.toString()
                )
            }
        }

        observeData()
    }

    private fun observeData() = with(binding) {
        viewModel.signUpState.observe(viewLifecycleOwner) { state ->
            when (state) {
                SignUpState.Loading -> progressBar.visible()

                is SignUpState.GoToHome -> {
                    progressBar.gone()
                    findNavController().navigate(R.id.signupToHome)
                }

                is SignUpState.ShowPopUp -> {
                    progressBar.gone()
                    Snackbar.make(requireView(), state.errorMessage, 1000).show()
                }
            }
        }
    }
}