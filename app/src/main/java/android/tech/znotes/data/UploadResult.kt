package android.tech.znotes.data

import android.net.Uri

data class UploadResult(
    var resultUrl: Uri?,
    var error: Throwable?
) {
    constructor() : this(null, null)
}
