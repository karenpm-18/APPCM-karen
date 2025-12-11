package com.example.quiropracticaapp

/**
 * Clase de modelo (data class) para representar un Cliente/Paciente.
 * El uso de 'data class' es ideal para clases que solo almacenan datos.
 */
data class Client(
    // ID único para cada cliente
    val id: Int,
    // Nombre completo del paciente
    val name: String,
    // Número de teléfono
    val phone: String,
    // Correo electrónico (opcional)
    val email: String? = null
)