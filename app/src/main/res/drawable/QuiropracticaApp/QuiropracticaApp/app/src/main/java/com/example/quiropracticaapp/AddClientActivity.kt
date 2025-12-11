package com.example.quiropracticaapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText

class AddClientActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Usa el layout activity_add_client.xml
        setContentView(R.layout.activity_add_client)

        setupSaveClientButton()
    }

    private fun setupSaveClientButton() {
        val saveButton: Button = findViewById(R.id.button_save_client)

        saveButton.setOnClickListener {
            // 1. Obtener los datos usando los IDs corregidos
            val name = findViewById<TextInputEditText>(R.id.input_client_name).text.toString().trim()
            val phone = findViewById<TextInputEditText>(R.id.input_client_phone).text.toString().trim()
            val email = findViewById<TextInputEditText>(R.id.input_client_email).text.toString().trim()

            // 2. Validación básica
            if (name.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "El Nombre y Teléfono son obligatorios.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // 3. Crear y GUARDAR el cliente en la base de datos estática (Singleton)
            val newId = Database.clientList.size + 1
            val newClient = Client(newId, name, phone, email)

            Database.addClient(newClient) // <-- ¡El cliente Pia se añade aquí!

            // 4. Simulación de guardado
            val message = "Cliente $name registrado. Tel: $phone"
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()

            // Cierra la actividad de registro para volver a ClientsActivity
            finish()
        }
    }
}