package com.example.quiropracticaapp

// Este objeto actúa como una base de datos estática global (Singleton)
object Database {

    // =========================================================
    // LÓGICA DE CLIENTES
    // =========================================================

    // La lista real de clientes, inicializada con Ana y Luis
    val clientList: MutableList<Client> = mutableListOf(
        Client(id = 1, name = "Ana Guzmán", phone = "5512345678", email = "ana.guzman@example.com"),
        Client(id = 2, name = "Luis Pérez", phone = "5587654321", email = "luis.perez@example.com")
    )

    // Función para añadir un nuevo cliente a la base de datos
    fun addClient(client: Client) {
        clientList.add(client)
    }

    // =========================================================
    // LÓGICA DE CITAS (Appointments)
    // =========================================================

    // Lista de Citas, usando el modelo Appointment
    val appointmentList: MutableList<Appointment> = mutableListOf()

    // Función para añadir una nueva cita
    fun addAppointment(appointment: Appointment) {
        appointmentList.add(appointment)
    }
}