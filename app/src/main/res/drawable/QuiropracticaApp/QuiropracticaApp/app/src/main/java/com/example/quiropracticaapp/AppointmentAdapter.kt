package com.example.quiropracticaapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// Adaptador para mostrar la lista de citas
class AppointmentAdapter(private val appointmentList: List<Appointment>) :
    RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder>() {

    class AppointmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Enlazar a los IDs de item_appointment.xml
        val nameTextView: TextView = itemView.findViewById(R.id.text_appointment_client_name)
        val datetimeTextView: TextView = itemView.findViewById(R.id.text_appointment_datetime)
        val notesTextView: TextView = itemView.findViewById(R.id.text_appointment_notes)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        // Usar el layout item_appointment.xml
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_appointment, parent, false)
        return AppointmentViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
        val currentAppointment = appointmentList[position]

        holder.nameTextView.text = currentAppointment.clientName
        holder.datetimeTextView.text = "Fecha: ${currentAppointment.date} - Hora: ${currentAppointment.time}"

        // Mostrar notas solo si existen
        if (currentAppointment.notes.isEmpty()) {
            holder.notesTextView.visibility = View.GONE
        } else {
            holder.notesTextView.visibility = View.VISIBLE
            holder.notesTextView.text = "Notas: ${currentAppointment.notes}"
        }
    }

    override fun getItemCount() = appointmentList.size
}