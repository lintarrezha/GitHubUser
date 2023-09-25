package com.dicoding.githubuser.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.databinding.FragmentFollowBinding
import com.dicoding.githubuser.ui.UserAdapter
import com.dicoding.githubuser.viewmodel.FollowersViewModel
import com.dicoding.githubuser.viewmodel.FollowingViewModel

class FollowFragment : Fragment() {

    private lateinit var followersViewModel: FollowersViewModel
    private lateinit var followingViewModel: FollowingViewModel

    private lateinit var binding: FragmentFollowBinding
    private lateinit var adapter: UserAdapter

    private var position: Int = 0
    private var username: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME, "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        followingViewModel = ViewModelProvider(this).get(FollowingViewModel::class.java)
        followersViewModel = ViewModelProvider(this).get(FollowersViewModel::class.java)

        setupRecyclerView()
        observeFollowersOrFollowing()
    }

    private fun setupRecyclerView() {
        adapter = UserAdapter()
        binding.rvFollow.layoutManager = LinearLayoutManager(requireContext())

        val itemDecoration = DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        binding.rvFollow.addItemDecoration(itemDecoration)
    }

    private fun observeFollowersOrFollowing() {
        if (position == 0) {
            followingViewModel.setUserLogin(username)
            followingViewModel.following.observe(viewLifecycleOwner) { following ->
                Log.d("Logku", "ini di following")
                showLoading(false)
                adapter.submitList(following)
                binding.rvFollow.adapter = adapter

            }
        } else if (position == 1) {
            followersViewModel.setUserLogin(username)
            followersViewModel.followers.observe(viewLifecycleOwner) { followers ->
                Log.d("Logku", "ini di followers")
                showLoading(false)
                adapter.submitList(followers)
                binding.rvFollow.adapter = adapter

            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBarFollow.visibility = View.VISIBLE
            binding.rvFollow.visibility = View.GONE
        } else {
            binding.progressBarFollow.visibility = View.GONE
            binding.rvFollow.visibility = View.VISIBLE
        }
    }

    companion object {
        const val ARG_POSITION = "position"
        const val ARG_USERNAME = "username"

        fun newInstance(position: Int, username: String): FollowFragment {
            val fragment = FollowFragment()
            val args = Bundle()
            args.putInt(ARG_POSITION, position)
            args.putString(ARG_USERNAME, username)
            fragment.arguments = args
            return fragment
        }
    }
}
