package com.feiradagua.feiradagua.view.fragments.user

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.feiradagua.feiradagua.R
import com.feiradagua.feiradagua.databinding.ActivityExtraInfosProducerBinding
import com.feiradagua.feiradagua.databinding.FragmentUserStoreInfosProductsBinding
import com.feiradagua.feiradagua.model.`class`.Cart
import com.feiradagua.feiradagua.model.`class`.Products
import com.feiradagua.feiradagua.utils.Constants
import com.feiradagua.feiradagua.utils.Constants.Intents.CART_INFO
import com.feiradagua.feiradagua.utils.Constants.Intents.POSITION
import com.feiradagua.feiradagua.utils.Constants.Intents.PRODUCER_ID
import com.feiradagua.feiradagua.utils.Constants.Intents.PRODUCT_INFO
import com.feiradagua.feiradagua.utils.confirmIfProductExistsInCart
import com.feiradagua.feiradagua.utils.getItemFromIdCart
import com.feiradagua.feiradagua.view.activitys.user.UserMenuActivity
import com.feiradagua.feiradagua.view.activitys.user.UserProductInfoActivity
import com.feiradagua.feiradagua.view.activitys.user.UserStoreInfosActivity
import com.feiradagua.feiradagua.view.adapter.StoreInfosProductMainAdapter
import java.net.URLEncoder

class UserStoreInfosProductsFragment : Fragment() {
    private lateinit var binding: FragmentUserStoreInfosProductsBinding
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

        binding.floatingButtonWhatsApp.setOnClickListener {
            UserMenuActivity.PRODUCERS.forEach { producer ->
                if(producer.uid == getId) {
                    producerPhone = producer.phone
                    producerName = producer.name
                }
            }
            startWhatsAppChat(producerPhone, producerName)
        }

        setUpRecyclerView()
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
}