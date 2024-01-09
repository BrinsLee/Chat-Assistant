package com.brins.gpt.fragment

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.transition.TransitionManager
import com.brins.gpt.R
import com.brins.gpt.adapter.ChannelAdapter
import com.brins.gpt.databinding.FragmentHomeBinding
import com.brins.gpt.di.GPTChannelViewModelFactory
import com.brins.gpt.extensions.bindsView
import com.brins.gpt.viewmodel.ChatGPTChannelStateViewModel
import com.brins.gpt.viewmodel.ChatGPTMessageViewModel
import com.brins.gpt.viewmodel.ChatGPTUserInfoViewModel
import com.brins.lib_base.base.BaseFragment
import com.brins.lib_base.extensions.isVisible
import dagger.hilt.android.AndroidEntryPoint
import io.getstream.chat.android.ui.viewmodel.channels.ChannelListHeaderViewModel
import io.getstream.chat.android.ui.viewmodel.channels.ChannelListViewModel
import io.getstream.chat.android.ui.viewmodel.search.SearchViewModel
import io.getstream.chat.android.ui.viewmodel.search.bindView
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
        ChatGPTChannelStateViewModel.DEFAULT_CHANNEL_LIMIT,
        ChatGPTChannelStateViewModel.DEFAULT_MESSAGE_LIMIT,
        ChatGPTChannelStateViewModel.DEFAULT_MEMBER_LIMIT)


    private val mChannelViewModel: ChatGPTChannelStateViewModel by viewModels { channelViewModelFactory }

    private val searchViewModel: SearchViewModel by viewModels()

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
        setUpBackPressListener()
        setUpChannelListHeader()
        setUpSearchInput()
        setUpSearchResultList()
        setUpChannelViews()
        setUpNavigationDrawer()
        observerStateAndEvents()
        mUserInfoViewModel.fetchUserInfo()
    }

    private fun setUpSearchInput() {
        with(mBinding.searchInputView) {
            setDebouncedInputChangedListener {query ->
                if (query.isEmpty()) {
                    mBinding.channelList.visibility = View.VISIBLE
                    mBinding.searchResultListView.visibility = View.GONE
                }
            }
            setSearchStartedListener {query ->
                hideKeyBoard()
                searchViewModel.setQuery(query)
                mBinding.channelList.visibility = if (query.isEmpty()) View.VISIBLE else View.GONE
                mBinding.searchResultListView.visibility = if (query.isEmpty()) View.GONE else View.VISIBLE
            }
        }
    }
    private fun setUpSearchResultList() {
        with(mBinding.searchResultListView) {
            searchViewModel.bindView(this, viewLifecycleOwner)
            setSearchResultSelectedListener {
                navigateTo(R.id.chatMessageFragment, bundleOf(ChatMessageFragment.EXTRA_CHANNEL_ID to it.cid, ChatMessageFragment.EXTRA_MESSAGE_ID to it.id))
            }
        }
    }


    private fun setUpBackPressListener() {
        activity?.apply {
            onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
                if (mBinding.searchInputView.isVisible()) {
                    mBinding.searchInputView.clear()
                    hideSearchView()
                    return@addCallback
                }
                finish()
            }
        }
    }

    private fun setUpChannelListHeader() {
        with(mBinding.channelListHeader) {
            channelListHeaderViewModel.bindsView(this, viewLifecycleOwner)
            setOnMenuItemClickListener(object :
                Toolbar.OnMenuItemClickListener {
                override fun onMenuItemClick(item: MenuItem): Boolean {
                    when (item.itemId) {
                        com.brins.lib_base.R.id.action_search -> {
                            displaySearchView()
                            return true
                        }

                        com.brins.lib_base.R.id.action_add -> {
//                            mChannelViewModel.
                            return true
                        }

                        com.brins.lib_base.R.id.action_gpt_3_5 -> {
                            mChannelViewModel.handleEvents(ChatGPTChannelStateViewModel.GPTChannelEvent.CreateChannelEvent3_5())
                            return true
                        }

                        com.brins.lib_base.R.id.action_gpt_4 -> {
//                            mChannelViewModel.handleEvents(ChatGPTChannelStateViewModel.GPTChannelEvent.CreateChannelEvent4())
                            mChannelViewModel.handleEvents(ChatGPTChannelStateViewModel.GPTChannelEvent.CreateChannelEventVision4())
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

    private fun displaySearchView() {
        TransitionManager.beginDelayedTransition(mBinding.headerContainer)
        mBinding.searchInputView.visibility = View.VISIBLE
        mBinding.searchInputView.focusAndShowKeyboard()
    }

    private fun hideSearchView() {
        TransitionManager.beginDelayedTransition(mBinding.headerContainer)
        mBinding.searchInputView.visibility = View.GONE
    }



    private fun setUpNavigationDrawer() {
        AppBarConfiguration(
            setOf(R.id.changeColor, R.id.changeLanguage), mBinding.drawerLayout
        )
        mBinding.drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

            }

            override fun onDrawerOpened(drawerView: View) {
                mBinding.searchInputView.hideKeyBoard()
            }

            override fun onDrawerClosed(drawerView: View) {

            }

            override fun onDrawerStateChanged(newState: Int) {

            }

        })
        mBinding.navigationView.setupWithNavController(findNavController())
        val head = mBinding.navigationView.getHeaderView(0)
        userAvatarView = head.findViewById(R.id.userAvatarView)
        nameTextView = head.findViewById(R.id.nameTextView)
        mBinding.navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.changeColor -> {
                    // todo 更改颜色
                    showChangeColorSchemeDialog()
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
            activity?.finish()
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