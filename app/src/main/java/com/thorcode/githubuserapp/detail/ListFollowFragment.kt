package com.thorcode.githubuserapp.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.thorcode.githubuserapp.databinding.FragmentListFollowBinding
import dev.thorcode.core.data.source.remote.network.ApiResponse
import dev.thorcode.core.domain.model.User
import dev.thorcode.core.ui.ListUsersAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class ListFollowFragment : Fragment() {

    private var _binding: FragmentListFollowBinding? = null
    private val binding get() = _binding!!
    private val followsViewModel: DetailViewModel by viewModel()
    private var position: Int = 1
    private var username: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListFollowBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME)
        }
        if (position == 1) {
            followsViewModel.getListFollowers(username ?: "")
            Log.d("Followers", username.toString())
        } else {
            followsViewModel.getListFollowing(username?:"")
        }

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvFollows.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding.rvFollows.addItemDecoration(itemDecoration)

        val adapter = ListUsersAdapter()

        binding.rvFollows.adapter = adapter

        followsViewModel.followers.observe(viewLifecycleOwner) { apiResponse ->
            when (apiResponse) {
                is ApiResponse.Success -> {
                    val followersList = apiResponse.data.map {
                        User(it.id, it.login, it.avatarUrl, false)
                    }
                    adapter.setData(followersList)
                }
                is ApiResponse.Error -> {
                    Toast.makeText(requireActivity(), "Something Went Wrong!!", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(requireActivity(), "Something Went Wrong!!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        followsViewModel.followings.observe(viewLifecycleOwner) { apiResponse ->
            when (apiResponse) {
                is ApiResponse.Success -> {
                    val followingsList = apiResponse.data.map {
                        User(it.id, it.login, it.avatarUrl, false)
                    }
                    adapter.setData(followingsList)
                }
                is ApiResponse.Error -> {
                    Toast.makeText(requireActivity(), "Something Went Wrong!!", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(requireActivity(), "Something Went Wrong!!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val ARG_POSITION = "arg_position"
        const val ARG_USERNAME = "arg_username"
    }
}