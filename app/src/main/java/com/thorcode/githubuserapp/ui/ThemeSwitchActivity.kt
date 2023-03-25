package com.thorcode.githubuserapp.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.thorcode.githubuserapp.preferences.SettingPreferences
import com.thorcode.githubuserapp.model.ThemeViewModel
import com.thorcode.githubuserapp.model.ThemeViewModelFactory
import com.thorcode.githubuserapp.databinding.ActivityThemeSwitchBinding
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.thorcode.githubuserapp.R

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ThemeSwitchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityThemeSwitchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThemeSwitchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#B3C5FF")))

        val switchTheme = binding.switchTheme

        val pref = SettingPreferences.getInstance(dataStore)
        val themeViewModel = ViewModelProvider(this, ThemeViewModelFactory(pref))[ThemeViewModel::class.java]

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

        switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            themeViewModel.saveThemeSetting(isChecked)
        }
    }
}