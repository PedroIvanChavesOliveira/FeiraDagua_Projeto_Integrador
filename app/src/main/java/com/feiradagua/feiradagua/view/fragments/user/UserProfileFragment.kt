package com.feiradagua.feiradagua.view.fragments.user

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.feiradagua.feiradagua.R
import com.feiradagua.feiradagua.databinding.FragmentUserProfileBinding
import com.feiradagua.feiradagua.utils.Constants
import com.feiradagua.feiradagua.utils.Constants.Intents.TUTORIAL
import com.feiradagua.feiradagua.utils.TutorialPreferences
import com.feiradagua.feiradagua.view.activitys.both.LoginActivity
import com.feiradagua.feiradagua.view.activitys.user.UserMenuActivity
import com.feiradagua.feiradagua.view.activitys.user.UserStoreInfosActivity
import com.feiradagua.feiradagua.view.activitys.user.UserUpdateProfileActivity
import com.feiradagua.feiradagua.viewModel.UserProfileViewModel
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetSequence

class UserProfileFragment : Fragment() {
    private lateinit var binding: FragmentUserProfileBinding
    private var tutorial = false
    private val viewModelUserProfile: UserProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tutorial = arguments?.getBoolean(TUTORIAL) == true

        binding.btLogOutProfile.setOnClickListener {
            viewModelUserProfile.signOut()
            startActivity(Intent(activity, LoginActivity::class.java))
            UserMenuActivity.MY_CART.clear()
            activity?.finish()
        }

        binding.btEditProfile.setOnClickListener {
            startActivity(Intent(activity, UserUpdateProfileActivity::class.java))
        }

        if(tutorial) {
            context?.let {
                if(TutorialPreferences.getTutorialStatus(it, 6) == true) {
                    initTutorial()
                }
            }
        }else {
            setUserInfos()
        }
    }

    private fun setUserInfos() {
        val user = UserMenuActivity.USER

        Glide.with(this).load(user.photo).into(binding.ivProfile)
        binding.tvNameValue.text = user.name
        binding.tvEmailValue.text = user.email
        binding.tvCellPhoneValue.text = user.phone
        binding.tvAdressValue.text = user.adress
    }

    private fun initTutorial() {
        TapTargetSequence(activity).targets(
            TapTarget.forView(
                binding.ivProfile, "Seu Perfil",
                "Este é o seu perfil, onde contém os seus dados, que você poderá alterar de acordo com a sua necessidade!!"
            )
                .transparentTarget(true).targetCircleColor(R.color.backgroundColor)
                .cancelable(false)
                .descriptionTextColor(R.color.white).titleTextColor(R.color.white)
                .titleTextSize(20).descriptionTextSize(16).targetRadius(100),
            TapTarget.forView(
                binding.btEditProfile, "Editar Seus Dados",
                "Clicando nesse botão você poderá alterar os seus dados, inclusive a sua foto (bastando clicar na imagem que aparecer), " +
                        "sempre que julgar necessário, sinta-se a vontade de alterá-los!!"
            )
                .transparentTarget(true).targetCircleColor(R.color.backgroundColor)
                .cancelable(false)
                .descriptionTextColor(R.color.white).titleTextColor(R.color.white)
                .titleTextSize(20).descriptionTextSize(16).targetRadius(100),
            TapTarget.forView(
                binding.btLogOutProfile, "Sair de Sua Conta",
                "Caso sinta a necessidade de fazer o Log Out, basta clicar nesse botão, MAS não demore, sentiremos saudades !!"
            )
                .transparentTarget(true).targetCircleColor(R.color.backgroundColor)
                .cancelable(false)
                .descriptionTextColor(R.color.white).titleTextColor(R.color.white)
                .titleTextSize(20).descriptionTextSize(16).targetRadius(100)
        ).listener(
            object : TapTargetSequence.Listener {
                override fun onSequenceFinish() {
                    val intent = Intent(activity, UserMenuActivity::class.java)
                    context?.let { TutorialPreferences.tutorialPreferences(it, false, 6) }
                    startActivity(intent)
                    activity?.finish()
                }
                override fun onSequenceStep(lastTarget: TapTarget?, targetClicked: Boolean) {}
                override fun onSequenceCanceled(lastTarget: TapTarget?) {}
            }
        ).start()
    }
}