package com.limboooo.contactrecorder.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.fragment.app.proxyFragmentFactory
import androidx.navigation.fragment.NavHostFragment
import com.github.fragivity.loadRoot
import com.limboooo.contactrecorder.R
import com.limboooo.contactrecorder.databinding.ActivityMainBinding
import com.limboooo.contactrecorder.fragment.FragmentContactList
import com.limboooo.contactrecorder.repository.ProjectViewModel
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewModel by viewModels<ProjectViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        runBlocking {
            viewModel.invalidateMainList()
        }
        WindowCompat.setDecorFitsSystemWindows(window, false)
        proxyFragmentFactory()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host) as NavHostFragment
        navHostFragment.loadRoot(FragmentContactList::class)
    }

}