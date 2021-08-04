package com.feiradagua.feiradagua.view.fragments.user

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.feiradagua.feiradagua.R
import com.feiradagua.feiradagua.databinding.ActivityExtraInfosProducerBinding
import com.feiradagua.feiradagua.databinding.FragmentUserStoreInfosProductsBinding
import com.feiradagua.feiradagua.model.`class`.Cart
import com.feiradagua.feiradagua.model.`class`.Products
import com.feiradagua.feiradagua.model.`class`.TutorialData
import com.feiradagua.feiradagua.utils.Constants
import com.feiradagua.feiradagua.utils.Constants.Intents.CART_INFO
import com.feiradagua.feiradagua.utils.Constants.Intents.POSITION
import com.feiradagua.feiradagua.utils.Constants.Intents.PRODUCER_ID
import com.feiradagua.feiradagua.utils.Constants.Intents.PRODUCT_INFO
import com.feiradagua.feiradagua.utils.Constants.Intents.TUTORIAL
import com.feiradagua.feiradagua.utils.TutorialPreferences
import com.feiradagua.feiradagua.utils.confirmIfProductExistsInCart
import com.feiradagua.feiradagua.utils.getItemFromIdCart
import com.feiradagua.feiradagua.view.activitys.user.UserMenuActivity
import com.feiradagua.feiradagua.view.activitys.user.UserProductInfoActivity
import com.feiradagua.feiradagua.view.activitys.user.UserStoreInfosActivity
import com.feiradagua.feiradagua.view.adapter.StoreInfosProductMainAdapter
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetSequence
import java.net.URLEncoder

class UserStoreInfosProductsFragment : Fragment() {
    private lateinit var binding: FragmentUserStoreInfosProductsBinding
    private var tutorial: Boolean? = false
    private var getId: String? = ""
    private var producerPhone: String? = ""
    private var producerName: String? = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserStoreInfosProductsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getId = arguments?.getString(PRODUCER_ID)
        tutorial = arguments?.getBoolean(TUTORIAL)

        binding.floatingButtonWhatsApp.setOnClickListener {
            UserMenuActivity.PRODUCERS?.forEach { producer ->
                if(producer.uid == getId) {
                    producerPhone = producer.phone
                    producerName = producer.name
                }
            }
            startWhatsAppChat(producerPhone, producerName)
        }

        if(tutorial == true) {
            context?.let {
                if(TutorialPreferences.getTutorialStatus(it, 3) == true) {
                    initTutorial()
                }
            }
        }else {
            setUpRecyclerView()
        }
    }

    private fun setUpRecyclerView() {
        binding.rvStoreHome.apply {
            UserStoreInfosActivity.PRODUCTS.let {list ->
                list[getId]?.let {
                    layoutManager = LinearLayoutManager(activity)
                    adapter = StoreInfosProductMainAdapter(it) { prod ->
                        if(UserMenuActivity.MY_CART.confirmIfProductExistsInCart(prod.id)) {
                            UserMenuActivity.MY_CART.getItemFromIdCart(prod.id)?.let { item ->
                                startProductInfosActivityInCart(item)
                            }
                        }else {
                            startProductInfosActivity(prod)
                        }
                    }
                }
            }
        }
    }

    private fun startProductInfosActivityInCart(cart: Cart) {
        val intent = Intent(activity, UserProductInfoActivity::class.java)
        intent.putExtra(CART_INFO, cart)
        intent.putExtra(POSITION, 3)
        startActivity(intent)
    }

    private fun startProductInfosActivity(prod: Products) {
        val intent = Intent(activity, UserProductInfoActivity::class.java)
        intent.putExtra(PRODUCT_INFO, prod)
        intent.putExtra(POSITION, 2)
        startActivity(intent)
    }

    private fun startWhatsAppChat(phone: String?, name: String?) {
        phone?.let {
            val contact = "+55 $phone"
            val message = "Olá $name, sou o cliente ${UserMenuActivity.USER.name} e estou entrando em contato " +
                    "a partir do Feira D'água!!"
            val intent = Intent(Intent.ACTION_VIEW)
            val url = "https://api.whatsapp.com/send?phone=$contact&text=${URLEncoder.encode(message,"UTF-8")}"

            intent.data = Uri.parse(url)
            startActivity(intent)
        }
    }

    private fun initTutorial() {
        binding.rvStoreHome.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = StoreInfosProductMainAdapter(TutorialData().setUpProductsRecyclerView()) {}
        }

        TapTargetSequence(activity).targets(
            TapTarget.forView(
                binding.rvStoreHome, "Conferir Produto",
                "Clicando neste cartão, você poderá verificar detalhadamente o produto ofertado!!"
            )
                .transparentTarget(true).targetCircleColor(R.color.backgroundColor)
                .cancelable(false)
                .descriptionTextColor(R.color.white).titleTextColor(R.color.white)
                .titleTextSize(20).descriptionTextSize(16).targetRadius(200)
        ).listener(
            object : TapTargetSequence.Listener {
                override fun onSequenceFinish() {
                    val intent = Intent(activity, UserProductInfoActivity::class.java)
                    intent.putExtra(TUTORIAL, true)
                    context?.let { TutorialPreferences.tutorialPreferences(it, false, 3) }
                    startActivity(intent)
                    activity?.finish()
                }
                override fun onSequenceStep(lastTarget: TapTarget?, targetClicked: Boolean) {}
                override fun onSequenceCanceled(lastTarget: TapTarget?) {}
            }
        ).start()
    }
}