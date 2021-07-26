package com.feiradagua.feiradagua.view.activitys.producer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.feiradagua.feiradagua.R
import com.feiradagua.feiradagua.databinding.ActivityExtraInfosProducerBinding
import com.feiradagua.feiradagua.utils.Constants.Intents.EXTRA_INFOS
import com.feiradagua.feiradagua.utils.addItem
import com.feiradagua.feiradagua.utils.removeItem
import com.feiradagua.feiradagua.viewModel.ExtraInfosProducerViewModel

class ExtraInfosProducerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExtraInfosProducerBinding
    private var viewModelExtraInfosProducer = ExtraInfosProducerViewModel()
    private var deliveryDateArray = mutableListOf<String>()
    private var deliveryLocationArray = mutableListOf<String>()
    private var paymentArray = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExtraInfosProducerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModelExtraInfosProducer = ViewModelProvider(this).get(ExtraInfosProducerViewModel::class.java)

        chipDeliveryDateSelection()
        chipDeliveryLocationSelection()
        chipPaymentSelection()
        insertDataToDB()
    }

    private fun insertDataToDB() {
        binding.btContinue.setOnClickListener {
            viewModelExtraInfosProducer.setExtraInfosDB(
                    presentationMessage(), deliveryDateArray, deliveryLocationArray, paymentArray
            )
//            startProducerMenu()
        }
    }

    private fun presentationMessage(): String {
        return if(binding.tietPresentationPresentation.text.isNullOrEmpty()) {
            "Sem apresentação da Empresa!"
        }else {
            binding.tietPresentationPresentation.text.toString()
        }
    }

//    private fun startProducerMenu() {
//        val intent = Intent(this, ProducerMenuActivity::class.java)
//        intent.putExtra(EXTRA_INFOS, 1)
//        startActivity(intent)
//        finish()
//    }

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

    private fun activatingButton() {
        binding.btContinue.isEnabled = deliveryDateArray.isNotEmpty() && deliveryLocationArray.isNotEmpty() && paymentArray.isNotEmpty()
    }

    private fun checkingArrays(array: MutableList<String>, isChecked: Boolean, tag: String) {
        if(isChecked) {
            array.addItem(tag)
        }else {
            array.removeItem(tag)
        }
        activatingButton()
    }
}