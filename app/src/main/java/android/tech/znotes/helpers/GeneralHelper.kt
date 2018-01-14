package android.tech.znotes.helpers

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.view.View
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

fun ImageView.greyFilter() {
    val matrix = ColorMatrix()
    matrix.setSaturation(0f)  //0 means grayscale
    val cf = ColorMatrixColorFilter(matrix)
    colorFilter = cf
    imageAlpha = 128   // 128 = 0.5
}

fun ImageView.colorFilter() {
    colorFilter = null;
    imageAlpha = 255;
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun View.toggle() {
    if (visibility != View.VISIBLE)
        visibility = View.VISIBLE
    else
        visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

