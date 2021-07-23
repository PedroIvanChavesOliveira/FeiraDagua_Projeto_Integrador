package com.feiradagua.feiradagua.view.fragments.producer

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.feiradagua.feiradagua.databinding.FragmentProducerProfileBinding
import com.feiradagua.feiradagua.utils.checkByTag
import com.feiradagua.feiradagua.view.activitys.both.LoginActivity
import com.feiradagua.feiradagua.view.activitys.producer.ProducerMenuActivity
import com.feiradagua.feiradagua.view.activitys.producer.ProducerUpdateProfileActivity
import com.feiradagua.feiradagua.viewModel.ProducerProfileViewModel

class ProducerProfileFragment : Fragment() {
    private lateinit var binding: FragmentProducerProfileBinding
    private var viewModelProducerProfile = ProducerProfileViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProducerProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModelProducerProfile = ViewModelProvider(this).get(ProducerProfileViewModel::class.java)

        setProducerInfos()

        binding.btLogOutProfile.setOnClickListener {
            viewModelProducerProfile.signOut()
            startActivity(Intent(activity, LoginActivity::class.java))
        }

//        binding.btEditProducts.setOnClickListener {
//            startActivity(Intent(activity, ProducerNewProductActivity::class.java))
//        }

        binding.btEditProfile.setOnClickListener {
            startActivity(Intent(activity, ProducerUpdateProfileActivity::class.java))
        }
    }

    private fun setProducerInfos() {
        val producer = ProducerMenuActivity.PRODUCER

        Glide.with(this).load(producer.photo).into(binding.ivProfile)
        binding.tvContactValue.text = producer.phone
        binding.tvAdressValue.text = producer.adress
        binding.tvPresentationValue.text = producer.description
        producer.deliveryLocation.forEach { loc ->
            getChipTagDeliveryLocation(loc)
        }
        producer.deliveryDate.forEach { loc ->
            getChipTagDeliveryDate(loc)
        }
        producer.payment.forEach { loc ->
            getChipTagPayment(loc)
        }
    }

    private fun getChipTagDeliveryLocation(tag: String) {
        binding.chipFlorianopolisCenter.checkByTag(tag)
        binding.chipFlorianopolisEast.checkByTag(tag)
        binding.chipFlorianopolisNorth.checkByTag(tag)
        binding.chipFlorianopolisSouth.checkByTag(tag)
        binding.chipBiguacu.checkByTag(tag)
        binding.chipPalhoca.checkByTag(tag)
        binding.chipGaropaba.checkByTag(tag)
        binding.chipImbituba.checkByTag(tag)
    }

    private fun getChipTagDeliveryDate(tag: String) {
        binding.chipSunday.checkByTag(tag)
        binding.chipMonday.checkByTag(tag)
        binding.chipTuesday.checkByTag(tag)
        binding.chipWednesday.checkByTag(tag)
        binding.chipThursday.checkByTag(tag)
        binding.chipFriday.checkByTag(tag)
        binding.chipSaturday.checkByTag(tag)
    }

    private fun getChipTagPayment(tag: String) {
        binding.chipPix.checkByTag(tag)
        binding.chipBankTransfer.checkByTag(tag)
        binding.chipDebitCard.checkByTag(tag)
        binding.chipCreditCard.checkByTag(tag)
    }
}