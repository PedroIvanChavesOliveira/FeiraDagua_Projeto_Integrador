package com.feiradagua.feiradagua.view.activitys.producer

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.feiradagua.feiradagua.R
import com.feiradagua.feiradagua.databinding.ActivityProducerOrderDetailsBinding
import com.feiradagua.feiradagua.model.`class`.Order
import com.feiradagua.feiradagua.model.`class`.Products
import com.feiradagua.feiradagua.model.`class`.User
import com.feiradagua.feiradagua.model.`class`.notification.NotificationData
import com.feiradagua.feiradagua.model.`class`.notification.PushNotification
import com.feiradagua.feiradagua.utils.Constants.Intents.ORDER_DETAILS
import com.feiradagua.feiradagua.utils.getOrderProducts
import com.feiradagua.feiradagua.utils.getTotalPriceOfProduct
import com.feiradagua.feiradagua.view.activitys.user.UserMenuActivity
import com.feiradagua.feiradagua.view.adapter.ProducerOrderDetailsMainAdapter
import com.feiradagua.feiradagua.viewModel.OrderDetailsViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.NonCancellable.cancel
import java.net.URLEncoder

class ProducerOrderDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProducerOrderDetailsBinding
    private val viewModelOrderDetails: OrderDetailsViewModel by viewModels()
    var getOrderInfos: Order? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProducerOrderDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getOrderInfos = intent.getParcelableExtra(ORDER_DETAILS)

        getOrderInfos?.let {order ->
            viewModelOrderDetails.getUserDataById(order.userId)
            viewModelOrderDetails.userData.observe(this) { user ->
                user?.let {
                    confirmOrder(it, order)
                    setUpOrderDetails(order)
                }
            }
        }
    }

    private fun confirmOrder(user: User, order: Order) {
        binding.btAcceptOrder.setOnClickListener {
            user.token?.let { token ->
                PushNotification(
                    NotificationData(
                        getString(R.string.string_title_notification_from_producer_accept),
                        getString(R.string.string_message_notification_from_producer_accept)
                    ), token).also {
                    viewModelOrderDetails.sendNotification(it)
                    viewModelOrderDetails.updateOrderConfirmation(order.id, 1)
                    viewModelOrderDetails.updateOk.observe(this) {
                        if(it) {
                            startWhatsAppChat(user.phone, user.name)
                            finish()
                        }
                    }
                }
            }
        }

        binding.btDeclineOrder.setOnClickListener {
            user.token?.let { token ->
                val singleItems = resources.getStringArray(R.array.string_array_cancel_order_options)
                val checkedItem = 0
                var messageSelected = singleItems[checkedItem]
                MaterialAlertDialogBuilder(this, R.style.AlertDialogCustom)
                    .setTitle(resources.getString(R.string.string_dialog_title_order))
                    .setNeutralButton(resources.getString(R.string.string_dialog_cancel)) { dialog, which ->
                        dialog.dismiss()
                    }
                    .setSingleChoiceItems(singleItems, checkedItem) { dialog, which ->
                        messageSelected = singleItems[which]
                    }
                    .setPositiveButton(resources.getString(R.string.string_dialog_send)) { dialog, which ->
                        PushNotification(
                            NotificationData(
                                getString(R.string.string_title_notification_from_producer_decline),
                                getString(
                                    R.string.string_message_notification_from_producer_decline,
                                    messageSelected)
                            ), token).also {
                            viewModelOrderDetails.sendNotification(it)
                            viewModelOrderDetails.updateOrderConfirmation(order.id, -1)
                            viewModelOrderDetails.updateOk.observe(this) {
                                if(it) {
                                    finish()
                                }
                            }
                        }
                    }
                .show()
            }
        }
    }

    private fun startWhatsAppChat(phone: String?, name: String?) {
        phone?.let {
            val contact = "+55 $phone"
            val message = "Olá ${name?:"Nome não encontrado"}, sou o produtor ${ProducerMenuActivity.PRODUCER.name} e estou entrando em contato " +
                    "a partir do Feira D'água!! Vamos conversar sobre o seu pedido!!"
            val intent = Intent(Intent.ACTION_VIEW)
            val url = "https://api.whatsapp.com/send?phone=$contact&text=${URLEncoder.encode(message,"UTF-8")}"

            intent.data = Uri.parse(url)
            startActivity(intent)
        }
    }

    private fun setUpOrderDetails(order: Order) {
        val productsInOrder: MutableList<Products> = order.getOrderProducts(ProducerMenuActivity.PRODUCTS)
        val priceOfEachProduct: MutableMap<String, Double> = order.getTotalPriceOfProduct()

        Glide.with(this).load(order.userPhoto).placeholder(R.drawable.logo_feira_dagua_remove).into(binding.ivClient)
        binding.tvClientValue.text = order.username
        binding.tvDateValueOrderDetails.text = order.deliveryDate
        binding.tvTotalValueOrderDetails.text = "R$ ${order.totalPrice}"
        binding.rvOrderDetails.apply {
            layoutManager = LinearLayoutManager(this@ProducerOrderDetailsActivity)
            adapter = ProducerOrderDetailsMainAdapter(productsInOrder, priceOfEachProduct)
        }
    }
}