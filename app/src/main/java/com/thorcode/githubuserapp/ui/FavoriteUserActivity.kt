package com.thorcode.githubuserapp.ui

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.thorcode.githubuserapp.model.FavoriteViewModel
import com.thorcode.githubuserapp.adapter.ListUsersAdapter
import com.thorcode.githubuserapp.repository.UserRepository
import com.thorcode.githubuserapp.api.ApiConfig
import com.thorcode.githubuserapp.database.FavoriteUserDatabase
import com.thorcode.githubuserapp.databinding.ActivityFavoriteUserBinding

class FavoriteUserActivity : AppCompatActivity() {

    private val favoriteViewModel: FavoriteViewModel by lazy {
        val apiService = ApiConfig.getApiService()
        val dao = FavoriteUserDatabase.getDatabase(this).favoriteUserDao()
        val userRepository = UserRepository.getInstance(apiService, userDao = dao)
        val viewModel = ViewModelProvider(this, FavoriteViewModel.Factory(userRepository))[FavoriteViewModel::class.java]
        viewModel
    }
    private lateinit var binding: ActivityFavoriteUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#B3C5FF")))

        val layoutManager = LinearLayoutManager(this)
        binding.rvUsersFav.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUsersFav.addItemDecoration(itemDecoration)

        val adapter = ListUsersAdapter(object : ListUsersAdapter.OnItemListener {
            override fun onClickListener(username: String) {
                Intent(this@FavoriteUserActivity, DetailActivity::class.java)
                    .putExtra(DetailActivity.EXTRA_USERNAME, username)
                    .also {
                        startActivity(it)
                    }
            }
        })

        binding.rvUsersFav.adapter = adapter

        favoriteViewModel.getListUserFav().observe(this) { user ->
            adapter.submitList(user)
        }
    }
}