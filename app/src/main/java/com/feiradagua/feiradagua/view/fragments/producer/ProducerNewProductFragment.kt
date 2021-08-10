package com.feiradagua.feiradagua.view.fragments.producer

import android.content.Intent
import android.os.Bundle
import android.provider.DocumentsContract.EXTRA_INFO
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.feiradagua.feiradagua.R
import com.feiradagua.feiradagua.databinding.FragmentProducerMyOrdersBinding
import com.feiradagua.feiradagua.databinding.FragmentProducerNewProductBinding
import com.feiradagua.feiradagua.model.`class`.Products
import com.feiradagua.feiradagua.model.`class`.TutorialData
import com.feiradagua.feiradagua.utils.Constants
import com.feiradagua.feiradagua.utils.Constants.Intents.EXTRA_INFOS
import com.feiradagua.feiradagua.utils.Constants.Intents.TUTORIAL
import com.feiradagua.feiradagua.utils.TutorialPreferences
import com.feiradagua.feiradagua.view.activitys.producer.ProducerMenuActivity
import com.feiradagua.feiradagua.view.activitys.producer.ProducerUpdateAndAddProductActivity
import com.feiradagua.feiradagua.view.activitys.user.UserProductInfoActivity
import com.feiradagua.feiradagua.view.activitys.user.UserStoreInfosActivity
import com.feiradagua.feiradagua.view.adapter.ProducerNewProductMainAdapter
import com.feiradagua.feiradagua.viewModel.NewProductViewModel
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetSequence
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class ProducerNewProductFragment : Fragment() {
    private lateinit var binding: FragmentProducerNewProductBinding
    private val viewModelNewProduct: NewProductViewModel by activityViewModels()
    private var tutorial = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentProducerNewProductBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tutorial = arguments?.getBoolean(TUTORIAL) == true
        binding.btNewProduct.setOnClickListener {
            startUpdateProductActivity(null)
        }

        if(tutorial) {
            context?.let {
                if(TutorialPreferences.getTutorialStatusProducer(it, 1) == true) {
                    initTutorial(1)
                }else if(TutorialPreferences.getTutorialStatusProducer(it, 3) == true) {
                    initTutorial(2)
                }
            }
        }else {
            setUpRecyclerView()
        }
    }

    override fun onResume() {
        super.onResume()
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        ProducerMenuActivity.PRODUCTS?.let { products ->
            if(products.isNotEmpty()) {
                binding.rvNewProducts.apply {
                    layoutManager = LinearLayoutManager(activity)
                    adapter = ProducerNewProductMainAdapter(products, {
                        startProductInfoActivity(it)
                    }, {product ->
                        MaterialAlertDialogBuilder(context)
                            .setTitle(resources.getString(R.string.string_dialog_title))
                            .setMessage(resources.getString(R.string.string_dialog_message))
                            .setNeutralButton(resources.getString(R.string.string_dialog_cancel)) { dialog, _ ->
                                dialog.dismiss()
                            }
                            .setNegativeButton(resources.getString(R.string.string_dialog_delete)) { _, _ ->
                                viewModelNewProduct.deleteById(product.id, product.photo)
                                viewModelNewProduct.deleteDone.observe(viewLifecycleOwner) {
                                    if(it) {
                                        Snackbar.make(this, R.string.string_snackbar_delete_done, Snackbar.LENGTH_SHORT)
                                            .setBackgroundTint(ContextCompat.getColor(context, R.color.colorPrimary))
                                            .setTextColor(ContextCompat.getColor(context, R.color.white))
                                            .show()
                                        adapter?.notifyDataSetChanged()
                                    }
                                }
                            }
                            .show()
                    }, {
                        startUpdateProductActivity(it)
                    })
                }
                binding.tvProductNotFound.isVisible = false
                binding.animationProduct.isVisible = false
            }else {
                binding.rvNewProducts.isVisible = false
            }
        }
    }

    private fun startProductInfoActivity(product: Products) {
        val intent = Intent(activity, UserProductInfoActivity::class.java)
        intent.putExtra(Constants.Intents.PRODUCT_INFO, product)
        intent.putExtra(Constants.Intents.POSITION, 1)
        startActivity(intent)
    }

    private fun startUpdateProductActivity(product: Products?) {
        val intent = Intent(activity, ProducerUpdateAndAddProductActivity::class.java)
        intent.putExtra(Constants.Intents.PRODUCT_UPDATE, product)
        startActivity(intent)
    }

    private fun initTutorial(pos: Int) {
        when(pos) {
            1 -> {
                TapTargetSequence(activity).targets(
                        TapTarget.forView(
                                binding.btNewProduct, "Adicionar um Novo Produto",
                                "Bem-vindo ao Feira D'água!! \nClicando neste botão você poderá adicionar todos os produtos" +
                                        " de seu catálogo. Clique para conferir!!"
                        )
                                .transparentTarget(true).targetCircleColor(R.color.backgroundColor)
                                .cancelable(false)
                                .descriptionTextColor(R.color.white).titleTextColor(R.color.white)
                                .titleTextSize(20).descriptionTextSize(16).targetRadius(100)
                ).listener(
                        object : TapTargetSequence.Listener {
                            override fun onSequenceFinish() {
                                val intent = Intent(activity, ProducerUpdateAndAddProductActivity::class.java)
                                intent.putExtra(TUTORIAL, true)
                                startActivity(intent)
                                context?.let { TutorialPreferences.tutorialPreferencesProducer(it, false, 1) }
                                activity?.finish()
                            }
                            override fun onSequenceStep(lastTarget: TapTarget?, targetClicked: Boolean) {}
                            override fun onSequenceCanceled(lastTarget: TapTarget?) {}
                        }
                ).start()
            }
            2 -> {
                val tutorial = TutorialData().setUpNewProduct()
                val list = mutableListOf(tutorial)
                binding.animationProduct.isVisible = false
                binding.tvProductNotFound.isVisible = false
                binding.rvNewProducts.isVisible = true
                binding.rvNewProducts.apply {
                    layoutManager = LinearLayoutManager(activity)
                    adapter = ProducerNewProductMainAdapter(list,{},{},{})
                }

                TapTargetSequence(activity).targets(
                        TapTarget.forView(
                                binding.rvNewProducts, "Seus Produtos",
                                "Aqui estará a lista de produtos que você cadastrou!! Clicando na lixeira ao canto" +
                                        " você poderá fazer a exclusão do produto, ao clicar no lápis, você poderá fazer a atualização" +
                                        " desse produto e clicando no cartão do produto, você será direcionado para as informações gerais " +
                                        " de seu produto, da forma que o cliente verá!!")
                                .transparentTarget(true).targetCircleColor(R.color.backgroundColor)
                                .cancelable(false)
                                .descriptionTextColor(R.color.white).titleTextColor(R.color.white)
                                .titleTextSize(20).descriptionTextSize(16).targetRadius(100),
                ).listener(
                        object : TapTargetSequence.Listener {
                            override fun onSequenceFinish() {
                                val intent = Intent(activity, ProducerMenuActivity::class.java)
                                intent.putExtra(TUTORIAL, true)
                                intent.putExtra(EXTRA_INFOS, 3)
                                startActivity(intent)
                                context?.let { TutorialPreferences.tutorialPreferencesProducer(it, false, 3) }
                                activity?.finish()
                            }
                            override fun onSequenceStep(lastTarget: TapTarget?, targetClicked: Boolean) {}
                            override fun onSequenceCanceled(lastTarget: TapTarget?) {}
                        }
                ).start()
            }
        }
    }
}