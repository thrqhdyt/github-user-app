package com.thorcode.githubuserapp

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.thorcode.githubuserapp.databinding.ActivityMainBinding
import com.thorcode.githubuserapp.utils.NetworkStatus

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#3C5BA9")))

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

        mainViewModel.listUser.observe(this) { user ->
            adapter.submitList(user)
        }

        mainViewModel.status.observe(this) {
            setStatus(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.search)?.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                Toast.makeText(this@MainActivity, query, Toast.LENGTH_SHORT).show()
                mainViewModel.getListUser(query)
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
        return true
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
}