package android.tech.znotes.feature.notes

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.tech.znotes.R
import android.tech.znotes.ViewModelFactory
import android.tech.znotes.data.Note
import android.tech.znotes.feature.NoteViewModel
import android.tech.znotes.feature.notes.bindings.NotesRVAdapter
import android.tech.znotes.helpers.DividerItemDecoration
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_list_notes.*
import javax.inject.Inject

class NotesListActivity : AppCompatActivity() {

    private lateinit var notesList: ArrayList<Note>
    @Inject lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by lazy { ViewModelProviders.of(this@NotesListActivity, viewModelFactory).get(NoteViewModel::class.java) }
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this@NotesListActivity)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_notes)
        notesList = ArrayList()
        adapter = NotesRVAdapter(notesList)
        viewModel.getAllNotes().observe(
            this,
            Observer { data ->
                run {
                    notesList.clear()
                    notesList.addAll(data!!)
                    adapter.notifyDataSetChanged()
                }
            }
        )
        setupRecyclerView()
    }

    private lateinit var adapter: NotesRVAdapter
    private fun setupRecyclerView() {
        rvNotes.layoutManager = LinearLayoutManager(this@NotesListActivity)
        rvNotes.addItemDecoration(DividerItemDecoration(this@NotesListActivity))
        rvNotes.adapter = adapter
    }
}
