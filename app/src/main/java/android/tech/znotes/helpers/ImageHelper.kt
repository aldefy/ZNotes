package android.tech.znotes.helpers

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

fun ImageView.loadUrl(url: String, placeholderId: Int = 0) {
    if (url.isNotBlank()) {
        if (placeholderId != 0) {
            Glide.with(context).load(url).apply(RequestOptions().placeholder(placeholderId)).into(this)
        } else {
            Glide.with(context).load(url).into(this)
        }
    }
}
