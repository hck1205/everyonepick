package com.everyonepick.core.di

import android.content.Context
import androidx.room.Room
import com.everyonepick.core.data.NoteRepository
import com.everyonepick.core.data.OfflineNoteRepository
import com.everyonepick.core.database.EveryonePickDatabase
import com.everyonepick.core.database.QuickNoteDao
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindNoteRepository(
        repository: OfflineNoteRepository,
    ): NoteRepository
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): EveryonePickDatabase =
        Room.databaseBuilder(
            context,
            EveryonePickDatabase::class.java,
            "everyonepick.db",
        ).build()

    @Provides
    fun provideQuickNoteDao(
        database: EveryonePickDatabase,
    ): QuickNoteDao = database.quickNoteDao()
}
