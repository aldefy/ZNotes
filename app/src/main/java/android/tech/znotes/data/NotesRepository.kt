package android.tech.znotes.data

import android.arch.lifecycle.LiveData

class NotesRepository(var db: NotesDatabase) {

    /**
     * Gets all notes from db
     */
    fun getAllNotes(): LiveData<List<Note>> {
        return db.noteDao().getNotes()
    }
}
