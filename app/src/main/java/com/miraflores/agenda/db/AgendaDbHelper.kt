package com.miraflores.agenda.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.miraflores.agenda.model.Appointment

class AgendaDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "Agenda.db"
        const val DATABASE_VERSION = 4

        // TABLA CITAS
        const val TABLE_APPOINTMENTS = "appointments"
        const val COL_ID = "id"
        const val COL_NAME = "client_name"
        const val COL_PHONE = "phone"
        const val COL_AGE = "age"
        const val COL_SERVICE = "service"
        const val COL_DATE = "date"
        const val COL_TIME = "time"
        const val COL_NOTES = "notes"
        const val COL_STATUS = "status"

        // TABLA CLIENTES
        const val TABLE_CLIENTS = "clients"
        const val COL_EMAIL = "email"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE $TABLE_APPOINTMENTS ($COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COL_NAME TEXT, $COL_PHONE TEXT, $COL_AGE TEXT, $COL_SERVICE TEXT, $COL_DATE TEXT, $COL_TIME TEXT, $COL_NOTES TEXT, $COL_STATUS TEXT)")
        db.execSQL("CREATE TABLE $TABLE_CLIENTS ($COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COL_NAME TEXT, $COL_PHONE TEXT, $COL_EMAIL TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_APPOINTMENTS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CLIENTS")
        onCreate(db)
    }

    // --- VALIDACIÓN DISPONIBILIDAD ---
    fun isSlotAvailable(date: String, time: String, currentId: Int): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT $COL_ID FROM $TABLE_APPOINTMENTS WHERE $COL_DATE = ? AND $COL_TIME = ? AND $COL_STATUS != 'RECHAZADO' AND $COL_ID != ?", arrayOf(date, time, currentId.toString()))
        val count = cursor.count
        cursor.close()
        // No cerramos db aquí si planeamos usarla inmediatamente después, pero es buena práctica en helpers simples.
        return count == 0
    }

    // --- CRUD CITAS ---
    fun insertAppointment(appt: Appointment): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COL_NAME, appt.clientName)
            put(COL_PHONE, appt.phone)
            put(COL_AGE, appt.age)
            put(COL_SERVICE, appt.service)
            put(COL_DATE, appt.date)
            put(COL_TIME, appt.time)
            put(COL_NOTES, appt.notes)
            put(COL_STATUS, "PENDIENTE")
        }
        val id = db.insert(TABLE_APPOINTMENTS, null, values)
        db.close()
        return id
    }

    fun updateAppointment(appt: Appointment): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COL_NAME, appt.clientName)
            put(COL_PHONE, appt.phone)
            put(COL_AGE, appt.age)
            put(COL_SERVICE, appt.service)
            put(COL_DATE, appt.date)
            put(COL_TIME, appt.time)
            put(COL_NOTES, appt.notes)
            // No actualizamos STATUS aquí para no reiniciar una cita confirmada a pendiente por error
        }
        val rows = db.update(TABLE_APPOINTMENTS, values, "$COL_ID=?", arrayOf(appt.id.toString()))
        db.close()
        return rows
    }

    fun getAppointmentById(id: Int): Appointment? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_APPOINTMENTS WHERE $COL_ID = ?", arrayOf(id.toString()))
        var appt: Appointment? = null
        if (cursor.moveToFirst()) appt = mapCursor(cursor)
        cursor.close()
        db.close()
        return appt
    }

    fun getAllAppointments(): List<Appointment> {
        val list = ArrayList<Appointment>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_APPOINTMENTS ORDER BY $COL_ID DESC", null)
        if (cursor.moveToFirst()) {
            do { list.add(mapCursor(cursor)) } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return list
    }

    fun getAppointmentsByService(service: String): List<Appointment> {
        val list = ArrayList<Appointment>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_APPOINTMENTS WHERE $COL_SERVICE = ? ORDER BY $COL_ID DESC", arrayOf(service))
        if (cursor.moveToFirst()) {
            do { list.add(mapCursor(cursor)) } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return list
    }

    fun updateStatus(id: Int, status: String) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COL_STATUS, status)
        db.update(TABLE_APPOINTMENTS, values, "$COL_ID=?", arrayOf(id.toString()))
        db.close()
    }

    fun deleteAppointment(id: Int): Int {
        val db = this.writableDatabase
        val res = db.delete(TABLE_APPOINTMENTS, "$COL_ID=?", arrayOf(id.toString()))
        db.close()
        return res
    }

    private fun mapCursor(cursor: android.database.Cursor): Appointment {
        return Appointment(
            id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
            clientName = cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME)) ?: "",
            phone = cursor.getString(cursor.getColumnIndexOrThrow(COL_PHONE)) ?: "",
            age = cursor.getString(cursor.getColumnIndexOrThrow(COL_AGE)) ?: "",
            service = cursor.getString(cursor.getColumnIndexOrThrow(COL_SERVICE)) ?: "",
            date = cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE)) ?: "",
            time = cursor.getString(cursor.getColumnIndexOrThrow(COL_TIME)) ?: "",
            notes = cursor.getString(cursor.getColumnIndexOrThrow(COL_NOTES)) ?: "",
            status = cursor.getString(cursor.getColumnIndexOrThrow(COL_STATUS)) ?: "PENDIENTE"
        )
    }
}