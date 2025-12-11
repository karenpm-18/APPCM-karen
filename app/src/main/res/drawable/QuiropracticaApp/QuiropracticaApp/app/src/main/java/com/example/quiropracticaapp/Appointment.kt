package com.example.quiropracticaapp

// Modelo de datos para una Cita
data class Appointment(
    val id: Int,
    val clientName: String,
    val date: String,
    val time: String,
    val notes: String
)