package android.tech.znotes.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(
    entities = [(Note::class)],
    version = 1,
    exportSchema = true
)
abstract class NotesDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}
