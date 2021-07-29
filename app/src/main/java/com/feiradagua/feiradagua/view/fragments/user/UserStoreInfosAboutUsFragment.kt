package com.feiradagua.feiradagua.view.fragments.user

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.feiradagua.feiradagua.R
import com.feiradagua.feiradagua.databinding.FragmentUserStoreInfosAboutUsBinding
import com.feiradagua.feiradagua.model.`class`.Producer
import com.feiradagua.feiradagua.utils.Constants.Intents.PRODUCER_ID
import com.feiradagua.feiradagua.utils.checkByTag
import com.feiradagua.feiradagua.utils.getProducer
import com.feiradagua.feiradagua.view.activitys.user.UserMenuActivity
import java.net.URLEncoder

class UserStoreInfosAboutUsFragment : Fragment() {
    private lateinit var binding: FragmentUserStoreInfosAboutUsBinding
    private var getId: String? = ""
    private var producer: Producer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserStoreInfosAboutUsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getId = arguments?.getString(PRODUCER_ID)
        producer = UserMenuActivity.PRODUCERS.getProducer(getId)

        binding.floatingButtonWhatsApp.setOnClickListener {
            startWhatsAppChat(producer?.phone, producer?.name)
        }

        setUpProducerInfos()
    }

    private fun setUpProducerInfos() {
        producer?.let {producer ->
            Glide.with(this).load(producer.photo).placeholder(R.drawable.logo_feira_dagua_remove).into(binding.ivProfile)
            binding.tvNameValue.text = getString(R.string.string_name_title_profile, producer.name)
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
    }

    private fun startWhatsAppChat(phone: String?, name: String?) {
        phone?.let {
            val contact = "+55 $phone"
            val message = "Olá ${name?:"Nome não encontrado"}, sou o cliente ${UserMenuActivity.USER.name} e estou entrando em contato " +
                    "a partir do Feira D'água!!"
            val intent = Intent(Intent.ACTION_VIEW)
            val url = "https://api.whatsapp.com/send?phone=$contact&text=${URLEncoder.encode(message,"UTF-8")}"

            intent.data = Uri.parse(url)
            startActivity(intent)
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