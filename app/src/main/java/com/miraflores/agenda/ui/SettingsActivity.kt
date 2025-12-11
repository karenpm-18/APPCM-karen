package com.miraflores.agenda.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.miraflores.agenda.R
import com.miraflores.agenda.databinding.ActivitySettingsBinding
import com.miraflores.agenda.utils.ColorHelper

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. Aplicar tema inicial (Fondo + Textos)
        ColorHelper.applyTheme(this, binding.root)

        setupUI()
        loadPreferences()
        setupListeners()
    }

    private fun setupUI() {
        binding.tvVersionApp.text = "Versión 1.0.5"
    }

    private fun loadPreferences() {
        // Cargar selección guardada de FONDO
        when (ColorHelper.getTheme(this)) {
            ColorHelper.THEME_BLUE -> binding.rbColorBlue.isChecked = true
            ColorHelper.THEME_GREEN -> binding.rbColorGreen.isChecked = true
            else -> binding.rbColorWine.isChecked = true
        }

        // Cargar selección guardada de TEXTO
        when (ColorHelper.getTextStyle(this)) {
            ColorHelper.TEXT_BLACK -> binding.rbTextBlack.isChecked = true
            ColorHelper.TEXT_WHITE -> binding.rbTextWhite.isChecked = true
            else -> binding.rbTextAuto.isChecked = true
        }
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener { finish() }

        // LISTENER DE FONDO: Guarda y aplica al instante
        binding.rgBackgroundColor.setOnCheckedChangeListener { _, checkedId ->
            val theme = when (checkedId) {
                R.id.rbColorBlue -> ColorHelper.THEME_BLUE
                R.id.rbColorGreen -> ColorHelper.THEME_GREEN
                else -> ColorHelper.THEME_WINE
            }
            ColorHelper.saveTheme(this, theme)

            // ¡IMPORTANTE! Re-aplicar para ver el cambio de color de fondo ya
            ColorHelper.applyTheme(this, binding.root)
        }

        // LISTENER DE TEXTO: Guarda y aplica al instante
        binding.rgTextColor.setOnCheckedChangeListener { _, checkedId ->
            val style = when (checkedId) {
                R.id.rbTextBlack -> ColorHelper.TEXT_BLACK
                R.id.rbTextWhite -> ColorHelper.TEXT_WHITE
                else -> ColorHelper.TEXT_AUTO
            }
            ColorHelper.saveTextStyle(this, style)

            // ¡IMPORTANTE! Re-aplicar para que la función recursiva pinte las letras ya
            ColorHelper.applyTheme(this, binding.root)

            Toast.makeText(this, "Estilo de texto actualizado", Toast.LENGTH_SHORT).show()
        }
    }

    // Si sales y entras, aseguramos que se pinte todo bien
    override fun onResume() {
        super.onResume()
        ColorHelper.applyTheme(this, binding.root)
    }
}
