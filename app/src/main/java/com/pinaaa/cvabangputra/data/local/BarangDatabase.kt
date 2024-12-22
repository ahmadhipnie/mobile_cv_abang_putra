package com.pinaaa.cvabangputra.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [BarangEntity::class], version = 1, exportSchema = false)
abstract class BarangDatabase : RoomDatabase() {
    abstract fun barangDao(): BarangDao

    companion object{
        @Volatile
        private var INSTANCE: BarangDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): BarangDatabase {
            if (INSTANCE == null) {
                synchronized(BarangDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        BarangDatabase::class.java, "barang_database"
                    )
                        .build()
                }
            }
            return INSTANCE as BarangDatabase
        }
    }
}