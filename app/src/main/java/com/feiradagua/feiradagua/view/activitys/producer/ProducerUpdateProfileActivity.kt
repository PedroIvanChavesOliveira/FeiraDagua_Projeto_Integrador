package com.feiradagua.feiradagua.view.activitys.producer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.activity.viewModels
import androidx.core.widget.doOnTextChanged
import com.bumptech.glide.Glide
import com.feiradagua.feiradagua.R
import com.feiradagua.feiradagua.databinding.ActivityProducerUpdateProfileBinding
import com.feiradagua.feiradagua.model.`class`.Mask
import com.feiradagua.feiradagua.model.`class`.Producer
import com.feiradagua.feiradagua.utils.*
import com.feiradagua.feiradagua.utils.Constants.Intents.POSITION
import com.feiradagua.feiradagua.view.activitys.both.CameraAndGalleryActivity
import com.feiradagua.feiradagua.viewModel.ProducerUpdateProfileViewModel
import com.google.android.material.textfield.TextInputLayout

class ProducerUpdateProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProducerUpdateProfileBinding
    private val viewModelProducerUpdate: ProducerUpdateProfileViewModel by viewModels()
    private var deliveryDateArray = mutableListOf<String>()
    private var deliveryLocationArray = mutableListOf<String>()
    private var paymentArray = mutableListOf<String>()
    private var categoryArray = mutableListOf<String>()
    private var completedAdress = ""
    private var isCellphoneOk = false
    private var isCepOk = false
    private var isCityOk = false
    private var isStreetOk = false
    private var isDistrictOk = false
    private var isNumberOk = false
    private var isUfOk = false
    private var photo: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProducerUpdateProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        textChangeDefault(binding.tietCellNumberUpdateProducer, binding.tilCellNumberUpdateProducer, R.string.string_cellphone)
        textChangeDefault(binding.tietCepUpdateProducer, binding.tilCepUpdateProducer, R.string.string_cep_error)
        textChangeDefault(binding.tietCityUpdateProducer, binding.tilCityUpdateProducer, R.string.string_city)
        textChangeDefault(binding.tietDistrictUpdateProducer, binding.tilDistrictUpdateProducer, R.string.string_district)
        textChangeDefault(binding.tietNumberUpdateProducer, binding.tilNumberUpdateProducer, R.string.string_number)
        textChangeDefault(binding.tietStreetUpdateProducer, binding.tilStreetUpdateProducer, R.string.string_street)
        textChangeDefault(binding.tietUfUpdateProducer, binding.tilUfUpdateProducer, R.string.string_uf)

        setProducerInfos()
        chipDeliveryDateSelection()
        chipDeliveryLocationSelection()
        chipPaymentSelection()
        chipCategorySelection()
        presentationMessage()
        startingCameraActivity()
        addingMaskOnCepAndCellphone()
        updatingProducer()
        cepTextInput()
    }

    override fun onRestart() {
        super.onRestart()
        photo = CameraAndGalleryActivity.USER_PHOTO
        Glide.with(this).load(photo).placeholder(R.drawable.logo_feira_dagua_remove).into(binding.ivProfileUpdateProducer)
    }

    private fun cepTextInput() {
        binding.tietCepUpdateProducer.doOnTextChanged { text, _, _, _ ->
            val cepValue = Mask.unmask(text.toString())
            if(cepValue.length == 8) {
                viewModelProducerUpdate.viaCepAPIResponse(cepValue.toInt())
                viewModelProducerUpdate.viaCepResponseSuccess.observe(this) {cep ->
                    if(cep.bairro.isNullOrEmpty()) {
                        binding.tilCepUpdateProducer.error = "Este CEP não é válido"
                        isCepOk = false
                    }else {
                        binding.tilCepUpdateProducer.isErrorEnabled = false
                        isCepOk = true
                        binding.tietCityUpdateProducer.setText(cep.localidade)
                        binding.tietStreetUpdateProducer.setText(cep.logradouro)
                        binding.tietDistrictUpdateProducer.setText(cep.bairro)
                        binding.tietUfUpdateProducer.setText(cep.uf)

                        binding.tietCityUpdateProducer.isEnabled = false
                        binding.tietStreetUpdateProducer.isEnabled = false
                        binding.tietDistrictUpdateProducer.isEnabled = false
                        binding.tietUfUpdateProducer.isEnabled = false
                    }
                }
            }else {
                binding.tietCityUpdateProducer.isEnabled = true
                binding.tietStreetUpdateProducer.isEnabled = true
                binding.tietDistrictUpdateProducer.isEnabled = true
                binding.tietUfUpdateProducer.isEnabled = true
            }
        }
    }

    private fun addingMaskOnCepAndCellphone() {
        binding.tietCellNumberUpdateProducer.addTextChangedListener(Mask.insert(getString(R.string.string_mask_cellphone), binding.tietCellNumberUpdateProducer))
        binding.tietCepUpdateProducer.addTextChangedListener(Mask.insert(getString(R.string.string_mask_cep), binding.tietCepUpdateProducer))
        binding.tietUfUpdateProducer.addTextChangedListener(Mask.insert(getString(R.string.string_mask_uf), binding.tietUfUpdateProducer))
    }

    private fun updatingProducer() {
        binding.btContinueUpdateProducer.setOnClickListener {
            val producer = ProducerMenuActivity.PRODUCER
            val setProducer = Producer(producer.uid, binding.tietNameUpdateProducer.text.toString(),
                producer.email, binding.tietCellNumberUpdateProducer.text.toString(), photo?:producer.photo,
                producer.type, completedAdress, binding.tietPresentationUpdateProducer.text.toString(),
                deliveryDateArray, deliveryLocationArray, paymentArray, categoryArray)
            viewModelProducerUpdate.updateProducer(setProducer)
            viewModelProducerUpdate.updateProducer.observe(this) {
                finish()
            }
        }
    }

    private fun textChangeDefault(editText: EditText, textInputLayout: TextInputLayout, errorString: Int) {
        editText.doOnTextChanged { text, _, _, _ ->
            if (text?.isBlank() == true) {
                textInputLayout.error = getString(R.string.string_error_message, getString(errorString))
                setByTag(editText.tag as String, false)
            } else {
                textInputLayout.isErrorEnabled = false
                setByTag(editText.tag as String, true)
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
        binding.ivProfileUpdateProducer.setOnClickListener {
            val intent = Intent(this, CameraAndGalleryActivity::class.java)
            intent.putExtra(POSITION, 1)
            startActivity(intent)
        }
    }

    private fun setByTag(tag: String, isOk: Boolean) {
        when (tag) {
            getString(R.string.string_whatsapp_number_hint) -> isCellphoneOk = isOk
            getString(R.string.string_cep_hint) -> isCepOk = isOk
            getString(R.string.string_street_hint) -> isStreetOk = isOk
            getString(R.string.string_district_hint) -> isDistrictOk = isOk
            getString(R.string.string_number_hint_producer) -> isNumberOk = isOk
            getString(R.string.string_city_hint) -> isCityOk = isOk
            getString(R.string.string_uf_hint) -> isUfOk = isOk
        }
    }

    private fun activatingButton(): Boolean {
        val isOk: Boolean
        val cepValue = Mask.unmask(binding.tietCepUpdateProducer.text.toString())
        if (isCellphoneOk && isCepOk && isStreetOk && isDistrictOk && isNumberOk && isCityOk && isUfOk &&
                deliveryDateArray.isNotEmpty() && deliveryLocationArray.isNotEmpty()
                && paymentArray.isNotEmpty() && categoryArray.isNotEmpty()) {
            binding.btContinueUpdateProducer.isEnabled = true
            completedAdress = if(!binding.tietComplementUpdateProducer.text.isNullOrEmpty()) {
                "${binding.tietStreetUpdateProducer.text},${binding.tietNumberUpdateProducer.text}," +
                        "${binding.tietComplementUpdateProducer.text},${binding.tietDistrictUpdateProducer.text}," +
                        "${binding.tietCityUpdateProducer.text}-${binding.tietUfUpdateProducer.text}-$cepValue"
            }else {
                "${binding.tietStreetUpdateProducer.text},${binding.tietNumberUpdateProducer.text}," +
                        "${binding.tietDistrictUpdateProducer.text},${binding.tietCityUpdateProducer.text}-${binding.tietUfUpdateProducer.text}-$cepValue"
            }
            isOk = true
        } else {
            binding.btContinueUpdateProducer.isEnabled = false
            isOk = false
        }

        return isOk
    }

    private fun setProducerInfos() {
        val producer = ProducerMenuActivity.PRODUCER

        Glide.with(this).load(producer.photo).placeholder(R.drawable.logo_feira_dagua_remove).into(binding.ivProfileUpdateProducer)
        binding.tietNameUpdateProducer.setText(producer.name)
        binding.tietCellNumberUpdateProducer.setText(producer.phone)
        binding.tietCepUpdateProducer.splitAdress(producer.adress)
        binding.tietCityUpdateProducer.splitAdress(producer.adress)
        binding.tietComplementUpdateProducer.splitAdress(producer.adress)
        binding.tietDistrictUpdateProducer.splitAdress(producer.adress)
        binding.tietNumberUpdateProducer.splitAdress(producer.adress)
        binding.tietStreetUpdateProducer.splitAdress(producer.adress)
        binding.tietUfUpdateProducer.splitAdress(producer.adress)

        binding.tietPresentationUpdateProducer.setText(producer.description)

        producer.deliveryLocation.forEach { loc ->
            getChipTagDeliveryLocation(loc, deliveryLocationArray)
        }
        producer.deliveryDate.forEach { loc ->
            getChipTagDeliveryDate(loc, deliveryDateArray)
        }
        producer.payment.forEach { loc ->
            getChipTagPayment(loc, paymentArray)
        }
        producer.category.forEach { loc ->
            getChipTagCategory(loc, categoryArray)
        }

        binding.btContinueUpdateProducer.isEnabled = true
    }

    private fun presentationMessage(): String {
        return if(binding.tietPresentationUpdateProducer.text.isNullOrEmpty()) {
            "Sem apresentação da Empresa!"
        }else {
            binding.tietPresentationUpdateProducer.text.toString()
        }
    }

    private fun chipDeliveryDateSelection() {
        binding.chipSunday.setOnCheckedChangeListener { buttonView, isChecked ->
            checkingArrays(deliveryDateArray, isChecked, buttonView.tag.toString())
        }
        binding.chipMonday.setOnCheckedChangeListener { buttonView, isChecked ->
            checkingArrays(deliveryDateArray, isChecked, buttonView.tag.toString())
        }
        binding.chipTuesday.setOnCheckedChangeListener { buttonView, isChecked ->
            checkingArrays(deliveryDateArray, isChecked, buttonView.tag.toString())
        }
        binding.chipWednesday.setOnCheckedChangeListener { buttonView, isChecked ->
            checkingArrays(deliveryDateArray, isChecked, buttonView.tag.toString())
        }
        binding.chipThursday.setOnCheckedChangeListener { buttonView, isChecked ->
            checkingArrays(deliveryDateArray, isChecked, buttonView.tag.toString())
        }
        binding.chipFriday.setOnCheckedChangeListener { buttonView, isChecked ->
            checkingArrays(deliveryDateArray, isChecked, buttonView.tag.toString())
        }
        binding.chipSaturday.setOnCheckedChangeListener { buttonView, isChecked ->
            checkingArrays(deliveryDateArray, isChecked, buttonView.tag.toString())
        }
    }

    private fun chipDeliveryLocationSelection() {
        binding.chipFlorianopolisCenter.setOnCheckedChangeListener { buttonView, isChecked ->
            checkingArrays(deliveryLocationArray, isChecked, buttonView.tag.toString())
        }
        binding.chipFlorianopolisEast.setOnCheckedChangeListener { buttonView, isChecked ->
            checkingArrays(deliveryLocationArray, isChecked, buttonView.tag.toString())
        }
        binding.chipFlorianopolisNorth.setOnCheckedChangeListener { buttonView, isChecked ->
            checkingArrays(deliveryLocationArray, isChecked, buttonView.tag.toString())
        }
        binding.chipFlorianopolisSouth.setOnCheckedChangeListener { buttonView, isChecked ->
            checkingArrays(deliveryLocationArray, isChecked, buttonView.tag.toString())
        }
        binding.chipBiguacu.setOnCheckedChangeListener { buttonView, isChecked ->
            checkingArrays(deliveryLocationArray, isChecked, buttonView.tag.toString())
        }
        binding.chipImbituba.setOnCheckedChangeListener { buttonView, isChecked ->
            checkingArrays(deliveryLocationArray, isChecked, buttonView.tag.toString())
        }
        binding.chipPalhoca.setOnCheckedChangeListener { buttonView, isChecked ->
            checkingArrays(deliveryLocationArray, isChecked, buttonView.tag.toString())
        }
        binding.chipGaropaba.setOnCheckedChangeListener { buttonView, isChecked ->
            checkingArrays(deliveryLocationArray, isChecked, buttonView.tag.toString())
        }
    }

    private fun chipPaymentSelection() {
        binding.chipPix.setOnCheckedChangeListener { buttonView, isChecked ->
            checkingArrays(paymentArray, isChecked, buttonView.tag.toString())
        }
        binding.chipBankTransfer.setOnCheckedChangeListener { buttonView, isChecked ->
            checkingArrays(paymentArray, isChecked, buttonView.tag.toString())
        }
        binding.chipCreditCard.setOnCheckedChangeListener { buttonView, isChecked ->
            checkingArrays(paymentArray, isChecked, buttonView.tag.toString())
        }
        binding.chipDebitCard.setOnCheckedChangeListener { buttonView, isChecked ->
            checkingArrays(paymentArray, isChecked, buttonView.tag.toString())
        }
    }

    private fun chipCategorySelection() {
        binding.chipFish.setOnCheckedChangeListener { buttonView, isChecked ->
            checkingArrays(categoryArray, isChecked, buttonView.tag.toString())
        }
        binding.chipOyster.setOnCheckedChangeListener { buttonView, isChecked ->
            checkingArrays(categoryArray, isChecked, buttonView.tag.toString())
        }
        binding.chipAquaponic.setOnCheckedChangeListener { buttonView, isChecked ->
            checkingArrays(categoryArray, isChecked, buttonView.tag.toString())
        }
        binding.chipShrimp.setOnCheckedChangeListener { buttonView, isChecked ->
            checkingArrays(categoryArray, isChecked, buttonView.tag.toString())
        }
    }

    private fun checkingArrays(array: MutableList<String>, isChecked: Boolean, tag: String) {
        if(isChecked) {
            array.addItem(tag)
        }else {
            array.removeItem(tag)
        }
        activatingButton()
    }

    private fun getChipTagDeliveryLocation(tag: String, array: MutableList<String>) {
        binding.chipFlorianopolisCenter.checkByTagUpdate(tag, array)
        binding.chipFlorianopolisEast.checkByTagUpdate(tag, array)
        binding.chipFlorianopolisNorth.checkByTagUpdate(tag, array)
        binding.chipFlorianopolisSouth.checkByTagUpdate(tag, array)
        binding.chipBiguacu.checkByTagUpdate(tag, array)
        binding.chipPalhoca.checkByTagUpdate(tag, array)
        binding.chipGaropaba.checkByTagUpdate(tag, array)
        binding.chipImbituba.checkByTagUpdate(tag, array)
    }

    private fun getChipTagDeliveryDate(tag: String, array: MutableList<String>) {
        binding.chipSunday.checkByTagUpdate(tag, array)
        binding.chipMonday.checkByTagUpdate(tag, array)
        binding.chipTuesday.checkByTagUpdate(tag, array)
        binding.chipWednesday.checkByTagUpdate(tag, array)
        binding.chipThursday.checkByTagUpdate(tag, array)
        binding.chipFriday.checkByTagUpdate(tag, array)
        binding.chipSaturday.checkByTagUpdate(tag, array)
    }

    private fun getChipTagPayment(tag: String, array: MutableList<String>) {
        binding.chipPix.checkByTagUpdate(tag, array)
        binding.chipBankTransfer.checkByTagUpdate(tag, array)
        binding.chipDebitCard.checkByTagUpdate(tag, array)
        binding.chipCreditCard.checkByTagUpdate(tag, array)
    }

    private fun getChipTagCategory(tag: String, array: MutableList<String>) {
        binding.chipFish.checkByTagUpdate(tag, array)
        binding.chipOyster.checkByTagUpdate(tag, array)
        binding.chipShrimp.checkByTagUpdate(tag, array)
        binding.chipAquaponic.checkByTagUpdate(tag, array)
    }
}