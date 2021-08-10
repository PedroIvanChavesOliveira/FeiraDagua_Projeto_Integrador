package com.feiradagua.feiradagua.view.fragments.producer

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.feiradagua.feiradagua.R
import com.feiradagua.feiradagua.databinding.FragmentProducerMyOrdersBinding
import com.feiradagua.feiradagua.model.`class`.Order
import com.feiradagua.feiradagua.model.`class`.TutorialData
import com.feiradagua.feiradagua.utils.Constants
import com.feiradagua.feiradagua.utils.Constants.Intents.EXTRA_INFOS
import com.feiradagua.feiradagua.utils.Constants.Intents.ORDER_DETAILS
import com.feiradagua.feiradagua.utils.Constants.Intents.TUTORIAL
import com.feiradagua.feiradagua.utils.TutorialPreferences
import com.feiradagua.feiradagua.view.activitys.producer.ProducerMenuActivity
import com.feiradagua.feiradagua.view.activitys.producer.ProducerOrderDetailsActivity
import com.feiradagua.feiradagua.view.adapter.ProducerOrdersMainAdapter
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetSequence

class ProducerMyOrdersFragment : Fragment() {
    private lateinit var binding: FragmentProducerMyOrdersBinding
    private var tutorial = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProducerMyOrdersBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tutorial = arguments?.getBoolean(TUTORIAL) == true

        if(tutorial) {
            context?.let {
                if(TutorialPreferences.getTutorialStatusProducer(it, 4) == true) {
                    initTutorial()
                }
            }
        }else {
            setUpOrdersRecyclerView()
        }
    }

    override fun onResume() {
        super.onResume()
        if(!tutorial) {
            setUpOrdersRecyclerView()
        }
    }

    private fun setUpOrdersRecyclerView() {
        ProducerMenuActivity.ORDERS?.let { orders ->
            if(orders.isNotEmpty()) {
                binding.rvStoreHome.apply {
                    layoutManager = LinearLayoutManager(activity)
                    activity?.let {
                        adapter = ProducerOrdersMainAdapter(orders) { orderInfo ->
                            startProducerOrdersDetails(orderInfo)
                        }
                    }
                }
                binding.tvOrderNotFound.isVisible = false
                binding.animationMyOrder.isVisible = false
            }else {
                binding.rvStoreHome.isVisible = false
            }
        }?: run {
            binding.rvStoreHome.isVisible = false
        }
    }

    private fun startProducerOrdersDetails(order: Order) {
        val intent = Intent(activity, ProducerOrderDetailsActivity::class.java)
        intent.putExtra(ORDER_DETAILS, order)
        startActivity(intent)
    }

    private fun initTutorial() {
        val tutorialProduct = TutorialData().setUpOrderRecyclerView()
        binding.animationMyOrder.isVisible = false
        binding.tvOrderNotFound.isVisible = false
        binding.rvStoreHome.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = ProducerOrdersMainAdapter(tutorialProduct) {}
        }

        TapTargetSequence(activity).targets(
                TapTarget.forView(
                        binding.rvStoreHome, "Seus Pedidos",
                        "Nessa seção, você poderá conferir quais são os pedidos feitos pelos clientes, clicando em seu cartão" +
                                " é possível ver detalhadamente a data de entrega, a quantidade e quais são os produtos e por fim" +
                                " poderá optar por rejeitar ou aceitar esse pedido específico."
                )
                        .transparentTarget(true).targetCircleColor(R.color.backgroundColor)
                        .cancelable(false)
                        .descriptionTextColor(R.color.white).titleTextColor(R.color.white)
                        .titleTextSize(20).descriptionTextSize(16).targetRadius(200)
        ).listener(
                object : TapTargetSequence.Listener {
                    override fun onSequenceFinish() {
                        val intent = Intent(activity, ProducerMenuActivity::class.java)
                        intent.putExtra(TUTORIAL, true)
                        intent.putExtra(EXTRA_INFOS, 4)
                        startActivity(intent)
                        context?.let { TutorialPreferences.tutorialPreferencesProducer(it, false, 4) }
                        activity?.finish()
                    }
                    override fun onSequenceStep(lastTarget: TapTarget?, targetClicked: Boolean) {}
                    override fun onSequenceCanceled(lastTarget: TapTarget?) {}
                }
        ).start()
    }
}