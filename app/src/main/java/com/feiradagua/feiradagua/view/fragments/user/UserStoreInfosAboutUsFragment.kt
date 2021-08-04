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
import com.feiradagua.feiradagua.model.`class`.TutorialData
import com.feiradagua.feiradagua.utils.Constants.Intents.POSITION
import com.feiradagua.feiradagua.utils.Constants.Intents.PRODUCER_ID
import com.feiradagua.feiradagua.utils.Constants.Intents.TUTORIAL
import com.feiradagua.feiradagua.utils.TutorialPreferences
import com.feiradagua.feiradagua.utils.checkByTag
import com.feiradagua.feiradagua.utils.getProducer
import com.feiradagua.feiradagua.view.activitys.user.UserMenuActivity
import com.feiradagua.feiradagua.view.activitys.user.UserStoreInfosActivity
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetSequence
import java.net.URLEncoder

class UserStoreInfosAboutUsFragment : Fragment() {
    private lateinit var binding: FragmentUserStoreInfosAboutUsBinding
    private var tutorial = false
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
        tutorial = arguments?.getBoolean(TUTORIAL) == true

        binding.floatingButtonWhatsApp.setOnClickListener {
            startWhatsAppChat(producer?.phone, producer?.name)
        }

        if(tutorial) {
            context?.let {
                if(TutorialPreferences.getTutorialStatus(it, 2) == true) {
                    initTutorial()
                }
            }
        }else {
            producer = UserMenuActivity.PRODUCERS?.getProducer(getId)
            setUpProducerInfos()
        }
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
            producer.category.forEach { loc ->
                getChipTagCategory(loc)
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

    private fun getChipTagCategory(tag: String) {
        binding.chipFish.checkByTag(tag)
        binding.chipOyster.checkByTag(tag)
        binding.chipShrimp.checkByTag(tag)
        binding.chipAquaponic.checkByTag(tag)
    }

    private fun setUpProducerInfosTutorial() {
        val tutorial = TutorialData().setUpProducerRecyclerView()
        tutorial.forEach {producer ->
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

    private fun initTutorial() {
        setUpProducerInfosTutorial()
        TapTargetSequence(activity).targets(
            TapTarget.forView(
                binding.ivProfile, "Sobre o Produtor",
                "Nesta tela você poderá conferir as informações a respeito do produtor de sua escolha!!"
            )
                .transparentTarget(true).targetCircleColor(R.color.backgroundColor)
                .cancelable(false)
                .descriptionTextColor(R.color.white).titleTextColor(R.color.white)
                .titleTextSize(20).descriptionTextSize(16).targetRadius(100),
            TapTarget.forView(
                binding.floatingButtonWhatsApp, "Contato por WhatsApp",
                "Clicando neste botão você poderá entrar em contato diretamente com o produtor pelo WhatsApp!!"
            )
                .transparentTarget(true).targetCircleColor(R.color.backgroundColor)
                .cancelable(false)
                .descriptionTextColor(R.color.white).titleTextColor(R.color.white)
                .titleTextSize(20).descriptionTextSize(16).targetRadius(120)
        ).listener(
            object : TapTargetSequence.Listener {
                override fun onSequenceFinish() {
                    val intent = Intent(activity, UserStoreInfosActivity::class.java)
                    intent.putExtra(POSITION, 1)
                    intent.putExtra(TUTORIAL, true)
                    context?.let { TutorialPreferences.tutorialPreferences(it, false, 2) }
                    startActivity(intent)
                    activity?.finish()
                }
                override fun onSequenceStep(lastTarget: TapTarget?, targetClicked: Boolean) {}
                override fun onSequenceCanceled(lastTarget: TapTarget?) {}
            }
        ).start()
    }
}