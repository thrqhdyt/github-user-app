package com.thorcode.githubuserapp.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.thorcode.githubuserapp.model.DetailViewModel
import com.thorcode.githubuserapp.R
import com.thorcode.githubuserapp.adapter.SectionPagerAdapter
import com.thorcode.githubuserapp.repository.UserRepository
import com.thorcode.githubuserapp.api.ApiConfig
import com.thorcode.githubuserapp.database.FavoriteUserDatabase
import com.thorcode.githubuserapp.databinding.ActivityDetailBinding
import com.thorcode.githubuserapp.utils.NetworkStatus

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel: DetailViewModel by lazy {
        val apiService = ApiConfig.getApiService()
        val dao = FavoriteUserDatabase.getDatabase(this).favoriteUserDao()
        val userRepository = UserRepository.getInstance(apiService, userDao = dao)
        val viewModel = ViewModelProvider(this, DetailViewModel.Factory(userRepository))[DetailViewModel::class.java]
        viewModel
    }
    private lateinit var username: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        username = intent.getStringExtra(EXTRA_USERNAME) ?: ""
        detailViewModel.getDetailUser(username)
        val btnFav = binding.fabFav

        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#B3C5FF")))
        supportActionBar?.title = username

        detailViewModel.detailUser.observe(this) {
            with(binding){
                tvNameDetail.text = it.name
                tvUsernameDetail.text = it.login
                tvFollowers.text = resources.getString(R.string.followers, it.followers)
                tvFollowing.text = resources.getString(R.string.following, it.following)
                Glide.with(this@DetailActivity)
                    .load(it.avatarUrl)
                    .into(avatarDetail)
            }
        }

        detailViewModel.detailUser.observe(this) { detailUser->
            detailViewModel.isFavoritedUser(detailUser.id).observe(this){isFavorite->
                btnFav.setImageResource(if (isFavorite) R.drawable.ic_favorite_24 else R.drawable.ic_favorite_border_24)
                btnFav.setOnClickListener {
                    Log.d(DetailActivity::class.java.simpleName, "has been called")
                detailViewModel.saveUser(
                    detailUser, !isFavorite
                )
                }
            }
        }

        detailViewModel.status.observe(this) {
            setStatus(it)
        }

        val sectionPagerAdapter = SectionPagerAdapter(this)
        sectionPagerAdapter.username = username

        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionPagerAdapter

        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) {tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.detail_option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.share -> {
                val intentShare = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "$BASE_URL/$username")
                    type = "text/plain"
                }
                @Suppress("DEPRECATION")
                packageManager?.resolveActivity(
                    intentShare, PackageManager.MATCH_DEFAULT_ONLY
                )
                startActivity(intentShare)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setStatus(status: NetworkStatus) {
        binding.progressBar.visibility =  when(status){
            is NetworkStatus.FAILED -> {
                Toast.makeText(this, status.message, Toast.LENGTH_LONG).show()
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

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        const val BASE_URL = "https://github.com"
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}