package com.example.quiropracticaapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ClientsActivity : AppCompatActivity() {

    // Ya no necesitamos 'lateinit var clientList' para manejar la lista de la UI
    private lateinit var clientAdapter: ClientAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clients)

        // El FAB no necesita recargarse, se inicializa una sola vez
        setupAddClientButton()
    }

    override fun onResume() {
        super.onResume()

        // ¡CORRECCIÓN CLAVE! Recargamos la configuración del RecyclerView en onResume.
        // Esto captura los clientes recién añadidos.
        setupRecyclerView()
    }

    private fun setupAddClientButton() {
        val fabAddClient: FloatingActionButton = findViewById(R.id.fab_add_client)
        fabAddClient.setOnClickListener {
            val intent = Intent(this, AddClientActivity::class.java)
            startActivity(intent)
        }
    }

    // Ya no necesitamos loadSampleClients(), la lista viene del Singleton Database
    // private fun loadSampleClients() { ... }

    private fun setupRecyclerView() {
        val recyclerView: RecyclerView = findViewById(R.id.recycler_view_clients)

        // Usamos Database.clientList directamente.
        // IMPORTANTE: Dado que clientList es mutable y se actualiza,
        // necesitamos crear un adaptador nuevo o notificar el cambio,
        // pero recrear el adaptador y asignarlo en onResume es la forma más segura.

        // Creamos y asignamos el adaptador con la lista COMPARTIDA de la base de datos
        clientAdapter = ClientAdapter(Database.clientList)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = clientAdapter
    }
}