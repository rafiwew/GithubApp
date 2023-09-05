package com.piwew.githubapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.piwew.githubapp.R
import com.piwew.githubapp.data.response.ItemsItem
import com.piwew.githubapp.databinding.ActivityMainBinding
import com.piwew.githubapp.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private val mainViewModel by viewModels<MainViewModel>()
    private lateinit var binding: ActivityMainBinding
    private val adapter = UserAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        Thread.sleep(2000)
        installSplashScreen()

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setOnEditorActionListener { _, _, _ ->
                val newQuery = searchView.text.toString()
                searchBar.text = searchView.text
                mainViewModel.findUser(newQuery)
                searchView.hide()
                false
            }
        }

        showRecyclerView()

        mainViewModel.userItem.observe(this) { userItem ->
            showViewModel(userItem)
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

    }

    private fun showRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        binding.rvUser.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUser.addItemDecoration(itemDecoration)

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ItemsItem) {
                showSelectedUser(data)
            }
        })
    }

    private fun showViewModel(userItem: List<ItemsItem>) {
        if (userItem.isNotEmpty()) {
            binding.rvUser.visibility = View.VISIBLE
            binding.tvSize.text = resources.getString(R.string.show_users, userItem.size.toString())
            adapter.submitList(userItem)
            binding.rvUser.setHasFixedSize(true)
            binding.rvUser.adapter = adapter
        } else {
            binding.rvUser.visibility = View.INVISIBLE
            binding.tvSize.text = resources.getString(R.string.not_found_user)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showSelectedUser(user: ItemsItem) {
        val intentToDetail = Intent(this@MainActivity, UserDetailActivity::class.java)
        intentToDetail.putExtra("DATA", user.login)
        startActivity(intentToDetail)
    }

}