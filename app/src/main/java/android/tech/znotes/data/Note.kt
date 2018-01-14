package android.tech.znotes.data

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "note")
data class Note(
    /**
     * Note Title
     */
    var title: String,
    /**
     * Really long user note in string
     */
    var note: String,
    /**
     * Can be photo url or local path
     */
    var photoPath: String,
    /**
     * mills in Long from DateTime
     */
    var recordedAt: Long
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    constructor() : this("", "", "", -1)
}
