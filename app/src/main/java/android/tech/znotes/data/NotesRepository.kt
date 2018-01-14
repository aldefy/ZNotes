package android.tech.znotes.data

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class NotesRepository(var db: NotesDatabase) {

    /**
     * Gets all notes from db
     */
    fun getAllNotes(): LiveData<List<Note>> {
        return db.noteDao().getNotes()
    }

    fun addNote(note: Note): LiveData<Note> {
        val mutableLiveData = MutableLiveData<Note>()
        Single.fromCallable {
            db.noteDao().insert(note)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ id ->
                Timber.tag("DB").d("inserted record with $id")
                note.id = id
                mutableLiveData.value = note
            }, { e -> e.printStackTrace() })
        return mutableLiveData
    }
}
