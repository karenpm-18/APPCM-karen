package com.miraflores.agenda.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.bumptech.glide.Glide
import com.miraflores.agenda.R
import com.miraflores.agenda.databinding.ActivityMainBinding
import com.miraflores.agenda.utils.ColorHelper
import com.miraflores.agenda.utils.SessionManager
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicialización completa
        applyThemeToAll()
        setupUserData()
        setupCurrentDate() // <--- NUEVA FUNCIÓN AGREGADA
        setupMainButtons()
        setupNavigationDrawer()
    }

    override fun onResume() {
        super.onResume()
        // Al volver, recargamos temas y datos, y actualizamos la fecha por si cambió el día
        applyThemeToAll()
        setupUserData()
        setupCurrentDate()
        binding.navView.setCheckedItem(0)
    }

    // --- NUEVA FUNCIÓN PARA LA FECHA ---
    private fun setupCurrentDate() {
        // Obtenemos la fecha actual del sistema
        val currentDate = Date()

        // Creamos un formateador en Español
        // Patrón: "EEEE" (Día completo), "d" (número día), "MMM" (Mes abreviado), "yyyy" (Año)
        val formatter = SimpleDateFormat("EEEE, d MMM, yyyy", Locale("es", "ES"))

        // Formateamos y convertimos a MAYÚSCULAS (ej: "DOMINGO, 10 AGO, 2025")
        val dateString = formatter.format(currentDate).uppercase()

        // Asignamos al TextView
        binding.tvDate.text = dateString
    }
    // -----------------------------------

    private fun applyThemeToAll() {
        ColorHelper.applyTheme(this, binding.mainContentLayout)
        ColorHelper.applyTheme(this, binding.navView)
    }

    private fun setupUserData() {
        val userName = SessionManager.getName(this)
        val userEmail = SessionManager.getEmail(this)

        binding.tvUserName.text = userName

        try {
            Glide.with(this).load(R.drawable.dos).circleCrop().into(binding.ivUserIcon)
        } catch (e: Exception) {}

        val headerView = binding.navView.getHeaderView(0)
        val tvHeaderName = headerView.findViewById<TextView>(R.id.tvHeaderName)
        val tvHeaderEmail = headerView.findViewById<TextView>(R.id.tvHeaderEmail)
        val ivHeaderIcon = headerView.findViewById<android.widget.ImageView>(R.id.ivHeaderIcon)

        if (tvHeaderName != null) tvHeaderName.text = userName
        if (tvHeaderEmail != null) tvHeaderEmail.text = userEmail

        try {
            if (ivHeaderIcon != null) {
                Glide.with(this).load(R.drawable.dos).circleCrop().into(ivHeaderIcon)
            }
        } catch (e: Exception) {}
    }

    private fun setupMainButtons() {
        binding.btnMenu.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        binding.btnAgenda.setOnClickListener {
            startActivity(Intent(this, AppointmentActivity::class.java))
        }

        binding.btnVerCitas.setOnClickListener {
            startActivity(Intent(this, AppointmentsListActivity::class.java))
        }

        binding.btnMapa.setOnClickListener {
            startActivity(Intent(this, MapsActivity::class.java))
        }
    }

    private fun setupNavigationDrawer() {
        binding.navView.setNavigationItemSelectedListener { menuItem ->
            handleNavigation(menuItem)
            true
        }
    }

    private fun handleNavigation(item: MenuItem) {
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        when (item.itemId) {
            R.id.nav_citas -> {
                startActivity(Intent(this, AppointmentsListActivity::class.java))
            }
            R.id.nav_ubicacion -> {
                startActivity(Intent(this, MapsActivity::class.java))
            }
            R.id.nav_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
            }
            R.id.nav_logout -> {
                showLogoutDialog()
            }
        }
    }

    private fun showLogoutDialog() {
        AlertDialog.Builder(this)
            .setTitle("Cerrar Sesión")
            .setMessage("¿Estás seguro de que deseas salir?")
            .setPositiveButton("Sí") { _, _ ->
                SessionManager.logout(this)
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}
