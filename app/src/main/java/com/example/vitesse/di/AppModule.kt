package com.example.vitesse.di

import android.content.Context
import androidx.room.Room
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
        return Room.databaseBuilder(context, VitesseDB::class.java, "vitesse_db").build()
    }

    @Provides
    fun provideAnimalDao(database: VitesseDB): CandidateDao =
        database.candidateDao()


}
