package com.miraflores.agenda.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.messaging.FirebaseMessaging
import com.miraflores.agenda.databinding.ActivityAdminBinding
import com.miraflores.agenda.utils.ColorHelper
import com.miraflores.agenda.utils.SessionManager

class AdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ColorHelper.applyTheme(this, binding.root)

        // SUSCRIPCIÓN AL TEMA (OBLIGATORIO PARA QUE LE LLEGUEN AL ADMIN)
        FirebaseMessaging.getInstance().subscribeToTopic("admin_citas")

        val navigationListener = { serviceName: String ->
            val intent = Intent(this, AppointmentsListActivity::class.java)
            intent.putExtra("EXTRA_SERVICE", serviceName)
            intent.putExtra("IS_ADMIN", true)
            startActivity(intent)
        }

        binding.btnOdontologia.setOnClickListener { navigationListener("Odontología") }
        binding.btnPsicologia.setOnClickListener { navigationListener("Psicología") }
        binding.btnConsultaGeneral.setOnClickListener { navigationListener("Consulta General") }

        binding.btnCerrarSesionAdmin.setOnClickListener { showLogoutDialog() }
    }

    override fun onResume() {
        super.onResume()
        ColorHelper.applyTheme(this, binding.root)
    }

    private fun showLogoutDialog() {
        AlertDialog.Builder(this)
            .setTitle("Cerrar Sesión")
            .setMessage("¿Salir del modo administrador?")
            .setPositiveButton("Sí") { _, _ ->
                SessionManager.logout(this)
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
            .setNegativeButton("No", null)
            .show()
    }
}