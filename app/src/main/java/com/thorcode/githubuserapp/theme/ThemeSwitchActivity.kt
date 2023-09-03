package com.thorcode.githubuserapp.theme

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.thorcode.githubuserapp.R
import com.thorcode.githubuserapp.databinding.ActivityThemeSwitchBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class ThemeSwitchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityThemeSwitchBinding
    private val themeViewModel: ThemeViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThemeSwitchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#B3C5FF")))

        val switchTheme = binding.switchTheme

        themeViewModel.getThemeSettings().observe(this)
            {isDarkModeActive: Boolean ->
                if (isDarkModeActive) {
                    binding.switchTheme.text = getString(R.string.dark_mode)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    switchTheme.isChecked = true
                } else {
                    binding.switchTheme.text = getString(R.string.light_mode)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    switchTheme.isChecked = false
                }
            }

        switchTheme.setOnCheckedChangeListener {_, isChecked ->
            themeViewModel.saveThemeSetting(isChecked)
        }
    }
}