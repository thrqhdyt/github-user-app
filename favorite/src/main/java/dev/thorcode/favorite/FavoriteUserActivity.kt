package dev.thorcode.favorite

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.thorcode.githubuserapp.databinding.ActivityFavoriteUserBinding
import com.thorcode.githubuserapp.detail.DetailActivity
import dev.thorcode.core.ui.ListUsersAdapter
import dev.thorcode.favorite.di.favoriteModule
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

class FavoriteUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteUserBinding
    private val viewModel: FavoriteViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#B3C5FF")))

        loadKoinModules(favoriteModule)

        val layoutManager = LinearLayoutManager(this)
        binding.rvUsersFav.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUsersFav.addItemDecoration(itemDecoration)

        val adapter = ListUsersAdapter()
        adapter.onItemClick = { selectData ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_DATA, selectData)
            intent.putExtra(DetailActivity.EXTRA_USERNAME, selectData.username)
            startActivity(intent)
        }

        binding.rvUsersFav.adapter = adapter

        viewModel.favoriteUser.observe(this){ user ->
            if (user != null) {
                adapter.setData(user)
            }
        }
    }
}