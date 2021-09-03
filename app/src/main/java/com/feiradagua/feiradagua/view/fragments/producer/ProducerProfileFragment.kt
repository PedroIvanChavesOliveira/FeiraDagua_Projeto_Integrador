package com.feiradagua.feiradagua.view.fragments.producer

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.feiradagua.feiradagua.R
import com.feiradagua.feiradagua.databinding.FragmentProducerProfileBinding
import com.feiradagua.feiradagua.model.`class`.TutorialData
import com.feiradagua.feiradagua.utils.Constants
import com.feiradagua.feiradagua.utils.Constants.Intents.TUTORIAL
import com.feiradagua.feiradagua.utils.TutorialPreferences
import com.feiradagua.feiradagua.utils.checkByTag
import com.feiradagua.feiradagua.view.activitys.both.LoginActivity
import com.feiradagua.feiradagua.view.activitys.producer.ProducerMenuActivity
import com.feiradagua.feiradagua.view.activitys.producer.ProducerUpdateProfileActivity
import com.feiradagua.feiradagua.view.activitys.user.UserMenuActivity
import com.feiradagua.feiradagua.view.adapter.ProducerOrdersMainAdapter
import com.feiradagua.feiradagua.viewModel.ProducerProfileViewModel
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetSequence

class ProducerProfileFragment : Fragment() {
    private lateinit var binding: FragmentProducerProfileBinding
    private val viewModelProducerProfile: ProducerProfileViewModel by viewModels()
    private var tutorial = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProducerProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tutorial = arguments?.getBoolean(TUTORIAL) == true

        if(tutorial) {
            context?.let {
                if(TutorialPreferences.getTutorialStatusProducer(it, 5) == true) {
                    initTutorial()
                }
            }
        }else {
            setProducerInfos()
        }

        binding.btLogOutProfile.setOnClickListener {
            viewModelProducerProfile.signOut()
            startActivity(Intent(activity, LoginActivity::class.java))
            activity?.finish()
        }

        binding.btEditProfile.setOnClickListener {
            startActivity(Intent(activity, ProducerUpdateProfileActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        if(!tutorial) {
            setChipsToFalse()
            setProducerInfos()
        }
    }

    private fun setProducerInfos() {
        val producer = ProducerMenuActivity.PRODUCER

        Glide.with(this).load(producer.photo).placeholder(R.drawable.logo_feira_dagua_remove).into(binding.ivProfile)
        binding.tvNameTitle.text = getString(R.string.string_name_title_profile, producer.name)
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

    private fun setChipsToFalse() {
        binding.chipFlorianopolisCenter.isChecked = false
        binding.chipFlorianopolisEast.isChecked = false
        binding.chipFlorianopolisNorth.isChecked = false
        binding.chipFlorianopolisSouth.isChecked = false
        binding.chipBiguacu.isChecked = false
        binding.chipPalhoca.isChecked = false
        binding.chipGaropaba.isChecked = false
        binding.chipImbituba.isChecked = false
        binding.chipSunday.isChecked = false
        binding.chipMonday.isChecked = false
        binding.chipTuesday.isChecked = false
        binding.chipWednesday.isChecked = false
        binding.chipThursday.isChecked = false
        binding.chipFriday.isChecked = false
        binding.chipSaturday.isChecked = false
        binding.chipPix.isChecked = false
        binding.chipBankTransfer.isChecked = false
        binding.chipDebitCard.isChecked = false
        binding.chipCreditCard.isChecked = false
        binding.chipFish.isChecked = false
        binding.chipOyster.isChecked = false
        binding.chipShrimp.isChecked = false
        binding.chipAquaponic.isChecked = false
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

    private fun initTutorial() {
        TapTargetSequence(activity).targets(
                TapTarget.forView(
                        binding.ivProfile, "Seu Perfil",
                        "Este é o seu perfil, onde contém os seus dados, que você poderá alterar de acordo com a sua necessidade!!" +
                                " Para isso, basta clicar no botão de 'Atualizar Perfil' e caso queria sair do aplicativo, basta clicar no botão" +
                                " 'Sair'.\n\n Chegamos ao fim do tutorial, agora é só cadastrar os seus produtos e esperar que algum cliente " +
                                " te encontre!!"
                )
                        .transparentTarget(true).targetCircleColor(R.color.backgroundColor)
                        .cancelable(false)
                        .descriptionTextColor(R.color.white).titleTextColor(R.color.white)
                        .titleTextSize(20).descriptionTextSize(16).targetRadius(100)
        ).listener(
                object : TapTargetSequence.Listener {
                    override fun onSequenceFinish() {
                        val intent = Intent(activity, ProducerMenuActivity::class.java)
                        intent.putExtra(TUTORIAL, true)
                        context?.let { TutorialPreferences.tutorialPreferencesProducer(it, false, 5) }
                        startActivity(intent)
                        activity?.finish()
                    }
                    override fun onSequenceStep(lastTarget: TapTarget?, targetClicked: Boolean) {}
                    override fun onSequenceCanceled(lastTarget: TapTarget?) {}
                }
        ).start()
    }
}