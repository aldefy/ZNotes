package android.tech.znotes.helpers

import android.view.View

interface RVAdapterItemClickListener {
    fun onClick(pos: Int) = Any()
    fun onClick(pos: Int, view: View) = Any()
}
