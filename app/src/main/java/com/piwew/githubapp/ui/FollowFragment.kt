package com.piwew.githubapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.piwew.githubapp.data.response.ItemsItem
import com.piwew.githubapp.databinding.FragmentSectionPagerBinding
import com.piwew.githubapp.viewmodel.FollowersViewModel
import com.piwew.githubapp.viewmodel.FollowingViewModel

class FollowFragment : Fragment() {

    private val followersViewModel by viewModels<FollowersViewModel>()
    private val followingViewModel by viewModels<FollowingViewModel>()
    private lateinit var binding: FragmentSectionPagerBinding
    private val adapter = UserAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSectionPagerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val position = arguments?.getInt(ARG_POSITION) ?: 0

        if (position == 1) {
            followersViewModel.getFollowers(UserDetailActivity.username)
            followersViewModel.followers.observe(viewLifecycleOwner) { followers ->
                showFollowersViewModel(followers)
            }
            showRecyclerView()
            followersViewModel.isLoading.observe(viewLifecycleOwner) {
                showLoading(it)
            }
        } else {
            followingViewModel.getFollowing(UserDetailActivity.username)
            followingViewModel.following.observe(viewLifecycleOwner) { following ->
                showFollowingViewModel(following)
            }
            showRecyclerView()
            followingViewModel.isLoading.observe(viewLifecycleOwner) {
                showLoading(it)
            }
        }

    }

    private fun showFollowingViewModel(following: List<ItemsItem>) {
        if (following.isNotEmpty()) {
            binding.rvUser.visibility = View.VISIBLE
            adapter.submitList(following)
            binding.rvUser.adapter = adapter
        }
    }

    private fun showFollowersViewModel(followers: List<ItemsItem>) {
        if (followers.isNotEmpty()) {
            binding.rvUser.visibility = View.VISIBLE
            adapter.submitList(followers)
            binding.rvUser.adapter = adapter
        }
    }

    private fun showRecyclerView() {
        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvUser.layoutManager = layoutManager

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ItemsItem) {
                showSelectedUser(data)
            }
        })
    }

    private fun showSelectedUser(user: ItemsItem) {
        val intentToDetail = Intent(requireActivity(), UserDetailActivity::class.java)
        intentToDetail.putExtra("DATA", user.login)
        startActivity(intentToDetail)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val ARG_POSITION = "position"
        const val ARG_USERNAME = "username"
    }

}