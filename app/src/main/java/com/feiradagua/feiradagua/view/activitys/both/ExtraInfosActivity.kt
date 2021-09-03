package com.feiradagua.feiradagua.view.activitys.both

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import com.bumptech.glide.Glide
import com.feiradagua.feiradagua.R
import com.feiradagua.feiradagua.databinding.ActivityExtraInfosBinding
import com.feiradagua.feiradagua.model.`class`.Mask
import com.feiradagua.feiradagua.model.`class`.Registered
import com.feiradagua.feiradagua.utils.Constants.Intents.POSITION
import com.feiradagua.feiradagua.utils.Constants.Intents.TUTORIAL
import com.feiradagua.feiradagua.utils.TutorialPreferences
import com.feiradagua.feiradagua.utils.validatingPhone
import com.feiradagua.feiradagua.view.activitys.producer.ExtraInfosProducerActivity
import com.feiradagua.feiradagua.view.activitys.user.UserMenuActivity
import com.feiradagua.feiradagua.viewModel.ExtraInfosViewModel
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_extra_infos.*

class ExtraInfosActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExtraInfosBinding
    private val viewModelExtraInfos: ExtraInfosViewModel by viewModels()
    private var photo: String? = null
    private var deliveryLocation = ""
    private var completedAdress = ""
    private var isCellphoneOk = false
    private var isCepOk = false
    private var isCityOk = false
    private var isStreetOk = false
    private var isDistrictOk = false
    private var isNumberOk = false
    private var isUfOk = false
    private var getUserPos = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExtraInfosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        textChangeDefault(binding.tietCellNumber, binding.tilCellNumber, R.string.string_cellphone)
        textChangeDefault(binding.tietCep, binding.tilCep, R.string.string_cep_error)
        textChangeDefault(binding.tietCity, binding.tilCity, R.string.string_city)
        textChangeDefault(binding.tietDistrict, binding.tilDistrict, R.string.string_district)
        textChangeDefault(binding.tietNumber, binding.tilNumber, R.string.string_number)
        textChangeDefault(binding.tietStreet, binding.tilStreet, R.string.string_street)
        textChangeDefault(binding.tietUf, binding.tilUf, R.string.string_uf)

        radioButtonListener()
        cepTextInput()
        addingMaskOnCepAndCellphone()
        buttonContinueListener()
        startingCameraActivity()
        chipDeliveryLocationSelection()
    }

    override fun onRestart() {
        super.onRestart()
        photo = CameraAndGalleryActivity.USER_PHOTO
        Glide.with(this).load(photo).placeholder(R.drawable.logo_feira_dagua_remove).into(binding.ivProfile)
    }

    private fun startingCameraActivity() {
        binding.ivProfile.setOnClickListener {
            val intent = Intent(this, CameraAndGalleryActivity::class.java)
            intent.putExtra(POSITION, 1)
            startActivity(intent)
        }
    }

    private fun buttonContinueListener() {
        binding.btContinue.setOnClickListener {
            when(getUserPos) {
                1 -> {
                    val registered = Registered(getUserPos)
                    viewModelExtraInfos.setExtrasInfosUser(
                        getUserPos,
                        completedAdress,
                        registered,
                        binding.tietCellNumber.text.toString(), deliveryLocation
                    )
                    startMenuUserActivity()
                }
                2 -> {
                    val registered = Registered(getUserPos)
                    viewModelExtraInfos.setExtraInfosDB(
                        getUserPos,
                        completedAdress,
                        registered,
                        binding.tietCellNumber.text.toString()
                    )
                    startExtraInfosProducerActivity()
                }
            }
        }
    }

    private fun cepTextInput() {
        binding.tietCep.doOnTextChanged { text, _, _, _ ->
            val cepValue = Mask.unmask(text.toString())
            if(cepValue.length == 8) {
                viewModelExtraInfos.viaCepAPIResponse(cepValue.toInt())
                viewModelExtraInfos.viaCepResponseSuccess.observe(this) { cep ->
                    if(cep.bairro.isNullOrEmpty()) {
                        binding.tilCep.error = "Este CEP não é válido"
                        isCepOk = false
                    }else {
                        binding.tilCep.isErrorEnabled = false
                        isCepOk = true
                        binding.tietCity.setText(cep.localidade)
                        binding.tietStreet.setText(cep.logradouro)
                        binding.tietDistrict.setText(cep.bairro)
                        binding.tietUf.setText(cep.uf)

                        binding.tietCity.isEnabled = false
                        binding.tietStreet.isEnabled = false
                        binding.tietDistrict.isEnabled = false
                        binding.tietUf.isEnabled = false
                    }
                }
            }else {
                isCepOk = false
                binding.tietCity.isEnabled = true
                binding.tietStreet.isEnabled = true
                binding.tietDistrict.isEnabled = true
                binding.tietUf.isEnabled = true
            }
            activatingButton()
        }
    }

    private fun addingMaskOnCepAndCellphone() {
        binding.tietCellNumber.addTextChangedListener(
            Mask.insert(getString(R.string.string_mask_cellphone), binding.tietCellNumber)
        )
        binding.tietCep.addTextChangedListener(
            Mask.insert(getString(R.string.string_mask_cep), binding.tietCep)
        )
        binding.tietUf.addTextChangedListener(
            Mask.insert(getString(R.string.string_mask_uf), binding.tietUf)
        )
    }

    private fun textChangeDefault(
        editText: EditText,
        textInputLayout: TextInputLayout,
        errorString: Int
    ) {
        editText.doOnTextChanged { text, _, _, _ ->
            if (text?.isBlank() == true) {
                textInputLayout.error = getString(
                    R.string.string_error_message, getString(
                        errorString
                    )
                )
                setByTag(editText.tag as String, false)
            } else {
                textInputLayout.isErrorEnabled = false
                setByTag(editText.tag as String, true)
            }

            if(editText.tag == getString(R.string.string_whatsapp_number_hint)) {
                if(text?.length == 15) {
                    if(validatingPhoneNumber(text.toString())) {
                        isCellphoneOk = true
                        textInputLayout.isErrorEnabled = false
                    }else {
                        isCellphoneOk = false
                        textInputLayout.error = getString(R.string.string_error_phone_not_valid)
                    }
                }
            }
            activatingButton()
        }
    }

    private fun validatingPhoneNumber(phone: String): Boolean {
        return phone.validatingPhone()
    }

    private fun setByTag(tag: String, isOk: Boolean) {
        when (tag) {
            getString(R.string.string_whatsapp_number_hint) -> isCellphoneOk = isOk
            getString(R.string.string_cep_hint) -> isCepOk = isOk
            getString(R.string.string_street_hint) -> isStreetOk = isOk
            getString(R.string.string_district_hint) -> isDistrictOk = isOk
            getString(R.string.string_number_hint) -> isNumberOk = isOk
            getString(R.string.string_city_hint) -> isCityOk = isOk
            getString(R.string.string_uf_hint) -> isUfOk = isOk
        }
    }

    private fun activatingButton(): Boolean {
        val isOk: Boolean
        val cepValue = Mask.unmask(binding.tietCep.text.toString())
        if (isCellphoneOk && isCepOk && isStreetOk && isDistrictOk && isNumberOk && isCityOk && isUfOk && deliveryLocation.isNotEmpty()) {
            if(validatingPhoneNumber(binding.tietCellNumber.text.toString())) {
                binding.btContinue.isEnabled = true
            }else {
                binding.tietCellNumber.error = getString(R.string.string_error_phone_not_valid)
                isCellphoneOk = false
            }
            completedAdress = if(!binding.tietComplement.text.isNullOrEmpty()) {
                "${binding.tietStreet.text}, ${binding.tietNumber.text}," +
                        " ${binding.tietComplement.text}, ${binding.tietDistrict.text}," +
                        " ${binding.tietCity.text} - ${binding.tietUf.text} - $cepValue"
            }else {
                "${binding.tietStreet.text}, ${binding.tietNumber.text}," +
                        " ${binding.tietDistrict.text}, ${binding.tietCity.text} - ${binding.tietUf.text} - $cepValue"
            }
            isOk = true
        } else {
            binding.btContinue.isEnabled = false
            isOk = false
        }

        return isOk
    }

    private fun radioButtonListener() {
        binding.rbUser.setOnCheckedChangeListener { buttonView, isChecked ->
            rbStore.isChecked = !isChecked
            binding.chipGroupDeliveryArea.isVisible = true
            binding.tvDeliveryAreaTitle.isVisible = true
            if(deliveryLocation == "Nao") { deliveryLocation = "" }
            getUserPos = 1
            activatingButton()
        }

        binding.rbStore.setOnCheckedChangeListener { buttonView, isChecked ->
            rbUser.isChecked = !isChecked
            binding.chipGroupDeliveryArea.isVisible = false
            binding.tvDeliveryAreaTitle.isVisible = false
            deliveryLocation = "Nao"
            getUserPos = 2
            activatingButton()
        }
    }

    private fun chipDeliveryLocationSelection() {
        binding.chipFlorianopolisCenter.setOnCheckedChangeListener { buttonView, isChecked ->
            checkingDeliveryLocation(isChecked, buttonView.tag.toString())
        }
        binding.chipFlorianopolisEast.setOnCheckedChangeListener { buttonView, isChecked ->
            checkingDeliveryLocation(isChecked, buttonView.tag.toString())
        }
        binding.chipFlorianopolisNorth.setOnCheckedChangeListener { buttonView, isChecked ->
            checkingDeliveryLocation(isChecked, buttonView.tag.toString())
        }
        binding.chipFlorianopolisSouth.setOnCheckedChangeListener { buttonView, isChecked ->
            checkingDeliveryLocation(isChecked, buttonView.tag.toString())
        }
        binding.chipBiguacu.setOnCheckedChangeListener { buttonView, isChecked ->
            checkingDeliveryLocation(isChecked, buttonView.tag.toString())
        }
        binding.chipImbituba.setOnCheckedChangeListener { buttonView, isChecked ->
            checkingDeliveryLocation(isChecked, buttonView.tag.toString())
        }
        binding.chipPalhoca.setOnCheckedChangeListener { buttonView, isChecked ->
            checkingDeliveryLocation(isChecked, buttonView.tag.toString())
        }
        binding.chipGaropaba.setOnCheckedChangeListener { buttonView, isChecked ->
            checkingDeliveryLocation(isChecked, buttonView.tag.toString())
        }
    }

    private fun checkingDeliveryLocation(check: Boolean, tag: String) {
        if(deliveryLocation == tag && !check) {
            deliveryLocation = ""
        }else {
            if(check) {
                deliveryLocation = tag
            }
        }
        activatingButton()
    }

    private fun startMenuUserActivity() {
        val intent = Intent(this, UserMenuActivity::class.java)
        intent.putExtra(TUTORIAL, true)
        for(i in 1..6) {
            if(TutorialPreferences.getTutorialStatus(this, i) == false) {
                TutorialPreferences.tutorialPreferences(this, true, i)
            }
        }
        startActivity(intent)
        finish()
    }

    private fun startExtraInfosProducerActivity() {
        val intent = Intent(this, ExtraInfosProducerActivity::class.java)
        startActivity(intent)
        finish()
    }
}