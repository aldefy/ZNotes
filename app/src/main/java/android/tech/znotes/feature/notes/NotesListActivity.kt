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
import android.tech.znotes.feature.detail.NotesDetailsActivity
import android.tech.znotes.feature.notes.bindings.NotesRVAdapter
import android.tech.znotes.helpers.DividerItemDecoration
import android.tech.znotes.helpers.RVAdapterItemClickListener
import android.view.Menu
import android.view.View
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_list_notes.*
import javax.inject.Inject

class NotesListActivity : AppCompatActivity(), RVAdapterItemClickListener {

    private lateinit var notesList: ArrayList<Note>
    @Inject lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by lazy { ViewModelProviders.of(this@NotesListActivity, viewModelFactory).get(NoteViewModel::class.java) }
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this@NotesListActivity)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_notes)
        notesList = ArrayList()
        adapter = NotesRVAdapter(notesList)
        adapter.setOnItemClickListener(this@NotesListActivity)
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
        initViews()
    }

    private fun initViews() {
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Notes"
        }
        fab.setOnClickListener {
            startActivity(NotesDetailsActivity.newIntent(this@NotesListActivity))
            //   viewModel.addNote(Note("Note 1", "Some fancy text", "", DateTime().millis))
        }
        setupRecyclerView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_notes_list, menu)
        return true
    }

    private lateinit var adapter: NotesRVAdapter
    private fun setupRecyclerView() {
        rvNotes.layoutManager = LinearLayoutManager(this@NotesListActivity)
        rvNotes.addItemDecoration(DividerItemDecoration(this@NotesListActivity))
        rvNotes.adapter = adapter
    }

    override fun onClick(pos: Int) {
    }

    override fun onClick(pos: Int, view: View) {
    }
}
