package com.brins.gpt.fragment

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar.OnMenuItemClickListener
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.brins.gpt.R
import com.brins.gpt.adapter.ChannelAdapter
import com.brins.gpt.databinding.FragmentHomeBinding
import com.brins.gpt.viewmodel.ChatGPTChannelViewModel
import com.brins.gpt.viewmodel.ChatGPTMessageViewModel
import com.brins.lib_base.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import io.getstream.chat.android.ui.widgets.avatar.UserAvatarView
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment : BaseFragment(R.layout.fragment_home) {

    private lateinit var mBinding: FragmentHomeBinding

    private val mViewModel: ChatGPTMessageViewModel by viewModels()

    private val mChannelViewModel: ChatGPTChannelViewModel by viewModels()

    @Inject
    lateinit var mChannelAdapter: ChannelAdapter

    private lateinit var userAvatarView: UserAvatarView
    private lateinit var nameTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = FragmentHomeBinding.bind(view)
        setUpChannelListHeader()
        setUpChannelViews()
        setUpNavigationDrawer()
        observerStateAndEvents()
        mChannelViewModel.fetchUserInfo()
    }

    private fun setUpChannelListHeader() {
        mBinding.channelListHeader.setOnMenuItemClickListener(object : OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem): Boolean {
                when (item.itemId) {
                    com.brins.lib_base.R.id.action_search -> {
                        return true
                    }

                    com.brins.lib_base.R.id.action_more -> {
                        return true
                    }
                }
                return false
            }

        })
        mBinding.channelListHeader.setOnUserAvatarClickListener {
            mBinding.drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    private fun setUpNavigationDrawer() {
        AppBarConfiguration(setOf(R.id.changeColor, R.id.changeLanguage),
            mBinding.drawerLayout)

        mBinding.navigationView.setupWithNavController(findNavController())
        val head = mBinding.navigationView.getHeaderView(0)
        userAvatarView = head.findViewById(R.id.userAvatarView)
        nameTextView = head.findViewById(R.id.nameTextView)
        mBinding.navigationView.setNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.changeColor -> {
                    // todo 更改颜色
                    true
                }
                R.id.changeLanguage -> {
                    // todo 更改语言
                    true
                }
                else -> {
                    false
                }
            }
        }

        mBinding.signOutTextView.setOnClickListener {
            //todo 退出登录
        }
    }



    private fun observerStateAndEvents() {

        mChannelViewModel.userState.observe(viewLifecycleOwner) { state->
            when(state) {
                is ChatGPTChannelViewModel.State.AvailableUser -> {
                    val user = state.availableUsers[0]
                    nameTextView.setText(user.name)
                    userAvatarView.setUser(user)
                    mBinding.channelListHeader.setUser(user)
//                    mChannelAdapter.setUsers(state.availableUsers)
                }
            }
        }

        mChannelViewModel.events.observe(viewLifecycleOwner) { event ->
            when(event) {
                is ChatGPTChannelViewModel.UiEvent.ShowLoading -> {
                    showLoadingDialog()
                }
                is ChatGPTChannelViewModel.UiEvent.UserClicked -> {

                }

                is ChatGPTChannelViewModel.UiEvent.HideLoading -> {
                    hideLoadingDialog()
                }
            }

        }
    }

    private fun setUpChannelViews() {
        with(mBinding.channelList) {
            adapter = mChannelAdapter
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    LinearLayoutManager.VERTICAL,
                ).apply {
                    setDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.common_ui_divider)!!)
                },
            )
        }
    }
}