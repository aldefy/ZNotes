package android.tech.znotes.data

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

@Dao
interface NoteDao {

    @Query("SELECT * FROM note ORDER BY title ASC")
    fun getNotesAscByTitle(): LiveData<List<Note>>

    @Query("SELECT * FROM note ORDER BY title DESC")
    fun getNotesDescByTitle(): LiveData<List<Note>>

    @Query("SELECT * FROM note ORDER BY recordedAt ASC")
    fun getNotesAscByDate(): LiveData<List<Note>>

    @Query("SELECT * FROM note ORDER BY recordedAt DESC")
    fun getNotesDescByDate(): LiveData<List<Note>>

    @Query("SELECT * FROM note WHERE photoPath IS NOT ''")
    fun getNotesByPhotoAttachments(): LiveData<List<Note>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(note: Note): Long

    @Delete()
    fun delete(note: Note): Int

    @Query("DELETE FROM note")
    fun nukeTable()
}
