package com.feiradagua.feiradagua.view.fragments.user

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.feiradagua.feiradagua.R
import com.feiradagua.feiradagua.databinding.ActivityExtraInfosProducerBinding
import com.feiradagua.feiradagua.databinding.FragmentUserStoreInfosProductsBinding
import java.net.URLEncoder

class UserStoreInfosProductsFragment : Fragment() {
    private lateinit var binding: FragmentUserStoreInfosProductsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserStoreInfosProductsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.floatingButtonWhatsApp.setOnClickListener {
            startWhatsAppChat()
        }
    }

    private fun startWhatsAppChat() {
        val contact = "+55 48996586778"
        val message = "Ola Tubs, um abraço do Feira Dagua!"
        val intent = Intent(Intent.ACTION_VIEW)
        val url = "https://api.whatsapp.com/send?phone=$contact&text=${URLEncoder.encode(message,"UTF-8")}"

        intent.data = Uri.parse(url)
        startActivity(intent)
    }
}