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
import android.view.MenuItem
import com.miguelcatalan.materialsearchview.MaterialSearchView
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_list_notes.*
import javax.inject.Inject

class NotesListActivity : AppCompatActivity(), RVAdapterItemClickListener {

    private lateinit var notesList: ArrayList<Note>
    @Inject lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by lazy { ViewModelProviders.of(this@NotesListActivity, viewModelFactory).get(NoteViewModel::class.java) }
    private var sort: Boolean = true
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this@NotesListActivity)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_notes)
        notesList = ArrayList()
        adapter = NotesRVAdapter(notesList)
        adapter.setOnItemClickListener(this@NotesListActivity)
        initViews()
        fetchData(sort)
    }

    private fun fetchData(sort: Boolean) {
        viewModel.getAllNotes(sort = sort).observe(
            this,
            Observer { data ->
                run {
                    notesList.clear()
                    notesList.addAll(data!!)
                    adapter.updateItems(notesList)
                }
            }
        )
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
        searchView.setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                adapter.filter.filter(newText)
                return false
            }
        })

        searchView.setOnSearchViewListener(object : MaterialSearchView.SearchViewListener {
            override fun onSearchViewShown() {
                fab.hide()
            }

            override fun onSearchViewClosed() {
                fab.show()
                adapter.filter.filter("")
            }
        })
        setupRecyclerView()
    }

    private lateinit var adapter: NotesRVAdapter
    private fun setupRecyclerView() {
        rvNotes.layoutManager = LinearLayoutManager(this@NotesListActivity)
        rvNotes.addItemDecoration(DividerItemDecoration(this@NotesListActivity))
        rvNotes.adapter = adapter
    }

    override fun onClick(pos: Int) {
        startActivity(NotesDetailsActivity.newIntent(this@NotesListActivity, adapter.getItemAtPos(pos)))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_notes_list, menu)
        val item = menu?.findItem(R.id.actionSearch)
        searchView.setMenuItem(item)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.actionSort) {
            sort = !sort
            fetchData(sort)
            true
        } else
            super.onOptionsItemSelected(item)
    }
}
