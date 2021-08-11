package com.feiradagua.feiradagua.view.activitys.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.core.view.marginBottom
import com.bumptech.glide.Glide
import com.feiradagua.feiradagua.R
import com.feiradagua.feiradagua.databinding.ActivityUserProductInfoBinding
import com.feiradagua.feiradagua.model.`class`.Cart
import com.feiradagua.feiradagua.model.`class`.Products
import com.feiradagua.feiradagua.model.`class`.TutorialData
import com.feiradagua.feiradagua.utils.Constants.Intents.CART_INFO
import com.feiradagua.feiradagua.utils.Constants.Intents.POSITION
import com.feiradagua.feiradagua.utils.Constants.Intents.PRODUCT_INFO
import com.feiradagua.feiradagua.utils.Constants.Intents.TUTORIAL
import com.feiradagua.feiradagua.utils.TutorialPreferences
import com.feiradagua.feiradagua.utils.updateCartList
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetSequence

class UserProductInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserProductInfoBinding
    private var tutorial = false
    private var getProductInfos: Products? = null
    private var getCartInfo: Cart? = null
    private var getOrigin = 0
    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProductInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getProductInfos = intent.getParcelableExtra(PRODUCT_INFO)
        getCartInfo = intent.getParcelableExtra(CART_INFO)
        getOrigin = intent.getIntExtra(POSITION, 0)
        tutorial = intent.getBooleanExtra(TUTORIAL, false)

        if(tutorial) {
            if(TutorialPreferences.getTutorialStatus(this, 4) == true) {
                initTutorial()
            }
        }else {
            when(getOrigin) {
                1 -> { setUpProductInfosFromProducer(getProductInfos) }
                2 -> { setUpProductInfosFromUser(getProductInfos) }
                3 -> { setUpProductInfosFromCart(getCartInfo) }
            }

            getCartInfo?.let {
                updateCartProducts(it)
            }?: run {
                savingToTheCart()
            }

            settingCount()
        }
    }

    private fun setUpProductInfosFromCart(cart: Cart?) {
        cart?.let {
            Glide.with(this).load(cart.photo).placeholder(R.drawable.logo_feira_dagua_remove).into(binding.ivProduct)
            binding.tvProductNameTitle.text = cart.name
            binding.tvProductNameValue.text = cart.description
            binding.tvProductValue.text = "R$ %.2f".format(cart.price)
            count = (cart.totalPrice/cart.price).toInt()
            binding.tvProductNumberValue.text = count.toString()
            binding.btAddToCart.isEnabled = true
        }
    }

    private fun updateCartProducts(cart: Cart) {
        binding.btAddToCart.setOnClickListener {
            if((cart.totalPrice/cart.price).toInt() != count) {
                cart.totalPrice = count*cart.price
            }
            UserMenuActivity.MY_CART.updateCartList(cart)
            finish()
        }
    }

    private fun savingToTheCart() {
        binding.btAddToCart.setOnClickListener {
            getProductInfos?.let { product ->
                val cart = Cart(product.id,product.name,product.description,product.price,
                    count*product.price,product.producerId, product.photo)
                UserMenuActivity.MY_CART.add(cart)
                finish()
            }
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
        binding.tvProductValue.text = "R$ %.2f".format(product?.price)

        binding.productCountCard.isVisible = false
        binding.btAddToCart.isVisible = false
    }

    private fun setUpProductInfosFromUser(product: Products?) {
        Glide.with(this).load(product?.photo).placeholder(R.drawable.logo_feira_dagua_remove).into(binding.ivProduct)
        binding.tvProductNameTitle.text = product?.name
        binding.tvProductNameValue.text = product?.description
        binding.tvProductValue.text = "R$ %.2f".format(product?.price)

    }

    private fun settingCount() {
        binding.tbAdd.setOnClickListener {
            count++
            binding.tvProductNumberValue.text = count.toString()
            binding.btAddToCart.isEnabled = count>=1
        }
        binding.tbSub.setOnClickListener {
            if(count>0) {
                count--
            }
            binding.tvProductNumberValue.text = count.toString()
            binding.btAddToCart.isEnabled = count>=1
        }
    }

    private fun initTutorial() {
        val tutorialInfos = TutorialData().setUpCartRecyclerView()
        tutorialInfos.forEach { item ->
            Glide.with(this).load(R.drawable.logo_feira_dagua_remove).into(binding.ivProduct)
            binding.tvProductNameValue.text = item.description
            binding.tvProductNameTitle.text = item.name
            binding.tvProductNumberValue.text = "2"
            binding.tvProductValue.text = "R$ %.2f".format(item.price)
        }

        TapTargetSequence(this).targets(
            TapTarget.forView(
                binding.ivProduct, "Informações do Produto",
                "Aqui se encontram as principais informações desse produto!!"
            )
                .transparentTarget(true).targetCircleColor(R.color.backgroundColor)
                .cancelable(false)
                .descriptionTextColor(R.color.white).titleTextColor(R.color.white)
                .titleTextSize(20).descriptionTextSize(16).targetRadius(200),
            TapTarget.forView(
                binding.productCountCard, "Selecionar a Quantidade",
                "Aqui é o local que você poderá escolher a quantidade desse produto você quer comprar!!"
            )
                .transparentTarget(true).targetCircleColor(R.color.backgroundColor)
                .cancelable(false)
                .descriptionTextColor(R.color.white).titleTextColor(R.color.white)
                .titleTextSize(20).descriptionTextSize(16).targetRadius(100),
            TapTarget.forView(
                binding.btAddToCart, "Adicionar ao Carrinho",
                "Apenas com um clique, você irá adicionar esse produto ao seu tão precioso carrinho!!"
            )
                .transparentTarget(true).targetCircleColor(R.color.backgroundColor)
                .cancelable(false)
                .descriptionTextColor(R.color.white).titleTextColor(R.color.white)
                .titleTextSize(20).descriptionTextSize(16).targetRadius(100)
        ).listener(
            object : TapTargetSequence.Listener {
                override fun onSequenceFinish() {
                    val intent = Intent(this@UserProductInfoActivity, UserMenuActivity::class.java)
                    intent.putExtra(TUTORIAL, true)
                    intent.putExtra(POSITION, 1)
                    TutorialPreferences.tutorialPreferences(this@UserProductInfoActivity, false, 4)
                    startActivity(intent)
                    finish()
                }
                override fun onSequenceStep(lastTarget: TapTarget?, targetClicked: Boolean) {}
                override fun onSequenceCanceled(lastTarget: TapTarget?) {}
            }
        ).start()
    }
}