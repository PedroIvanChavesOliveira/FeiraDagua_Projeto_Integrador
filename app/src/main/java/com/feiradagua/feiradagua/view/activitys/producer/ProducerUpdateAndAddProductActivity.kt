package com.feiradagua.feiradagua.view.activitys.producer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.core.widget.doBeforeTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.feiradagua.feiradagua.R
import com.feiradagua.feiradagua.databinding.ActivityProducerUpdateAndAddProductBinding
import com.feiradagua.feiradagua.model.`class`.Products
import com.feiradagua.feiradagua.model.`class`.TutorialData
import com.feiradagua.feiradagua.utils.Constants
import com.feiradagua.feiradagua.utils.Constants.Intents.EXTRA_INFOS
import com.feiradagua.feiradagua.utils.Constants.Intents.POSITION
import com.feiradagua.feiradagua.utils.Constants.Intents.PRODUCT_ID
import com.feiradagua.feiradagua.utils.Constants.Intents.PRODUCT_UPDATE
import com.feiradagua.feiradagua.utils.Constants.Intents.TUTORIAL
import com.feiradagua.feiradagua.utils.FirebaseTimestampPreferences
import com.feiradagua.feiradagua.utils.FirebaseTimestampPreferences.setLastModifiedPreferences
import com.feiradagua.feiradagua.utils.TutorialPreferences
import com.feiradagua.feiradagua.utils.generateRandomUUID
import com.feiradagua.feiradagua.view.activitys.both.CameraAndGalleryActivity
import com.feiradagua.feiradagua.viewModel.UpdateAndAddProductViewModel
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetSequence
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FieldValue
import java.util.*

class ProducerUpdateAndAddProductActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var binding: ActivityProducerUpdateAndAddProductBinding
    private val viewModelUpdateAndAddProduct: UpdateAndAddProductViewModel by viewModels()
    private var weightOk: String = ""
    private var gramasOk: String = ""
    private var tutorial = false
    private var getProducts: Products? = null
    private var nameOk = false
    private var descriptionOk = false
    private var priceOk = false
    private var photo: String? = null
    private var id = generateRandomUUID()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProducerUpdateAndAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getProducts = intent.getParcelableExtra(PRODUCT_UPDATE)
        tutorial = intent.getBooleanExtra(TUTORIAL, false)

        if(tutorial) {
            if(TutorialPreferences.getTutorialStatusProducer(this, 2) == true) {
                initTutorial()
            }
        }else {
            getProducts?.let { product ->
                Glide.with(this).load(product.photo).placeholder(R.drawable.logo_feira_dagua_remove).into(binding.ivProductUpdateAndAdd)
                binding.tietProductNameUpdate.setText(product.name)
                binding.tietDescriptionUpdate.setText(product.description)
                binding.tietProductValueUpdate.setText(product.price.toString())

                textChangeUpdate(binding.tietProductNameUpdate, binding.tilProductNameUpdate, R.string.string_product_name_title, product)
                textChangeUpdate(binding.tietDescriptionUpdate, binding.tilDescriptionUpdate, R.string.string_product_description_title_update, product)
                textChangeUpdate(binding.tietProductValueUpdate, binding.tilProductValueUpdate, R.string.string_product_price_title_update, product)

                setUpSpinnerUpdate(product.weight)
                confirmUpdate(product)
            }?: run {
                binding.btConfirmUpdate.isEnabled = false
                textChangeDefault(binding.tietProductNameUpdate, binding.tilProductNameUpdate, R.string.string_product_name_title)
                textChangeDefault(binding.tietDescriptionUpdate, binding.tilDescriptionUpdate, R.string.string_product_description_title_update)
                textChangeDefault(binding.tietProductValueUpdate, binding.tilProductValueUpdate, R.string.string_product_price_title_update)

                setUpSpinner()
                confirmUpdate(null)
            }
            startCameraActivity(getProducts)
        }
    }

    private fun startCameraActivity(products: Products?) {
        products?.let {
            binding.ivProductUpdateAndAdd.setOnClickListener {
                val intent = Intent(this, CameraAndGalleryActivity::class.java)
                intent.putExtra(PRODUCT_ID, products.id)
                intent.putExtra(POSITION, 2)
                startActivity(intent)
            }
        }?: run {
            binding.ivProductUpdateAndAdd.setOnClickListener {
                val intent = Intent(this, CameraAndGalleryActivity::class.java)
                intent.putExtra(PRODUCT_ID, id)
                intent.putExtra(POSITION, 2)
                startActivity(intent)
            }
        }
    }

    override fun onRestart() {
        super.onRestart()
        photo = CameraAndGalleryActivity.PRODUCTS_PHOTO
        Glide.with(this).load(CameraAndGalleryActivity.PRODUCTS_PHOTO).placeholder(R.drawable.logo_feira_dagua_remove).into(binding.ivProductUpdateAndAdd)
    }

    private fun setUpSpinner() {
        ArrayAdapter.createFromResource(this, R.array.string_spinner_weight_array, R.layout.custom_spinner_text).also {adapter ->
            adapter.setDropDownViewResource(R.layout.custom_spinner_layout)
            binding.spinnerWeight.adapter = adapter
        }

        binding.spinnerWeight.onItemSelectedListener = this
    }

    private fun setUpSpinnerUpdate(value: String) {
        var position: Int
        ArrayAdapter.createFromResource(this, R.array.string_spinner_weight_array, R.layout.custom_spinner_text).also {adapter ->
            adapter.setDropDownViewResource(R.layout.custom_spinner_layout)
            binding.spinnerWeight.adapter = adapter
            position = adapter.getPosition(value)
        }

        if(position == -1) {
            binding.spinnerWeight.setSelection(3)
            binding.sliderWeight.value = value.toFloat()
        }
        binding.spinnerWeight.onItemSelectedListener = this
    }

    private fun confirmUpdate(product: Products?) {
        product?.let {
            binding.btConfirmUpdate.setOnClickListener {
                val setProduct: Products = if(weightOk == "Gramas") {
                    Products(product.id,
                            binding.tietProductNameUpdate.text.toString(), binding.tietDescriptionUpdate.text.toString(),
                            binding.tietProductValueUpdate.text.toString().toDouble(), ProducerMenuActivity.PRODUCER.uid, gramasOk, photo?:product.photo)
                }else {
                    Products(product.id,
                            binding.tietProductNameUpdate.text.toString(), binding.tietDescriptionUpdate.text.toString(),
                            binding.tietProductValueUpdate.text.toString().toDouble(), ProducerMenuActivity.PRODUCER.uid, weightOk, photo?:product.photo)
                }
//                    ProducerMenuActivity.PRODUCTS?.forEachIndexed { index, products ->
//                        if(products.id == setProduct.id) {
//                            ProducerMenuActivity.PRODUCTS?.set(index, setProduct)
//                        }
//                setLastModifiedPreferences(this, 3, Calendar.getInstance().time.toString())
                viewModelUpdateAndAddProduct.addAndUpdateProduct(product.id, setProduct)
                viewModelUpdateAndAddProduct.insertOk.observe(this) {
                    if(it) {
                        finish()
                    }
                }
            }
        }?: run {
            binding.btConfirmUpdate.setOnClickListener {
                val setProduct: Products = if(weightOk == "Gramas") {
                    Products(id,
                            binding.tietProductNameUpdate.text.toString(), binding.tietDescriptionUpdate.text.toString(),
                            binding.tietProductValueUpdate.text.toString().toDouble(), ProducerMenuActivity.PRODUCER.uid, gramasOk, photo?:"")
                }else {
                    Products(id,
                            binding.tietProductNameUpdate.text.toString(), binding.tietDescriptionUpdate.text.toString(),
                            binding.tietProductValueUpdate.text.toString().toDouble(), ProducerMenuActivity.PRODUCER.uid, weightOk,photo?:"")
                }
//                setLastModifiedPreferences(this, 3, Calendar.getInstance().time.toString())
                viewModelUpdateAndAddProduct.addAndUpdateProduct(id, setProduct)
                viewModelUpdateAndAddProduct.insertOk.observe(this) {
                    if(it) {
                        finish()
                    }
                }
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

                if(!text.isNullOrEmpty() && editText.tag.equals(getString(R.string.string_product_price_hint))) {
                    if(text.first() == '0') {
                        textInputLayout.error = getString(R.string.string_error_value_equals_zero)
                        setByTag(editText.tag as String, false)
                    }else {
                        textInputLayout.isErrorEnabled = false
                        setByTag(editText.tag as String, true)
                    }
                }
            }

            activatingButton(null)
        }
    }

    private fun textChangeUpdate(editText: EditText, textInputLayout: TextInputLayout, errorString: Int, product: Products?) {
        nameOk = true
        descriptionOk = true
        priceOk = true

        editText.doOnTextChanged { text, _, _, _ ->
            if (text?.isBlank() == true) {
                textInputLayout.error = getString(R.string.string_error_message, getString(errorString))
                setByTag(editText.tag as String, false)
            } else {
                textInputLayout.isErrorEnabled = false
                setByTag(editText.tag as String, true)

                if(!text.isNullOrEmpty() && editText.tag.equals(getString(R.string.string_product_price_hint))) {
                    if(text.first() == '0') {
                        textInputLayout.error = getString(R.string.string_error_value_equals_zero)
                        setByTag(editText.tag as String, false)
                    }else {
                        textInputLayout.isErrorEnabled = false
                        setByTag(editText.tag as String, true)
                    }
                }
            }

            activatingButton(product)
        }
    }

    private fun setByTag(tag: String, isOk: Boolean) {
        when (tag) {
            getString(R.string.string_product_name_hint) -> nameOk = isOk
            getString(R.string.string_product_description_hint) -> descriptionOk = isOk
            getString(R.string.string_product_price_hint) -> priceOk = isOk
        }
    }

    private fun activatingButton(product: Products?): Boolean {
        val isOk: Boolean
        if (nameOk && descriptionOk && priceOk && weightOk.isNotEmpty() && gramasOk.isNotEmpty()) {
            binding.btConfirmUpdate.isEnabled = true
            confirmUpdate(product)
            isOk = true
        } else {
            binding.btConfirmUpdate.isEnabled = false
            isOk = false
        }
        return isOk
    }

    private fun initTutorial() {
        val tutorialProduct = TutorialData().setUpNewProduct()
        Glide.with(this).load("").placeholder(R.drawable.logo_feira_dagua_remove).into(binding.ivProductUpdateAndAdd)
        binding.tietDescriptionUpdate.setText(tutorialProduct.description)
        binding.tietProductValueUpdate.setText(tutorialProduct.price.toString())
        binding.tietProductNameUpdate.setText(tutorialProduct.name)

        TapTargetSequence(this).targets(
                TapTarget.forView(
                        binding.ivProductUpdateAndAdd, "Informações Sobre o Produto",
                        "Aqui, você poderá inserir as informações do produto, como foto, nome, valor e uma descrição" +
                                 " sobre o produto que está comercializando."
                )
                        .transparentTarget(true).targetCircleColor(R.color.backgroundColor)
                        .cancelable(false)
                        .descriptionTextColor(R.color.white).titleTextColor(R.color.white)
                        .titleTextSize(20).descriptionTextSize(16).targetRadius(100),
                TapTarget.forView(
                        binding.btConfirmUpdate, "Cadastrar o Produto",
                        "Clicando neste botão, após conferir se as informações do produto estão de acordo, basta clicar" +
                                " nesse botão que seu produto estará disponível em seu perfil para todos os seus clientes."
                )
                        .transparentTarget(true).targetCircleColor(R.color.backgroundColor)
                        .cancelable(false)
                        .descriptionTextColor(R.color.white).titleTextColor(R.color.white)
                        .titleTextSize(20).descriptionTextSize(16).targetRadius(100)
        ).listener(
                object : TapTargetSequence.Listener {
                    override fun onSequenceFinish() {
                        val intent = Intent(this@ProducerUpdateAndAddProductActivity, ProducerMenuActivity::class.java)
                        intent.putExtra(TUTORIAL, true)
                        intent.putExtra(EXTRA_INFOS, 2)
                        startActivity(intent)
                        TutorialPreferences.tutorialPreferencesProducer(this@ProducerUpdateAndAddProductActivity, false, 2)
                        finish()
                    }
                    override fun onSequenceStep(lastTarget: TapTarget?, targetClicked: Boolean) {}
                    override fun onSequenceCanceled(lastTarget: TapTarget?) {}
                }
        ).start()
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        weightOk = parent?.getItemAtPosition(position).toString()
        if (weightOk == "Gramas") {
            binding.sliderWeight.isVisible = true
            sliderListener()
        }else {
            binding.sliderWeight.isVisible = false
            gramasOk = "Null"
        }
    }
    override fun onNothingSelected(parent: AdapterView<*>?) {}

    private fun sliderListener() {
        binding.sliderWeight.addOnChangeListener { slider, value, fromUser ->
            gramasOk = value.toString()
        }
    }
}