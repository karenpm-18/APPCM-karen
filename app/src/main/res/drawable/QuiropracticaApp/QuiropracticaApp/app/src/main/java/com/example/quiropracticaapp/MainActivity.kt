package com.example.quiropracticaapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import androidx.cardview.widget.CardView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. Llamar a la función que configura los botones
        setupDashboardListeners()
    }

    private fun setupDashboardListeners() {
        // Enlazar las tarjetas del Dashboard usando su ID
        val cardCitas = findViewById<CardView>(R.id.card_citas)
        val cardClientes = findViewById<CardView>(R.id.card_clientes)

        // ¡NUEVO! Enlazar las nuevas tarjetas
        val cardVerCitas = findViewById<CardView>(R.id.card_ver_citas) // Card para la lista de citas
        val cardSalir = findViewById<CardView>(R.id.card_salir)       // Card para salir de la App

        // Configurar el clic en la tarjeta de Agendar Cita
        cardCitas.setOnClickListener {
            val intent = Intent(this, AppointmentActivity::class.java)
            startActivity(intent)
        }

        // Configurar el clic en la tarjeta de Mis Clientes
        cardClientes.setOnClickListener {
            val intent = Intent(this, ClientsActivity::class.java)
            startActivity(intent)
        }

        // ¡NUEVA LÓGICA! Configurar el clic en la tarjeta de Ver Citas
        cardVerCitas.setOnClickListener {
            // Navegamos a la nueva actividad que mostrará la lista de citas
            val intent = Intent(this, ViewAppointmentsActivity::class.java)
            startActivity(intent)
        }

        // ¡NUEVA LÓGICA! Configurar el clic en la tarjeta de Salir
        cardSalir.setOnClickListener {
            // finishAffinity() cierra la Activity actual y todas las actividades en la pila,
            // lo que termina la aplicación por completo.
            finishAffinity()
        }
    }
}