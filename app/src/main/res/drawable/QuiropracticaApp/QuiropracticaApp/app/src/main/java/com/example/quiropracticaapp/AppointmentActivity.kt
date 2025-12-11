package com.example.quiropracticaapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import java.util.Calendar
import java.util.Locale

class AppointmentActivity : AppCompatActivity() {

    private lateinit var clientNameEditText: TextInputEditText
    private lateinit var dateEditText: TextInputEditText
    private lateinit var timeEditText: TextInputEditText
    private lateinit var notesEditText: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment)

        // 1. Inicializar los campos de texto
        clientNameEditText = findViewById(R.id.input_appointment_client_name)
        dateEditText = findViewById(R.id.input_appointment_date)
        timeEditText = findViewById(R.id.input_appointment_time)
        notesEditText = findViewById(R.id.input_appointment_notes)

        // 2. Configurar los clics para abrir los selectores
        setupDateAndTimePickers()

        // 3. Configurar el botón de guardar
        setupSaveButton()
    }

    private fun setupDateAndTimePickers() {
        // Al hacer clic en el campo de fecha, muestra el selector de fecha
        dateEditText.setOnClickListener {
            showDatePicker()
        }

        // Al hacer clic en el campo de hora, muestra el selector de hora
        timeEditText.setOnClickListener {
            showTimePicker()
        }
    }

    /** Muestra el diálogo de selección de fecha (calendario) **/
    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val dateStr = String.format(Locale.getDefault(), "%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear)
                dateEditText.setText(dateStr)
            },
            year,
            month,
            day
        )
        // Opcional: Impedir seleccionar fechas pasadas
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }

    /** Muestra el diálogo de selección de hora **/
    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this,
            { _, selectedHour, selectedMinute ->
                val timeStr = String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute)
                timeEditText.setText(timeStr)
            },
            hour,
            minute,
            true
        )
        timePickerDialog.show()
    }

    private fun setupSaveButton() {
        // Asegúrate que el ID del botón sea R.id.button_save_appointment
        val saveButton: Button = findViewById(R.id.button_save_appointment)

        saveButton.setOnClickListener {
            // 1. Obtener los datos
            val clientName = clientNameEditText.text.toString().trim()
            val date = dateEditText.text.toString().trim()
            val time = timeEditText.text.toString().trim()
            val notes = notesEditText.text.toString().trim()

            // 2. Lógica de validación
            if (clientName.isEmpty() || date.isEmpty() || time.isEmpty()) {
                Toast.makeText(this, "Por favor, completa Nombre, Fecha y Hora.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // 3. Crear el objeto Appointment y GUARDARLO en Database
            val newId = Database.appointmentList.size + 1
            val newAppointment = Appointment(newId, clientName, date, time, notes)

            // Llamar a la función de guardado
            Database.addAppointment(newAppointment)

            // 4. Mostrar mensaje y terminar la actividad
            Toast.makeText(this, "Cita de $clientName agendada con éxito.", Toast.LENGTH_LONG).show()
            finish() // Cierra la Activity de Agendar Cita y regresa al Dashboard
        }
    }
}