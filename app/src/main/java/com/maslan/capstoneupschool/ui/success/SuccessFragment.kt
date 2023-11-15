package com.maslan.capstoneupschool.ui.success


import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.maslan.capstoneupschool.R
import com.maslan.capstoneupschool.common.viewBinding
import com.maslan.capstoneupschool.databinding.FragmentSuccessBinding


class SuccessFragment : Fragment(R.layout.fragment_success) {
    private val binding by viewBinding(FragmentSuccessBinding::bind)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            btnSuccess.setOnClickListener {
                findNavController().navigate(SuccessFragmentDirections.successToHome())
            }
        }
    }

}

