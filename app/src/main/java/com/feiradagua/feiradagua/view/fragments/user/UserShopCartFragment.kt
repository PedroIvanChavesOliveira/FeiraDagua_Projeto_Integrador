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
import com.feiradagua.feiradagua.model.`class`.TutorialData
import com.feiradagua.feiradagua.model.`class`.notification.NotificationData
import com.feiradagua.feiradagua.model.`class`.notification.PushNotification
import com.feiradagua.feiradagua.utils.*
import com.feiradagua.feiradagua.utils.Constants.Intents.CART_INFO
import com.feiradagua.feiradagua.utils.Constants.Intents.POSITION
import com.feiradagua.feiradagua.utils.Constants.Intents.TUTORIAL
import com.feiradagua.feiradagua.utils.Constants.Notification.TOPIC
import com.feiradagua.feiradagua.utils.getTotalPrice
import com.feiradagua.feiradagua.view.activitys.user.UserMenuActivity
import com.feiradagua.feiradagua.view.activitys.user.UserProductInfoActivity
import com.feiradagua.feiradagua.view.adapter.UserShopCartMainAdapter
import com.feiradagua.feiradagua.viewModel.UserShopCartViewModel
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetSequence
import com.getkeepsafe.taptargetview.TapTargetView
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
    private var tutorial = false
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
//        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)
        tutorial = arguments?.getBoolean(TUTORIAL) == true

        if(tutorial) {
            context?.let {
                if(TutorialPreferences.getTutorialStatus(it, 5) == true) {
                    initTutorial()
                }
            }
        }else {
            sendOrderToProducer()
            setUpRecyclerView()
        }
    }

    override fun onResume() {
        super.onResume()
        if(!tutorial) {
            setUpRecyclerView()
        }
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
                            if(UserMenuActivity.MY_CART.isEmpty()) {
                                binding.tvOrderNotFound.isVisible = true
                                binding.animationCart.isVisible = true
                                binding.btFinish.isEnabled = false
                            }
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
            binding.btFinish.isEnabled = false

            val paramsTv = binding.tvTotalValue.layoutParams as ConstraintLayout.LayoutParams
            val paramsBt = binding.btFinish.layoutParams as ConstraintLayout.LayoutParams
            paramsBt.endToEnd = binding.tvOrderNotFound.id
            paramsTv.endToEnd = binding.tvOrderNotFound.id
        }
    }

    private fun finishOrder() {
        binding.rvCart.isVisible = false
        binding.tvOrderNotFound.isVisible = true
        binding.animationCart.isVisible = true
        binding.btFinish.isEnabled = false
        binding.tvTotalValue.text = "R$ 00,00"

        val paramsTv = binding.tvTotalValue.layoutParams as ConstraintLayout.LayoutParams
        val paramsBt = binding.btFinish.layoutParams as ConstraintLayout.LayoutParams
        paramsBt.endToEnd = binding.tvOrderNotFound.id
        paramsTv.endToEnd = binding.tvOrderNotFound.id
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
                    val currentDate = 86400000 + it
                    val dateString: String = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(currentDate)
                    UserMenuActivity.MY_CART.getProducersIdsList().forEach {
                        val idOrder = generateRandomUUID()
                        val productsOrder = UserMenuActivity.MY_CART.getProductsInfosList(it)
                        val order = Order(idOrder, productsOrder, UserMenuActivity.USER.name?:"",
                                UserMenuActivity.USER.photo?:"", productsOrder.getTotalPriceValue(),
                                dateString,0, it, UserMenuActivity.USER.uid)
                        viewModelShopCart.sendNewOrder(idOrder, order)
                    }
                    viewModelShopCart.orderOk.observe(activity) {
                        if(it) {
                            Snackbar.make(view, R.string.string_snackbar_order_sent, Snackbar.LENGTH_SHORT)
                                .setBackgroundTint(ContextCompat.getColor(activity, R.color.white))
                                .setTextColor(ContextCompat.getColor(activity, R.color.textColor))
                                .show()
                        }
                        UserMenuActivity.PRODUCERS?.getProducersToken(UserMenuActivity.MY_CART.getProducersIdsList()).let {
                            it?.forEach { token ->
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
                        UserMenuActivity.MY_CART.clear()
                        finishOrder()
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

    private fun initTutorial() {
        binding.tvOrderNotFound.isVisible = false
        binding.animationCart.isVisible = false
        binding.rvCart.isVisible = true
        binding.btFinish.isEnabled = true
        binding.rvCart.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = UserShopCartMainAdapter(TutorialData().setUpCartRecyclerView(), {}) {}
        }
        binding.tvTotalValue.text = "R$ 37,00"
        TapTargetSequence(activity).targets(
            TapTarget.forView(
                binding.tvYourCartTitle, "Seu Carrinho",
                "Este é o seu carrinho, onde você poderá ver os produtos que você selecionou para efetuar a compra!!"
            )
                .transparentTarget(true).targetCircleColor(R.color.backgroundColor)
                .cancelable(false)
                .descriptionTextColor(R.color.white).titleTextColor(R.color.white)
                .titleTextSize(20).descriptionTextSize(16).targetRadius(100),
            TapTarget.forView(
                binding.rvCart, "Produto Escolhido",
                "Aqui você terá algumas informações do produto escolhido, caso queira editá-lo (adicionar ou remover itens)" +
                        " basta clicar no cartão ou se simplesmente desistiu da compra deste produto é so apagar clicando na lixeirinha ao lado!!"
            )
                .transparentTarget(true).targetCircleColor(R.color.backgroundColor)
                .cancelable(false)
                .descriptionTextColor(R.color.white).titleTextColor(R.color.white)
                .titleTextSize(20).descriptionTextSize(16).targetRadius(200),
            TapTarget.forView(
                binding.btFinish, "Finalizar o Pedido",
                "Com tudo nos conformes, você poderá finalizar o seu pedido, escolhendo uma data para receber a sua Feira D'água" +
                        " e então partir para o abraço!!"
            )
                .transparentTarget(true).targetCircleColor(R.color.backgroundColor)
                .cancelable(false)
                .descriptionTextColor(R.color.white).titleTextColor(R.color.white)
                .titleTextSize(20).descriptionTextSize(16).targetRadius(100)
        ).listener(
            object : TapTargetSequence.Listener {
                override fun onSequenceFinish() {
                    val intent = Intent(activity, UserMenuActivity::class.java)
                    intent.putExtra(TUTORIAL, true)
                    intent.putExtra(POSITION, 2)
                    context?.let { TutorialPreferences.tutorialPreferences(it, false, 5) }
                    startActivity(intent)
                    activity?.finish()
                }
                override fun onSequenceStep(lastTarget: TapTarget?, targetClicked: Boolean) {}
                override fun onSequenceCanceled(lastTarget: TapTarget?) {}
            }
        ).start()
    }
}
