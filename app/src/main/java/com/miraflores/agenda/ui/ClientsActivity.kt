package com.miraflores.agenda.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.miraflores.agenda.adapter.ClientAdapter
import com.miraflores.agenda.dao.ClientDaoImpl
import com.miraflores.agenda.databinding.ActivityClientsBinding // Asegúrate de crear el layout xml abajo
import com.miraflores.agenda.utils.ColorHelper

class ClientsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityClientsBinding
    private lateinit var clientDao: ClientDaoImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClientsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Aplicar tema (Azul si está seleccionado)
        ColorHelper.applyTheme(this, binding.rootLayout)

        clientDao = ClientDaoImpl(this)

        setupRecycler()

        binding.btnBack.setOnClickListener { finish() }
    }

    private fun setupRecycler() {
        val list = clientDao.getAllClients()
        if (list.isEmpty()) {
            binding.tvEmpty.visibility = View.VISIBLE
        } else {
            binding.tvEmpty.visibility = View.GONE
            binding.recyclerClients.layoutManager = LinearLayoutManager(this)
            binding.recyclerClients.adapter = ClientAdapter(list) { idClient ->
                confirmDelete(idClient)
            }
        }
    }

    private fun confirmDelete(id: Int) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar Cliente")
            .setMessage("¿Estás seguro?")
            .setPositiveButton("Sí") { _, _ ->
                if (clientDao.deleteClient(id)) {
                    Toast.makeText(this, "Eliminado", Toast.LENGTH_SHORT).show()
                    setupRecycler()
                }
            }
            .setNegativeButton("No", null)
            .show()
    }

    override fun onResume() {
        super.onResume()
        ColorHelper.applyTheme(this, binding.rootLayout)
    }
}