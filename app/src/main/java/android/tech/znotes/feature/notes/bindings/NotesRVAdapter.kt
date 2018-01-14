package android.tech.znotes.feature.notes.bindings

import android.support.v7.widget.RecyclerView
import android.tech.znotes.R
import android.tech.znotes.data.Note
import android.tech.znotes.helpers.RVAdapterItemClickListener
import android.tech.znotes.helpers.getEllapsedTime
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.joda.time.DateTime

class NotesRVAdapter(items: List<Note>) : RecyclerView.Adapter<NotesRVAdapter.ViewHolder>() {
    var mutableList = items

    override fun getItemCount(): Int {
        return mutableList.size
    }

    var rvAdapterItemClickListener: RVAdapterItemClickListener? = null
    fun setOnItemClickListener(rvAdapterItemClickListener: RVAdapterItemClickListener) {
        this.rvAdapterItemClickListener = rvAdapterItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_rv_note, parent, false), rvAdapterItemClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        mutableList[position].apply {
            holder.tvNoteTitle.text = title
            holder.tvNoteText.text = note
            holder.tvEllapsed.text = DateTime(recordedAt).getEllapsedTime()
        }
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.itemView.clearAnimation()
    }

    class ViewHolder(itemView: View, rvAdapterItemClickListener: RVAdapterItemClickListener?) : RecyclerView.ViewHolder(itemView) {
        var tvNoteTitle: TextView = itemView.findViewById(R.id.tvNoteTitle)
        var tvNoteText: TextView = itemView.findViewById(R.id.tvNoteText)
        var tvEllapsed: TextView = itemView.findViewById(R.id.tvEllapsed)

        init {
            itemView.setOnClickListener { rvAdapterItemClickListener?.onClick(adapterPosition, it) }
        }
    }
}

