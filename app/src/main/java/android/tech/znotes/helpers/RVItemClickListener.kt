package android.tech.znotes.helpers

import android.view.View

interface RVAdapterItemClickListener {
    fun onClick(pos: Int)
    fun onClick(pos: Int, view: View)
}
