package com.feiradagua.feiradagua.view.activitys.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.activity.viewModels
import androidx.core.widget.doOnTextChanged
import com.bumptech.glide.Glide
import com.feiradagua.feiradagua.R
import com.feiradagua.feiradagua.databinding.ActivityUserUpdateProfileBinding
import com.feiradagua.feiradagua.model.`class`.Mask
import com.feiradagua.feiradagua.model.`class`.Producer
import com.feiradagua.feiradagua.model.`class`.Registered
import com.feiradagua.feiradagua.model.`class`.User
import com.feiradagua.feiradagua.utils.*
import com.feiradagua.feiradagua.utils.FirebaseTimestampPreferences.setLastModifiedPreferences
import com.feiradagua.feiradagua.view.activitys.both.CameraAndGalleryActivity
import com.feiradagua.feiradagua.view.activitys.producer.ProducerMenuActivity
import com.feiradagua.feiradagua.viewModel.UserUpdateProfileViewModel
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FieldValue
import kotlinx.android.synthetic.main.activity_extra_infos.*
import java.util.*

class UserUpdateProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserUpdateProfileBinding
    private val viewModelUserUpdate: UserUpdateProfileViewModel by viewModels()
    private var photo: String? = null
    private var deliveryLocation = ""
    private var completedAdress = ""
    private var isNameOk = false
    private var isCellphoneOk = false
    private var isCepOk = false
    private var isCityOk = false
    private var isStreetOk = false
    private var isDistrictOk = false
    private var isNumberOk = false
    private var isUfOk = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserUpdateProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        textChangeDefault(binding.tietCellNumberUpdate, binding.tilCellNumberUpdate, R.string.string_cellphone)
        textChangeDefault(binding.tietNameUpdate, binding.tilNameUpdate, R.string.string_name)
        textChangeDefault(binding.tietCepUpdate, binding.tilCepUpdate, R.string.string_cep_error)
        textChangeDefault(binding.tietCityUpdate, binding.tilCityUpdate, R.string.string_city)
        textChangeDefault(binding.tietDistrictUpdate, binding.tilDistrictUpdate, R.string.string_district)
        textChangeDefault(binding.tietNumberUpdate, binding.tilNumberUpdate, R.string.string_number)
        textChangeDefault(binding.tietStreetUpdate, binding.tilStreetUpdate, R.string.string_street)
        textChangeDefault(binding.tietUfUpdate, binding.tilUfUpdate, R.string.string_uf)

        setUpInfos()
        cepTextInput()
        addingMaskOnCepAndCellphone()
        startingCameraActivity()
        buttonUpdateListener()
        chipDeliveryLocationSelection()
    }

    private fun setUpInfos() {
        val user = UserMenuActivity.USER

        Glide.with(this).load(user.photo).placeholder(R.drawable.logo_feira_dagua_remove).into(binding.ivProfileUpdate)
        binding.tietNameUpdate.setText(user.name)
        binding.tietCellNumberUpdate.setText(user.phone)
        binding.tietCepUpdate.splitAdress(user.adress)
        binding.tietCityUpdate.splitAdress(user.adress)
        binding.tietComplementUpdate.splitAdress(user.adress)
        binding.tietDistrictUpdate.splitAdress(user.adress)
        binding.tietNumberUpdate.splitAdress(user.adress)
        binding.tietStreetUpdate.splitAdress(user.adress)
        binding.tietUfUpdate.splitAdress(user.adress)
        deliveryLocation = user.deliveryArea
        selectChipByTag(user.deliveryArea)
    }

    override fun onRestart() {
        super.onRestart()
        photo = CameraAndGalleryActivity.USER_PHOTO
        Glide.with(this).load(photo).placeholder(R.drawable.logo_feira_dagua_remove).into(binding.ivProfileUpdate)
    }

    private fun selectChipByTag(tag: String) {
        binding.chipFlorianopolisCenter.checkByTagUser(tag)
        binding.chipFlorianopolisNorth.checkByTagUser(tag)
        binding.chipFlorianopolisEast.checkByTagUser(tag)
        binding.chipFlorianopolisSouth.checkByTagUser(tag)
        binding.chipPalhoca.checkByTagUser(tag)
        binding.chipImbituba.checkByTagUser(tag)
        binding.chipBiguacu.checkByTagUser(tag)
        binding.chipGaropaba.checkByTagUser(tag)
    }

    private fun buttonUpdateListener() {
        binding.btContinueUpdate.setOnClickListener {
            val user = UserMenuActivity.USER
            val setUser = User(user.uid, binding.tietNameUpdate.text.toString(),user.email,
                binding.tietCellNumberUpdate.text.toString(),photo?:user.photo,user.type,completedAdress, user.token, deliveryLocation )
            setLastModifiedPreferences(this, 1, Calendar.getInstance().time.toString())
            viewModelUserUpdate.updateUser(setUser)
            viewModelUserUpdate.updateUser.observe(this) {
                if(it) {
                    finish()
                }
            }
        }
    }

    private fun cepTextInput() {
        binding.tietCepUpdate.doOnTextChanged { text, _, _, _ ->
            val cepValue = Mask.unmask(text.toString())
            if(cepValue.length == 8) {
                viewModelUserUpdate.viaCepAPIResponse(cepValue.toInt())
                viewModelUserUpdate.viaCepResponseSuccess.observe(this) {cep ->
                    if(cep.bairro.isNullOrEmpty()) {
                        binding.tilCepUpdate.error = "Este CEP não é válido"
                        isCepOk = false
                    }else {
                        binding.tilCepUpdate.isErrorEnabled = false
                        isCepOk = true
                        binding.tietCityUpdate.setText(cep.localidade)
                        binding.tietStreetUpdate.setText(cep.logradouro)
                        binding.tietDistrictUpdate.setText(cep.bairro)
                        binding.tietUfUpdate.setText(cep.uf)

                        binding.tietCityUpdate.isEnabled = false
                        binding.tietStreetUpdate.isEnabled = false
                        binding.tietDistrictUpdate.isEnabled = false
                        binding.tietUfUpdate.isEnabled = false
                    }
                }
            }else {
                binding.tietCityUpdate.isEnabled = true
                binding.tietStreetUpdate.isEnabled = true
                binding.tietDistrictUpdate.isEnabled = true
                binding.tietUfUpdate.isEnabled = true
            }
        }
    }

    private fun addingMaskOnCepAndCellphone() {
        binding.tietCellNumberUpdate.addTextChangedListener(Mask.insert(getString(R.string.string_mask_cellphone), binding.tietCellNumberUpdate))
        binding.tietCepUpdate.addTextChangedListener(Mask.insert(getString(R.string.string_mask_cep), binding.tietCepUpdate))
        binding.tietUfUpdate.addTextChangedListener(Mask.insert(getString(R.string.string_mask_uf), binding.tietUfUpdate))
    }

    private fun textChangeDefault(editText: EditText, textInputLayout: TextInputLayout, errorString: Int) {
        editText.doOnTextChanged { text, start, before, count ->
            if (text?.isBlank() == true) {
                textInputLayout.error = getString(R.string.string_error_message, getString(errorString))
                setByTag(editText.tag as String, false)
            } else {
                textInputLayout.isErrorEnabled = false
                setByTag(editText.tag as String, true)
            }

            if(editText.tag == getString(R.string.string_cep_hint)) {
                if (count == 9) {
                    textInputLayout.isErrorEnabled = false
                    setByTag(editText.tag as String, true)
                }else {
//                    textInputLayout.error = getString(R.string.string_error_message_cep)
                    setByTag(editText.tag as String, false)
                }
            }

            if(editText.tag == getString(R.string.string_whatsapp_number_hint)) {
                if(text?.length == 15) {
                    if(validatingPhoneNumber(text.toString())) {
                        isNumberOk = true
                        textInputLayout.isErrorEnabled = false
                    }else {
                        isNumberOk = false
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

    private fun startingCameraActivity() {
        binding.ivProfileUpdate.setOnClickListener {
            val intent = Intent(this, CameraAndGalleryActivity::class.java)
            intent.putExtra(Constants.Intents.POSITION, 1)
            startActivity(intent)
        }
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
            getString(R.string.string_name_hint_user) -> isNameOk = isOk
        }
    }

    private fun activatingButton(): Boolean {
        val isOk: Boolean
        val cepValue = Mask.unmask(binding.tietCepUpdate.text.toString())
        if (isCellphoneOk && isCepOk && isStreetOk && isDistrictOk && isNumberOk && isCityOk && isUfOk && isNameOk && deliveryLocation.isNotEmpty()) {
            binding.btContinueUpdate.isEnabled = true
            completedAdress = if(!binding.tietComplementUpdate.text.isNullOrEmpty()) {
                "${binding.tietStreetUpdate.text}, ${binding.tietNumberUpdate.text}," +
                        " ${binding.tietComplementUpdate.text}, ${binding.tietDistrictUpdate.text}," +
                        " ${binding.tietCityUpdate.text} - ${binding.tietUfUpdate.text} - $cepValue"
            }else {
                "${binding.tietStreetUpdate.text}, ${binding.tietNumberUpdate.text}," +
                        " ${binding.tietDistrictUpdate.text}, ${binding.tietCityUpdate.text} - ${binding.tietUfUpdate.text} - $cepValue"
            }
            isOk = true
        } else {
            binding.btContinueUpdate.isEnabled = false
            isOk = false
        }

        return isOk
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
}