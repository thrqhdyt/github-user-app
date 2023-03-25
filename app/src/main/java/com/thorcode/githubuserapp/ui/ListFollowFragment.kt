package com.thorcode.githubuserapp.ui

import android.os.Bundle
import android.text.style.TtsSpan.ARG_USERNAME
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.thorcode.githubuserapp.adapter.ListUsersAdapter
import com.thorcode.githubuserapp.repository.UserRepository
import com.thorcode.githubuserapp.api.ApiConfig
import com.thorcode.githubuserapp.database.FavoriteUserDatabase
import com.thorcode.githubuserapp.databinding.FragmentListFollowBinding
import com.thorcode.githubuserapp.model.DetailViewModel
import com.thorcode.githubuserapp.utils.NetworkStatus

class ListFollowFragment : Fragment() {

    private var _binding: FragmentListFollowBinding? = null
    private val binding get() = _binding!!
    private val followsViewModel: DetailViewModel by lazy{
        val apiService = ApiConfig.getApiService()
        val dao = FavoriteUserDatabase.getDatabase(requireActivity()).favoriteUserDao()
        val userRepository = UserRepository.getInstance(apiService, userDao = dao)
        val viewModel = ViewModelProvider(this, DetailViewModel.Factory( userRepository))[DetailViewModel::class.java]
        viewModel
    }
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

        followsViewModel.status.observe(viewLifecycleOwner){
            setStatus(it)
        }
    }

    private fun setStatus(status: NetworkStatus) {
        binding.progressBar.visibility =  when(status){
            is NetworkStatus.FAILED -> {
                Toast.makeText(requireActivity(), status.message, Toast.LENGTH_LONG).show()
                View.VISIBLE
            }
            is NetworkStatus.SUCCESS -> {
                View.GONE
            }
            else -> {
                View.VISIBLE
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