package android.tech.znotes.di.module

import android.arch.lifecycle.ViewModelProvider
import android.arch.persistence.room.Room
import android.content.Context
import android.tech.znotes.NotesApp
import android.tech.znotes.ViewModelFactory
import android.tech.znotes.data.NoteDao
import android.tech.znotes.data.NotesDatabase
import android.tech.znotes.data.NotesRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

// Add application level bindings here, e.g.: RestClientApi, Repository, etc.
@Module(includes = [(ViewModelModule::class)])
class AppModule {

    @Provides
    fun provideContext(application: NotesApp): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideDB(context: Context): NotesDatabase = Room.databaseBuilder(context, NotesDatabase::class.java, "tech-notes")
        .build()

    @Provides
    @Singleton
    fun provideNoteDao(database: NotesDatabase): NoteDao = database.noteDao()

    @Singleton
    @Provides
    fun provideNotesRepository(db: NotesDatabase): NotesRepository {
        return NotesRepository(db = db)
    }

    @Provides
    @Singleton
    fun provideViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory {
        return factory
    }
}
