package com.thorcode.githubuserapp.detail

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.thorcode.githubuserapp.R
import com.thorcode.githubuserapp.databinding.ActivityDetailBinding
import com.thorcode.githubuserapp.ui.SectionPagerAdapter
import dev.thorcode.core.domain.model.DetailUser
import dev.thorcode.core.domain.model.User
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel: DetailViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#B3C5FF")))

        val username = intent.getStringExtra(EXTRA_USERNAME)

        if (username != null) {
            detailViewModel.getDetailUser(username)
            detailViewModel.detailUser.observe(this){
                if (it != null){
                    binding.progressBar.visibility = View.GONE
                    val dataUser = it.data
                    showDetailUser(dataUser)
                } else {
                    binding.progressBar.visibility = View.GONE
                    Log.d(DetailActivity::class.java.simpleName, DetailActivity.toString())
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            }
        }

        val detailUserFab = intent.getParcelableExtra<User>(EXTRA_DATA)
        Log.d("FAB", detailUserFab?.isFavorite.toString())
        if (detailUserFab != null) {
            showDetailUserFab(detailUserFab)
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

    private fun showDetailUser(detailUser: DetailUser?){
        detailUser.let {
            supportActionBar?.title = detailUser?.username
            binding.tvNameDetail.text = detailUser?.name
            binding.tvUsernameDetail.text = detailUser?.username
            binding.tvFollowers.text = resources.getString(R.string.followers, it?.followers ?: 0)
            binding.tvFollowing.text = resources.getString(R.string.following, it?.following ?: 0)
            Glide.with(this)
                .load(detailUser?.avatarUrl)
                .into(binding.avatarDetail)
        }
    }

    private fun showDetailUserFab(user: User){
        user.let {
            var statusFavorite = user.isFavorite
            setStatusFavorite(statusFavorite)

            binding.fabFav.setOnClickListener {
                statusFavorite = !statusFavorite
                detailViewModel.setFavoriteUser(user, statusFavorite)
                setStatusFavorite(statusFavorite)
            }
        }
    }

    private fun setStatusFavorite(statusFavorite: Boolean) {
        if (statusFavorite) {
            binding.fabFav.setImageResource(R.drawable.ic_favorite_24)
        } else {
            binding.fabFav.setImageResource(R.drawable.ic_favorite_border_24)
        }
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
                    putExtra(Intent.EXTRA_TEXT, "$BASE_URL/thrqhdyt")
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


    companion object {
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_DATA = "extra_data"
        const val BASE_URL = "https://github.com"
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}