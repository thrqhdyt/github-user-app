package com.thorcode.githubuserapp.ui

import android.app.SearchManager
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.thorcode.githubuserapp.*
import com.thorcode.githubuserapp.api.ApiConfig
import com.thorcode.githubuserapp.database.FavoriteUser
import com.thorcode.githubuserapp.database.FavoriteUserDatabase
import com.thorcode.githubuserapp.databinding.ActivityMainBinding
import com.thorcode.githubuserapp.utils.Result
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.thorcode.githubuserapp.adapter.ListUsersAdapter
import com.thorcode.githubuserapp.model.MainViewModel
import com.thorcode.githubuserapp.preferences.SettingPreferences
import com.thorcode.githubuserapp.repository.UserRepository

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by lazy {
        val pref = SettingPreferences.getInstance(dataStore)
        val apiService = ApiConfig.getApiService()
        val dao = FavoriteUserDatabase.getDatabase(this).favoriteUserDao()
        val userRepository = UserRepository.getInstance(apiService, userDao = dao)
        val mainViewModel = ViewModelProvider(this, MainViewModel.Factory(pref, userRepository))[MainViewModel::class.java]
        mainViewModel
    }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#B3C5FF")))

        val layoutManager = LinearLayoutManager(this)
        binding.rvUsers.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUsers.addItemDecoration(itemDecoration)

        val adapter = ListUsersAdapter(object : ListUsersAdapter.OnItemListener {
            override fun onClickListener(username: String) {
                Intent(this@MainActivity, DetailActivity::class.java)
                    .putExtra(DetailActivity.EXTRA_USERNAME, username)
                    .also {
                        startActivity(it)
                    }
            }
        })

        binding.rvUsers.adapter = adapter

        mainViewModel.getListUser().observe(this) {
            if(it is Result.Success)
                adapter.submitList(it.data)
            setStatus(it)
        }

        mainViewModel.listUser.observe(this) { user ->
            adapter.submitList(user)
        }

        mainViewModel.getTheme().observe(this)
        { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.search)?.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                Toast.makeText(this@MainActivity, query, Toast.LENGTH_SHORT).show()
                mainViewModel.getListUserByUsername(query)
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.ic_switch_theme -> {
                val intentToTheme = Intent(this@MainActivity, ThemeSwitchActivity::class.java)
                startActivity(intentToTheme)
            }
            R.id.ic_favorite -> {
                val intentToFav = Intent(this@MainActivity, FavoriteUserActivity::class.java)
                startActivity(intentToFav)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setStatus(status: Result<List<FavoriteUser>>) {
        binding.progressBar.visibility = when (status) {
            is Result.Error -> {
                Toast.makeText(this, status.error, Toast.LENGTH_LONG).show()
                View.VISIBLE
            }
            is Result.Success -> {
                View.GONE
            }
            else -> {
                View.VISIBLE
            }
        }
    }
}