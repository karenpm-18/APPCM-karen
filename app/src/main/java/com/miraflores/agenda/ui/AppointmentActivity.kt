package com.miraflores.agenda.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.miraflores.agenda.R
import com.miraflores.agenda.databinding.ActivityAppointmentBinding
import com.miraflores.agenda.db.AgendaDbHelper
import com.miraflores.agenda.model.Appointment
import com.miraflores.agenda.utils.ColorHelper
import org.json.JSONObject
import java.util.Calendar

class AppointmentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAppointmentBinding
    private lateinit var dbHelper: AgendaDbHelper

    // Variables de estado
    private var isEditMode = false
    private var appointmentId = -1

    // TU CLAVE DE SERVIDOR (FCM Legacy)
    private val SERVER_KEY = "8b4f7df1866dc7fa9dcc63925affe7638ef0d526"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppointmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. Configuración Inicial
        ColorHelper.applyTheme(this, binding.rootLayout)
        dbHelper = AgendaDbHelper(this)

        setupUI()
        checkForEditMode()
        setupListeners()
    }

    // --- CONFIGURACIÓN VISUAL (GIF y Spinner) ---
    private fun setupUI() {
        // Cargar GIF
        try {
            Glide.with(this).asGif().load(R.drawable.dos).into(binding.ivHeaderGif)
        } catch(e:Exception){}

        // Configurar Spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, arrayOf("Consulta General", "Odontología", "Psicología"))
        binding.spinnerService.adapter = adapter
    }

    // --- VERIFICAR SI ES EDICIÓN ---
    private fun checkForEditMode() {
        if (intent.hasExtra("EXTRA_ID")) {
            appointmentId = intent.getIntExtra("EXTRA_ID", -1)
            if (appointmentId != -1) {
                isEditMode = true
                loadAppointmentData(appointmentId)
            }
        }
    }

    // --- CARGAR DATOS EN LOS CAMPOS ---
    private fun loadAppointmentData(id: Int) {
        val cita = dbHelper.getAppointmentById(id)
        if (cita != null) {
            binding.titleAppointment.text = "EDITAR CITA"
            binding.buttonSaveAppointment.text = "ACTUALIZAR"

            binding.inputAppointmentClientName.setText(cita.clientName)
            binding.inputAppointmentAge.setText(cita.age)
            binding.inputAppointmentPhone.setText(cita.phone)
            binding.inputAppointmentDate.setText(cita.date)
            binding.inputAppointmentTime.setText(cita.time)
            binding.inputAppointmentNotes.setText(cita.notes)

            // Seleccionar el servicio en el spinner
            val adapter = binding.spinnerService.adapter as ArrayAdapter<String>
            val position = adapter.getPosition(cita.service)
            if (position >= 0) binding.spinnerService.setSelection(position)
        }
    }

    // --- LISTENERS DE BOTONES Y FECHAS ---
    private fun setupListeners() {
        binding.btnBack.setOnClickListener { finish() }

        // Selector de Fecha
        binding.inputAppointmentDate.setOnClickListener {
            val c = Calendar.getInstance()
            DatePickerDialog(this, { _, y, m, d ->
                val cal = Calendar.getInstance().apply { set(y, m, d) }
                // Validar fines de semana
                if (cal.get(Calendar.DAY_OF_WEEK) in arrayOf(Calendar.SATURDAY, Calendar.SUNDAY)) {
                    Toast.makeText(this, "Fines de semana inhábiles", Toast.LENGTH_LONG).show()
                    binding.inputAppointmentDate.text?.clear()
                } else {
                    binding.inputAppointmentDate.setText("$d/${m+1}/$y")
                }
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH))
                .apply { datePicker.minDate = System.currentTimeMillis() }
                .show()
        }

        // Selector de Hora
        binding.inputAppointmentTime.setOnClickListener {
            val c = Calendar.getInstance()
            TimePickerDialog(this, { _, h, m ->
                // Validar horario (9 AM a 6 PM)
                if (h in 9..17 || (h == 18 && m == 0)) {
                    binding.inputAppointmentTime.setText(String.format("%02d:%02d", h, m))
                } else {
                    Toast.makeText(this, "Horario de atención: 9:00 - 18:00", Toast.LENGTH_SHORT).show()
                }
            }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false).show()
        }

        // Botón Guardar
        binding.buttonSaveAppointment.setOnClickListener {
            saveAppointment()
        }
    }

    // --- LÓGICA DE GUARDADO ---
    private fun saveAppointment() {
        val name = binding.inputAppointmentClientName.text.toString().trim()
        val date = binding.inputAppointmentDate.text.toString().trim()
        val time = binding.inputAppointmentTime.text.toString().trim()
        val service = binding.spinnerService.selectedItem.toString()
        val phone = binding.inputAppointmentPhone.text.toString().trim()
        val age = binding.inputAppointmentAge.text.toString().trim()
        val notes = binding.inputAppointmentNotes.text.toString().trim()

        // 1. Validaciones
        if (name.isEmpty() || date.isEmpty() || time.isEmpty()) {
            Toast.makeText(this, "Por favor completa los campos obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        if (!dbHelper.isSlotAvailable(date, time, if (isEditMode) appointmentId else -1)) {
            Toast.makeText(this, "¡HORARIO OCUPADO! Selecciona otra hora.", Toast.LENGTH_LONG).show()
            return
        }

        // 2. Crear Objeto
        val appt = Appointment(
            id = if (isEditMode) appointmentId else -1,
            clientName = name,
            phone = phone,
            age = age,
            service = service,
            date = date,
            time = time,
            notes = notes,
            status = "PENDIENTE" // Por defecto pendiente
        )

        // 3. Guardar en Base de Datos
        if (isEditMode) {
            val result = dbHelper.updateAppointment(appt)
            if (result > 0) {
                Toast.makeText(this, "Cita actualizada correctamente", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show()
            }
        } else {
            val id = dbHelper.insertAppointment(appt)
            if (id > -1) {
                // Éxito: Enviar notificación
                enviarNotificacionDirecta(name, service, date, time)
                Toast.makeText(this, "Cita agendada y enviada", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Error al guardar en base de datos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // --- LÓGICA DE NOTIFICACIÓN (VOLLEY) ---
    private fun enviarNotificacionDirecta(cliente: String, servicio: String, fecha: String, hora: String) {
        val queue = Volley.newRequestQueue(this)
        val url = "https://fcm.googleapis.com/fcm/send"
        val jsonBody = JSONObject()

        try {
            val notification = JSONObject()
            notification.put("title", "📅 Nueva Cita: $servicio")
            notification.put("body", "$cliente solicita cita para el $fecha a las $hora.")
            notification.put("sound", "default")
            notification.put("click_action", "OPEN_ADMIN_ACTIVITY")

            jsonBody.put("to", "/topics/admin_citas") // Envia a todos los admins suscritos al tema
            jsonBody.put("notification", notification)
            jsonBody.put("priority", "high")

            val request = object : JsonObjectRequest(Request.Method.POST, url, jsonBody,
                { Log.d("FCM", "Notificación enviada OK") },
                { error -> Log.e("FCM", "Error enviando notificación: $error") }
            ) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-Type"] = "application/json"
                    headers["Authorization"] = "key=$SERVER_KEY"
                    return headers
                }
            }
            queue.add(request)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}