package com.maslan.capstoneupschool.ui.payment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.maslan.capstoneupschool.R
import com.maslan.capstoneupschool.common.viewBinding
import com.maslan.capstoneupschool.databinding.FragmentPaymentBinding


class PaymentFragment : Fragment(R.layout.fragment_payment) {
    private val binding by viewBinding(FragmentPaymentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        with(binding) {

            btnPayment.setOnClickListener {
                val address = etAddress.text.toString()
                val name = etNameSurname.text.toString()
                val cardNumber = etCardNumber.text.toString()
                val cardMonth = etCardMonth.text.toString()
                val cardYear=etCardYear.text.toString()
                val cvc = etCvc.text.toString()

                if (cardNumber.length == 16 && cardMonth.isNotEmpty()&& cardYear.isNotEmpty() && cvc.isNotEmpty() && address.isNotEmpty() && name.isNotEmpty()
                ) {
                    findNavController().navigate(PaymentFragmentDirections.paymentToSuccess())
                } else {
                    Snackbar.make(requireView(), "Fill all blanks and ensure card number is 16 characters long", Snackbar.LENGTH_SHORT).show()

                }

            }
            icBackToCart.setOnClickListener {
                findNavController().navigate(PaymentFragmentDirections.paymentToCart())
            }

        }
    }

}