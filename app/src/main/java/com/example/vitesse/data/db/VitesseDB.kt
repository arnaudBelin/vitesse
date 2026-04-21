package com.example.vitesse.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.vitesse.data.dao.CandidateDao
import com.example.vitesse.data.entity.Candidate
import com.example.vitesse.data.utils.Converters

const val DATABASE_VERSION = 1

@Database(entities = [Candidate::class], version = DATABASE_VERSION)
@TypeConverters(Converters::class)
abstract class VitesseDB : RoomDatabase() {
    companion object {
        @Volatile private var INSTANCE: VitesseDB? = null

        fun getDatabase(context: Context): VitesseDB {
            return INSTANCE
                ?: synchronized(this) {
                    Room.databaseBuilder(
                            context.applicationContext,
                            VitesseDB::class.java,
                            "vitesse_db",
                        )
                        .build()
                        .also { INSTANCE = it }
                }
        }
    }

    abstract fun candidateDao(): CandidateDao
}
