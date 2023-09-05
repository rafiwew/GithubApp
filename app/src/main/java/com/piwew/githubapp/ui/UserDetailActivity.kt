package com.piwew.githubapp.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.annotation.StringRes
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.piwew.githubapp.R
import com.piwew.githubapp.data.response.DetailUserResponse
import com.piwew.githubapp.databinding.ActivityUserDetailBinding
import com.piwew.githubapp.viewmodel.DetailUserViewModel

class UserDetailActivity : AppCompatActivity() {

    private val detailUserViewModel by viewModels<DetailUserViewModel>()
    private lateinit var binding: ActivityUserDetailBinding
    private val adapter = SectionsPagerAdapter(this@UserDetailActivity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        username = intent.getStringExtra(DATA).toString()
        detailUserViewModel.getDetailUser(username)

        detailUserViewModel.userDetail.observe(this) { userDetail ->
            showViewModel(userDetail)
        }

        detailUserViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun showViewModel(userDetail: DetailUserResponse) {
        with(binding) {
            ivProfile.loadImage(
                url = userDetail.avatarUrl
            )
            tvDetailName.text = userDetail.name
            tvDetailUsername.text = userDetail.login
            tvDetailLocation.text = userDetail.location
            tvDetailFollowing.text =
                resources.getString(R.string.following, userDetail.following)
            tvDetailFollowers.text =
                resources.getString(R.string.followers, userDetail.followers)
            adapter.username = userDetail.login
            viewPager.adapter = adapter
            TabLayoutMediator(tabs, viewPager) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()
            fabGithub.setOnClickListener {
                val url = "https://github.com/${userDetail.login}"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun ImageView.loadImage(url: String) {
        Glide.with(this.context)
            .load(url)
            .fitCenter()
            .skipMemoryCache(true)
            .into(this)
    }

    companion object {
        const val DATA = "DATA"
        var username = String()

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_2,
            R.string.tab_text_1,
        )
    }

}