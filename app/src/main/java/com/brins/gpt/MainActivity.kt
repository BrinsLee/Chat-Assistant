package com.brins.gpt

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import com.brins.gpt.databinding.ActivityMainBinding
import com.brins.lib_base.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

/**
 * 为Android类添加 @AndroidEntryPoint注解，就可以向其里面的字段添加注入依赖了
 */
@AndroidEntryPoint
class MainActivity : com.brins.lib_base.base.BaseActivity() {

    private val mBinding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)

//        setUpNavController()
    }

    private fun setUpNavController() {
        val navController = findNavController(R.id.fragment_container)
        val navInflater = navController.navInflater
        val navGraph = navInflater.inflate(R.navigation.nav_main_graph)
    }
}