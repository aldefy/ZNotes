package android.tech.znotes.data

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

@Dao
interface NoteDao {

    @Query("SELECT * FROM note ORDER BY title ASC")
    fun getNotesAsc(): LiveData<List<Note>>

    @Query("SELECT * FROM note ORDER BY title DESC")
    fun getNotesDesc(): LiveData<List<Note>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(note: Note): Long

    @Query("DELETE FROM note")
    fun nukeTable()
}
