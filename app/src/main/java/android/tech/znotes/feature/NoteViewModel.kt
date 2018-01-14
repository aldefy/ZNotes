package android.tech.znotes.feature

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.graphics.Bitmap
import android.tech.znotes.data.Note
import android.tech.znotes.data.NotesRepository
import android.tech.znotes.data.UploadResult
import javax.inject.Inject

class NoteViewModel @Inject constructor(var repo: NotesRepository) : ViewModel() {

    fun getAllNotes(sort: Boolean): LiveData<List<Note>> {
        return repo.getAllNotes(sort = sort)
    }

    fun addNote(note: Note): LiveData<Note> {
        return repo.addNote(note = note)
    }

    fun uploadNotePhoto(title: String, bitmap: Bitmap): LiveData<UploadResult> {
        return repo.uploadPicture(bitmap = bitmap, title = title)
    }
}
