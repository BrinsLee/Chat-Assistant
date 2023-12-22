package com.brins.gpt.fragment

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.brins.gpt.R
import com.brins.gpt.adapter.ChannelAdapter
import com.brins.gpt.databinding.FragmentHomeBinding
import com.brins.gpt.di.GPTChannelViewModelFactory
import com.brins.gpt.extensions.bindsView
import com.brins.gpt.viewmodel.ChatGPTChannelViewModel
import com.brins.gpt.viewmodel.ChatGPTMessageViewModel
import com.brins.gpt.viewmodel.ChatGPTUserInfoViewModel
import com.brins.lib_base.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import io.getstream.chat.android.ui.viewmodel.channels.ChannelListHeaderViewModel
import io.getstream.chat.android.ui.viewmodel.channels.ChannelListViewModel
import io.getstream.chat.android.ui.widgets.avatar.UserAvatarView
import io.getstream.log.streamLog
import kotlinx.coroutines.flow.filterNotNull
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment : BaseFragment(R.layout.fragment_home) {

    private lateinit var mBinding: FragmentHomeBinding

    private val channelListHeaderViewModel: ChannelListHeaderViewModel by viewModels()


    private val mUserInfoViewModel: ChatGPTUserInfoViewModel by viewModels()

    private val channelViewModelFactory: GPTChannelViewModelFactory = GPTChannelViewModelFactory(
        null, ChannelListViewModel.DEFAULT_SORT,
        ChatGPTChannelViewModel.DEFAULT_CHANNEL_LIMIT,
        ChatGPTChannelViewModel.DEFAULT_MESSAGE_LIMIT,
        ChatGPTChannelViewModel.DEFAULT_MEMBER_LIMIT)


    private val mChannelViewModel: ChatGPTChannelViewModel by viewModels { channelViewModelFactory }


    @Inject
    lateinit var mChannelAdapter: ChannelAdapter

    private lateinit var userAvatarView: UserAvatarView
    private lateinit var nameTextView: TextView

    companion object {
        val TAG = HomeFragment::class.simpleName
    }

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
        mUserInfoViewModel.fetchUserInfo()
    }

    private fun setUpChannelListHeader() {
        with(mBinding.channelListHeader) {
            channelListHeaderViewModel.bindsView(this, viewLifecycleOwner)
            setOnMenuItemClickListener(object :
                Toolbar.OnMenuItemClickListener {
                override fun onMenuItemClick(item: MenuItem): Boolean {
                    when (item.itemId) {
                        com.brins.lib_base.R.id.action_search -> {
                            return true
                        }

                        com.brins.lib_base.R.id.action_add -> {
//                            mChannelViewModel.
                            return true
                        }

                        com.brins.lib_base.R.id.action_gpt_3_5 -> {
                            mChannelViewModel.handleEvents(ChatGPTChannelViewModel.GPTChannelEvent.CreateChannelEvent3_5())
                            return true
                        }

                        com.brins.lib_base.R.id.action_gpt_4 -> {
                            mChannelViewModel.handleEvents(ChatGPTChannelViewModel.GPTChannelEvent.CreateChannelEvent4())
                            return true
                        }
                    }
                    return false
                }

            })
            setOnUserAvatarClickListener {
                mBinding.drawerLayout.openDrawer(GravityCompat.START)
            }
        }

    }

    private fun setUpNavigationDrawer() {
        AppBarConfiguration(
            setOf(R.id.changeColor, R.id.changeLanguage), mBinding.drawerLayout
        )

        mBinding.navigationView.setupWithNavController(findNavController())
        val head = mBinding.navigationView.getHeaderView(0)
        userAvatarView = head.findViewById(R.id.userAvatarView)
        nameTextView = head.findViewById(R.id.nameTextView)
        mBinding.navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
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

        mUserInfoViewModel.userState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ChatGPTUserInfoViewModel.State.AvailableUser -> {
                    val user = state.availableUsers[0]
                    nameTextView.setText(user.name)
                    userAvatarView.setUser(user)
//                    mBinding.channelListHeader.setUser(user)
//                    mChannelAdapter.setUsers(state.availableUsers)
                }
            }
        }

        mUserInfoViewModel.events.observe(viewLifecycleOwner) { event ->
            when (event) {
                is ChatGPTUserInfoViewModel.UiEvent.ShowLoading -> {
                    showLoadingDialog()
                }

                is ChatGPTUserInfoViewModel.UiEvent.UserClicked -> {

                }

                is ChatGPTUserInfoViewModel.UiEvent.HideLoading -> {
                    hideLoadingDialog()
                }
            }

        }

        mChannelViewModel.channelUiState.filterNotNull().asLiveData().observe(viewLifecycleOwner) { data ->
            streamLog(tag = TAG) {
                data.toString()
            }



        }
    }

    private fun setUpChannelViews() {
        with(mBinding.channelList) {
            mChannelViewModel.bindView(this, viewLifecycleOwner)
            setChannelItemClickListener {
                navigateTo(R.id.chatMessageFragment, bundleOf(ChatMessageFragment.EXTRA_CHANNEL_ID to it.cid))
            }
        }
        /*with(mBinding.channelList) {
            adapter = mChannelAdapter
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    LinearLayoutManager.VERTICAL,
                ).apply {
                    setDrawable(
                        ContextCompat.getDrawable(
                            requireContext(), R.drawable.common_ui_divider
                        )!!
                    )
                },
            )
        }*/
    }
}