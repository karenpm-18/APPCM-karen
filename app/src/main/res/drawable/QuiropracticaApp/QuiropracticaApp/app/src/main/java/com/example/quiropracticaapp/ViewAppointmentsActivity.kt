package com.example.quiropracticaapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ViewAppointmentsActivity : AppCompatActivity() {

    private lateinit var appointmentAdapter: AppointmentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_appointments)
        // La configuración inicial del RecyclerView se hará en onResume
    }

    override fun onResume() {
        super.onResume()
        // Recargamos la lista CADA VEZ que volvemos a esta pantalla para ver nuevas citas guardadas
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val recyclerView: RecyclerView = findViewById(R.id.recycler_view_appointments)

        // Usamos la lista de citas COMPARTIDA de la base de datos
        appointmentAdapter = AppointmentAdapter(Database.appointmentList)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = appointmentAdapter
    }
}