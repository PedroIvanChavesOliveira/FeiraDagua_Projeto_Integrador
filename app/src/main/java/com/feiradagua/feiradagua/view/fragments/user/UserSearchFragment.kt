package com.feiradagua.feiradagua.view.fragments.user

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.feiradagua.feiradagua.R
import com.feiradagua.feiradagua.databinding.FragmentUserSearchBinding
import com.feiradagua.feiradagua.model.`class`.Producer
import com.feiradagua.feiradagua.model.`class`.TutorialData
import com.feiradagua.feiradagua.utils.Constants.Intents.PRODUCER_ID
import com.feiradagua.feiradagua.utils.Constants.Intents.TUTORIAL
import com.feiradagua.feiradagua.view.activitys.user.UserMenuActivity
import com.feiradagua.feiradagua.view.activitys.user.UserStoreInfosActivity
import com.feiradagua.feiradagua.view.adapter.UserSearchMainAdapter
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetSequence
import com.getkeepsafe.taptargetview.TapTargetView
import java.util.*
import kotlin.collections.ArrayList

class UserSearchFragment : Fragment() {
    private  lateinit var binding: FragmentUserSearchBinding
    private var tutorial = false
    private var start = 0
    private val setUpMicrophone = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            val getString = intent?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            getString?.let {
                if(getString.isNotEmpty()) {
                    val query = getString[0]
                    binding.tietSearchStore.setText(query.toLowerCase(Locale.ROOT))
                }
            }
        }
    }

    companion object {
        var VALUE_TEXT: String = ""
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserSearchBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tutorial = arguments?.getBoolean(TUTORIAL) == true

        if(tutorial) {
            initTutorial()
        }else {
            if(start == 0) {
                setUpRecyclerView(null)
                start++
            }

            binding.tilSearchStore.setEndIconOnClickListener {
                openActivityForResult()
            }

            searchProducer()
        }
    }

    override fun onResume() {
        super.onResume()
        binding.tietSearchStore.setText(VALUE_TEXT)
    }

    private fun openActivityForResult() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Pesquisando...")
        setUpMicrophone.launch(intent)
    }

    private fun setUpRecyclerView(text: String?) {
        when(filteredList(UserMenuActivity.PRODUCERS, text).size) {
            0 -> {
                binding.rvStoreHome.isVisible = false
                binding.animationSearch.isVisible = true
                binding.tvProducerNotFound.isVisible = true
            }
            else -> {
                binding.rvStoreHome.isVisible = true
                binding.rvStoreHome.apply {
                    layoutManager = LinearLayoutManager(activity)
                    adapter = UserSearchMainAdapter(filteredList(UserMenuActivity.PRODUCERS, text)) {
                        startProducerInfos(it.uid)
                    }
                    binding.animationSearch.isVisible = false
                    binding.tvProducerNotFound.isVisible = false
                }
            }
        }
    }

    private fun searchProducer() {
        binding.tietSearchStore.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                setUpRecyclerView(s.toString())
                VALUE_TEXT = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun filteredList(producers: MutableList<Producer>, text: String?): MutableList<Producer> {
        val filteredList = ArrayList<Producer>()
        return if(text.isNullOrBlank()) {
            producers
        }else {
            for(i in producers) {
                i.name?.let {
                    if(it.toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT))) {
                        filteredList.add(i)
                    }
                }
            }
            filteredList
        }
    }

    private fun startProducerInfos(id: String) {
        val intent = Intent(activity, UserStoreInfosActivity::class.java)
        intent.putExtra(PRODUCER_ID, id)
        startActivity(intent)
    }

    private fun initTutorial() {
        binding.animationSearch.isVisible = false
        binding.tvProducerNotFound.isVisible = false
        binding.rvStoreHome.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = UserSearchMainAdapter(TutorialData().setUpProducerRecyclerView()) {}
        }
        TapTargetSequence(activity).targets(
            TapTarget.forView(
                binding.tilSearchStore, "Buscar por um Produtor",
                "Nesse campo você poderá fazer uma busca pelo nome dos produtores que estão à sua disposição"
            )
                .transparentTarget(true).targetCircleColor(R.color.backgroundColor)
                .cancelable(false)
                .descriptionTextColor(R.color.white).titleTextColor(R.color.white)
                .titleTextSize(20).descriptionTextSize(16).targetRadius(100),
            TapTarget.forView(
                binding.rvStoreHome, "Produtores A Vista!",
                "Aqui será mostrado os produtores que estarão por perto, clicando em seu cartão você conseguirá" +
                        "visualizar mais algumas informações. Vamos lá ?!"
            )
                .transparentTarget(true).targetCircleColor(R.color.backgroundColor)
                .cancelable(false)
                .descriptionTextColor(R.color.white).titleTextColor(R.color.white)
                .titleTextSize(20).descriptionTextSize(16).targetRadius(200)
        ).listener(
            object : TapTargetSequence.Listener {
                override fun onSequenceFinish() {
                    val intent = Intent(activity, UserStoreInfosActivity::class.java)
                    intent.putExtra(TUTORIAL, true)
                    startActivity(intent)
//                    val frag = activity?.supportFragmentManager?.beginTransaction()
//                    val bundle = Bundle()
//                    bundle.putBoolean(TUTORIAL, true)
//                    this@UserShopCartFragment.arguments = bundle
//                    frag?.replace(R.id.flContainerHomeUserActivity, UserProfileFragment())
//                    frag?.commit()
                }
                override fun onSequenceStep(lastTarget: TapTarget?, targetClicked: Boolean) {}
                override fun onSequenceCanceled(lastTarget: TapTarget?) {}
            }
        ).start()
    }
}