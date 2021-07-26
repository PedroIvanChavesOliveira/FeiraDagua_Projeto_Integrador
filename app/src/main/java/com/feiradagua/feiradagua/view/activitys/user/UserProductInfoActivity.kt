package com.feiradagua.feiradagua.view.activitys.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.core.view.marginBottom
import com.bumptech.glide.Glide
import com.feiradagua.feiradagua.R
import com.feiradagua.feiradagua.databinding.ActivityUserProductInfoBinding
import com.feiradagua.feiradagua.model.`class`.Products
import com.feiradagua.feiradagua.utils.Constants.Intents.POSITION
import com.feiradagua.feiradagua.utils.Constants.Intents.PRODUCT_INFO

class UserProductInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserProductInfoBinding
    private var getProductInfos: Products? = null
    private var getOrigin = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProductInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getProductInfos = intent.getParcelableExtra(PRODUCT_INFO)
        getOrigin = intent.getIntExtra(POSITION, 0)

        when(getOrigin) {
            1 -> { setUpProductInfosFromProducer(getProductInfos) }
            2 -> { setUpProductInfosFromUser() }
        }
    }

    private fun setUpProductInfosFromProducer(product: Products?) {
        val params = binding.tvProductValue.layoutParams as ConstraintLayout.LayoutParams
        val margin = binding.tvProductValue.layoutParams as ViewGroup.MarginLayoutParams
        params.bottomToBottom = binding.parentView.id
        params.startToStart = binding.tvProductNameTitle.id
        params.topToBottom = binding.tvProductNameValue.id
        margin.bottomMargin = 20
        binding.tvProductValue.requestLayout()

        Glide.with(this).load(product?.photo).placeholder(R.drawable.logo_feira_dagua_remove).into(binding.ivProduct)
        binding.tvProductNameTitle.text = product?.name
        binding.tvProductNameValue.text = product?.description
        binding.tvProductValue.text = "R$ ${product?.price.toString()}"

        binding.productCountCard.isVisible = false
        binding.btAddToCart.isVisible = false
    }

    private fun setUpProductInfosFromUser() {}
}