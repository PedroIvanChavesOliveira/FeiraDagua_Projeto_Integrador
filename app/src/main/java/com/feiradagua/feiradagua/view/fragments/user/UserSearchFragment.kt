package com.feiradagua.feiradagua.view.fragments.user

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.feiradagua.feiradagua.R
import com.feiradagua.feiradagua.databinding.FragmentUserSearchBinding
import com.feiradagua.feiradagua.utils.Constants.Intents.PRODUCER_ID
import com.feiradagua.feiradagua.view.activitys.user.UserMenuActivity
import com.feiradagua.feiradagua.view.activitys.user.UserStoreInfosActivity
import com.feiradagua.feiradagua.view.adapter.UserSearchMainAdapter

class UserSearchFragment : Fragment() {
    private  lateinit var binding: FragmentUserSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserSearchBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        binding.rvStoreHome.apply {
            UserMenuActivity.PRODUCERS.let {
                layoutManager = LinearLayoutManager(activity)
                adapter = UserSearchMainAdapter(it) {
                    startProducerInfos(it.uid)
                }
            }
        }
    }

    private fun startProducerInfos(id: String) {
        val intent = Intent(activity, UserStoreInfosActivity::class.java)
        intent.putExtra(PRODUCER_ID, id)
        startActivity(intent)
    }
}