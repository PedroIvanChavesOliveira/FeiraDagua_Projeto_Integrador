package com.feiradagua.feiradagua.view.activitys.producer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.core.widget.doBeforeTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.feiradagua.feiradagua.R
import com.feiradagua.feiradagua.databinding.ActivityProducerUpdateAndAddProductBinding
import com.feiradagua.feiradagua.model.`class`.Products
import com.feiradagua.feiradagua.utils.Constants.Intents.POSITION
import com.feiradagua.feiradagua.utils.Constants.Intents.PRODUCT_ID
import com.feiradagua.feiradagua.utils.Constants.Intents.PRODUCT_UPDATE
import com.feiradagua.feiradagua.utils.generateRandomUUID
import com.feiradagua.feiradagua.view.activitys.both.CameraAndGalleryActivity
import com.feiradagua.feiradagua.viewModel.UpdateAndAddProductViewModel
import com.google.android.material.textfield.TextInputLayout

class ProducerUpdateAndAddProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProducerUpdateAndAddProductBinding
    private var viewModelUpdateAndAddProduct = UpdateAndAddProductViewModel()
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

        viewModelUpdateAndAddProduct = ViewModelProvider(this).get(UpdateAndAddProductViewModel::class.java)
        getProducts = intent.getParcelableExtra(PRODUCT_UPDATE)

        getProducts?.let { product ->
            Glide.with(this).load(product.photo).placeholder(R.drawable.logo_feira_dagua_remove).into(binding.ivProductUpdateAndAdd)
            binding.tietProductNameUpdate.setText(product.name)
            binding.tietDescriptionUpdate.setText(product.description)
            binding.tietProductValueUpdate.setText(product.price.toString())

            textChangeUpdate(binding.tietProductNameUpdate, binding.tilProductNameUpdate, R.string.string_product_name_title, product)
            textChangeUpdate(binding.tietDescriptionUpdate, binding.tilDescriptionUpdate, R.string.string_product_description_title_update, product)
            textChangeUpdate(binding.tietProductValueUpdate, binding.tilProductValueUpdate, R.string.string_product_price_title_update, product)

            confirmUpdate(product)
        }?: run {
            binding.btConfirmUpdate.isEnabled = false
            textChangeDefault(binding.tietProductNameUpdate, binding.tilProductNameUpdate, R.string.string_product_name_title)
            textChangeDefault(binding.tietDescriptionUpdate, binding.tilDescriptionUpdate, R.string.string_product_description_title_update)
            textChangeDefault(binding.tietProductValueUpdate, binding.tilProductValueUpdate, R.string.string_product_price_title_update)

            confirmUpdate(null)
        }
        startCameraActivity(getProducts)
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

    private fun confirmUpdate(product: Products?) {
        product?.let {
            binding.btConfirmUpdate.setOnClickListener {
                val setProduct = Products(product.id,
                    binding.tietProductNameUpdate.text.toString(), binding.tietDescriptionUpdate.text.toString(),
                    binding.tietProductValueUpdate.text.toString().toDouble(), ProducerMenuActivity.PRODUCER.uid, photo?:product.photo)
//                    ProducerMenuActivity.PRODUCTS?.forEachIndexed { index, products ->
//                        if(products.id == setProduct.id) {
//                            ProducerMenuActivity.PRODUCTS?.set(index, setProduct)
//                        }
                viewModelUpdateAndAddProduct.addAndUpdateProduct(product.id, setProduct)
                viewModelUpdateAndAddProduct.insertOk.observe(this) {
                    if(it) {
                        finish()
                    }
                }
            }
        }?: run {
            binding.btConfirmUpdate.setOnClickListener {
                val setProduct = Products(id,
                    binding.tietProductNameUpdate.text.toString(), binding.tietDescriptionUpdate.text.toString(),
                    binding.tietProductValueUpdate.text.toString().toDouble(), ProducerMenuActivity.PRODUCER.uid, photo?:"")
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
        if (nameOk && descriptionOk && priceOk) {
            binding.btConfirmUpdate.isEnabled = true
            confirmUpdate(product)
            isOk = true
        } else {
            binding.btConfirmUpdate.isEnabled = false
            isOk = false
        }
        return isOk
    }
}