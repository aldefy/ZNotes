package android.tech.znotes.feature

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.tech.znotes.data.Note
import android.tech.znotes.data.NotesRepository
import javax.inject.Inject

class NoteViewModel @Inject constructor(var repo: NotesRepository) : ViewModel() {

    fun getAllNotes(): LiveData<List<Note>> {
        return repo.getAllNotes()
    }
}
