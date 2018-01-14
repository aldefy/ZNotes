package android.tech.znotes.data

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.graphics.Bitmap
import com.google.firebase.storage.FirebaseStorage
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.io.ByteArrayOutputStream

class NotesRepository(var db: NotesDatabase) {

    /**
     * Gets all notes from db
     */
    fun getAllNotes(sort: String): LiveData<List<Note>> {
        return when (sort) {
            "Title ascending" -> db.noteDao().getNotesAscByTitle()
            "Title descending" -> db.noteDao().getNotesDescByTitle()
            "Created at ascending" -> db.noteDao().getNotesAscByDate()
            "Created at descending" -> db.noteDao().getNotesDescByDate()
            else -> db.noteDao().getNotesByPhotoAttachments()
        }
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

    fun uploadPicture(title: String, bitmap: Bitmap): MutableLiveData<UploadResult> {
        val mutableLiveData = MutableLiveData<UploadResult>()
        val uploadResult = UploadResult()
        val storageRef = FirebaseStorage.getInstance().reference.child("images/note_" + title)
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, baos)
        val data = baos.toByteArray()
        val uploadTask = storageRef.putBytes(data)
        uploadTask.addOnFailureListener({ error ->
            // Handle unsuccessful uploads
            error.printStackTrace()
            uploadResult.error = error
            mutableLiveData.value = uploadResult
        }).addOnSuccessListener({ taskSnapshot ->
            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
            val downloadUrl = taskSnapshot.downloadUrl
            uploadResult.resultUrl = downloadUrl
            mutableLiveData.value = uploadResult
        })
        return mutableLiveData
    }

    fun deleteNote(note: Note): LiveData<Int> {
        val mutableLiveData = MutableLiveData<Int>()
        Single.fromCallable {
            val photoRef = FirebaseStorage.getInstance().getReferenceFromUrl(note.photoPath)
            photoRef.delete()
            db.noteDao().delete(note)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ noRowsDeleted ->
                Timber.tag("DB").d("deleted record with $noRowsDeleted number of rows deleted")
                mutableLiveData.value = noRowsDeleted
            }, { e -> e.printStackTrace() })
        return mutableLiveData
    }
}
