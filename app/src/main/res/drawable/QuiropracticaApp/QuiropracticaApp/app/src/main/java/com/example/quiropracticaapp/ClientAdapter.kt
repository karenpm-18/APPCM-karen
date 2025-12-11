package com.example.quiropracticaapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * Adaptador para mostrar una lista de objetos Client en un RecyclerView.
 *
 * @param clients La lista de clientes a mostrar.
 */
class ClientAdapter(private val clients: List<Client>) :
    RecyclerView.Adapter<ClientAdapter.ClientViewHolder>() {

    // 1. ViewHolder: Define los elementos de la vista de cada fila (item)
    inner class ClientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Enlaza los elementos de tu futura vista de item (por ahora, solo un TextView)
        val nameTextView: TextView = itemView.findViewById(R.id.text_client_name)
        val phoneTextView: TextView = itemView.findViewById(R.id.text_client_phone)
    }

    // 2. Crea la vista (infla el layout del item)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientViewHolder {
        // Aquí debes inflar el layout de una sola fila de cliente (item_client.xml, que crearemos después)
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_client, parent, false)
        return ClientViewHolder(view)
    }

    // 3. Rellena la vista con los datos del objeto Client
    override fun onBindViewHolder(holder: ClientViewHolder, position: Int) {
        val client = clients[position]
        holder.nameTextView.text = client.name
        holder.phoneTextView.text = "Tel: ${client.phone}"

        // Futuro: Aquí puedes añadir un listener al item completo si quieres ver detalles
    }

    // 4. Retorna el número total de elementos
    override fun getItemCount(): Int = clients.size
}