package com.scanner.phonenumber.di

import android.content.Context
import androidx.room.Room
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.chinese.ChineseTextRecognizerOptions
import com.scanner.phonenumber.data.database.AppDatabase
import com.scanner.phonenumber.data.database.ContactDao
import com.scanner.phonenumber.data.database.HistoryDao
import com.scanner.phonenumber.domain.util.PhoneNumberParser
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "phone_scanner_database"
        ).build()
    }

    @Provides
    fun provideContactDao(database: AppDatabase): ContactDao {
        return database.contactDao()
    }

    @Provides
    fun provideHistoryDao(database: AppDatabase): HistoryDao {
        return database.historyDao()
    }

    @Provides
    @Singleton
    fun provideTextRecognizer() = TextRecognition.getClient(ChineseTextRecognizerOptions.Builder().build())

    @Provides
    @Singleton
    fun providePhoneNumberParser() = PhoneNumberParser()
}