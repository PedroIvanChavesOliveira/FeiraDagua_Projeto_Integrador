package com.feiradagua.feiradagua.view.fragments.user

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.feiradagua.feiradagua.R
import com.feiradagua.feiradagua.databinding.FragmentUserShopCartBinding
import com.feiradagua.feiradagua.model.`class`.Cart
import com.feiradagua.feiradagua.model.`class`.Order
import com.feiradagua.feiradagua.model.`class`.notification.NotificationData
import com.feiradagua.feiradagua.model.`class`.notification.PushNotification
import com.feiradagua.feiradagua.utils.*
import com.feiradagua.feiradagua.utils.Constants.Intents.CART_INFO
import com.feiradagua.feiradagua.utils.Constants.Intents.POSITION
import com.feiradagua.feiradagua.utils.Constants.Notification.TOPIC
import com.feiradagua.feiradagua.utils.getTotalPrice
import com.feiradagua.feiradagua.view.activitys.user.UserMenuActivity
import com.feiradagua.feiradagua.view.activitys.user.UserProductInfoActivity
import com.feiradagua.feiradagua.view.adapter.UserShopCartMainAdapter
import com.feiradagua.feiradagua.viewModel.UserShopCartViewModel
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.messaging.FirebaseMessaging
import java.text.SimpleDateFormat
import java.util.*


class UserShopCartFragment : Fragment() {
    private lateinit var binding: FragmentUserShopCartBinding
    private val idOrder = generateRandomUUID()
    private val viewModelShopCart: UserShopCartViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentUserShopCartBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Se precisar enviar notificações para um grupo x de usuários
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)

        sendOrderToProducer()
        setUpRecyclerView()
    }

    override fun onResume() {
        super.onResume()

        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        if(UserMenuActivity.MY_CART.isNotEmpty()) {
            binding.rvCart.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = UserShopCartMainAdapter(UserMenuActivity.MY_CART, {
                    startProductInfosActivity(it)
                }) {
                    MaterialAlertDialogBuilder(context)
                        .setTitle(resources.getString(R.string.string_dialog_title))
                        .setMessage(resources.getString(R.string.string_dialog_message_cart))
                        .setNeutralButton(resources.getString(R.string.string_dialog_cancel)) { dialog, _ ->
                            dialog.dismiss()
                        }
                        .setNegativeButton(resources.getString(R.string.string_dialog_delete)) { _, _ ->
                            UserMenuActivity.MY_CART.deleteItemFromCartList(it)
                            binding.tvTotalValue.text = UserMenuActivity.MY_CART.getTotalPrice()
                            adapter?.notifyDataSetChanged()
                        }
                        .show()
                }
            }
            binding.tvTotalValue.text = UserMenuActivity.MY_CART.getTotalPrice()
            binding.btFinish.isEnabled = true
            binding.tvOrderNotFound.isVisible = false
            binding.animationCart.isVisible = false
        }else {
            binding.rvCart.isVisible = false
            binding.tvOrderNotFound.isVisible = true
            binding.animationCart.isVisible = true

            val paramsTv = binding.tvTotalValue.layoutParams as ConstraintLayout.LayoutParams
            val paramsBt = binding.btFinish.layoutParams as ConstraintLayout.LayoutParams
            paramsBt.endToEnd = binding.tvOrderNotFound.id
            paramsTv.endToEnd = binding.tvOrderNotFound.id
        }
    }

    private fun sendOrderToProducer() {
        binding.btFinish.setOnClickListener { view ->
            val contraints = CalendarConstraints.Builder().setValidator(DateValidatorPointForward.now()).build()
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setCalendarConstraints(contraints)
                .setTitleText("Selecione uma Data para seu pedido")
                .build()
            activity?.let { activity ->
                datePicker.show(activity.supportFragmentManager, "tag")
                datePicker.addOnPositiveButtonClickListener {
                    val dateString: String = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(datePicker.selection)
                    val order = Order(idOrder, UserMenuActivity.MY_CART.getProductsInfosList(), UserMenuActivity.USER.name?:"",
                        UserMenuActivity.USER.photo?:"", UserMenuActivity.MY_CART.getTotalPriceValue(),
                        dateString,0, UserMenuActivity.MY_CART.getProducersIdsList() ,UserMenuActivity.USER.uid)
                    viewModelShopCart.sendNewOrder(idOrder, order)
                    viewModelShopCart.orderOk.observe(activity) {
                        if(it) {
                            Snackbar.make(view, R.string.string_snackbar_order_sent, Snackbar.LENGTH_SHORT)
                                .setBackgroundTint(ContextCompat.getColor(activity, R.color.white))
                                .setTextColor(ContextCompat.getColor(activity, R.color.textColor))
                                .show()
                        }
                    }
                    UserMenuActivity.PRODUCERS.getProducersToken(UserMenuActivity.MY_CART.getProducersIdsList()).let {
                        it.forEach { token ->
                            token?.let {
                                PushNotification(
                                    NotificationData(getString(R.string.string_title_notification_from_user),
                                        getString(R.string.string_message_notification_from_user,
                                            UserMenuActivity.USER.name)), token).also { not ->
                                    viewModelShopCart.sendNotification(not)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun startProductInfosActivity(prod: Cart) {
        val intent = Intent(activity, UserProductInfoActivity::class.java)
        intent.putExtra(CART_INFO, prod)
        intent.putExtra(POSITION, 3)
        startActivity(intent)
    }
}