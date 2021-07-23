package com.feiradagua.feiradagua.view.fragments.producer

import android.content.Intent
import android.os.Bundle
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
import com.feiradagua.feiradagua.utils.Constants
import com.feiradagua.feiradagua.view.activitys.producer.ProducerMenuActivity
import com.feiradagua.feiradagua.view.activitys.producer.ProducerUpdateAndAddProductActivity
import com.feiradagua.feiradagua.view.activitys.user.UserProductInfoActivity
import com.feiradagua.feiradagua.view.adapter.ProducerNewProductMainAdapter
import com.feiradagua.feiradagua.viewModel.NewProductViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class ProducerNewProductFragment : Fragment() {
    private lateinit var binding: FragmentProducerNewProductBinding
    private val viewModelNewProduct: NewProductViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentProducerNewProductBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btNewProduct.setOnClickListener {
            startUpdateProductActivity(null)
        }

        setUpRecyclerView()
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
                                viewModelNewProduct.deleteById(product.id)
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
}