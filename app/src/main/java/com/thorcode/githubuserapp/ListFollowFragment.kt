package com.thorcode.githubuserapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.thorcode.githubuserapp.databinding.FragmentListFollowBinding


class ListFollowFragment : Fragment() {

    private var _binding: FragmentListFollowBinding? = null
    private val binding get() = _binding!!
    private val followsViewModel: DetailViewModel by viewModels()


    companion object {
        const val ARG_POSITION = "arg_position"
        const val ARG_USERNAME = "arg_username"
    }

    private var position: Int = 1
    private var username: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
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
            followsViewModel.getFollowers(username?:"")
        } else {
            followsViewModel.getFollowings(username?:"")
        }

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvFollows.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding.rvFollows.addItemDecoration(itemDecoration)
        val adapter = ListUsersAdapter()
        binding.rvFollows.adapter = adapter

        followsViewModel.listFollows.observe(viewLifecycleOwner) { user ->
            Log.d("ListFollowFragment", user.size.toString())
            adapter.submitList(user)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}