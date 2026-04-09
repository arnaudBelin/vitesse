package com.example.vitesse.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.vitesse.data.dao.CandidateDao
import com.example.vitesse.data.db.VitesseDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): VitesseDB {
        return Room.databaseBuilder(context, VitesseDB::class.java, "vitesse_db")
            .addCallback(
                object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        val now = System.currentTimeMillis()

                        db.execSQL(
                            """
                            INSERT INTO Candidate 
                            (first_name, last_name, phone, email, picture_uri, birth_date, salary, note, is_favorite, created_at)
                            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                            """.trimIndent(),
                            arrayOf<Any?>(
                                "John",
                                "Doe",
                                "1234567890",
                                "john@example.com",
                                null,
                                "1972-01-01",
                                8000.0,
                                "A good candidate but expensive",
                                1,
                                now
                            )
                        )

                        db.execSQL(
                            """
                            INSERT INTO Candidate 
                            (first_name, last_name, phone, email, picture_uri, birth_date, salary, note, is_favorite, created_at)
                            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                            """.trimIndent(),
                            arrayOf<Any?>(
                                "Jane",
                                "Smith",
                                "0987654321",
                                "jane@example.com",
                                null,
                                "1985-05-15",
                                6000.0,
                                "A promising candidate with a good track record",
                                0,
                                now + 1
                            )
                        )
                    }
                }
            )
            .build()
    }

    @Provides
    fun provideCandidateDao(database: VitesseDB): CandidateDao =
        database.candidateDao()


}
