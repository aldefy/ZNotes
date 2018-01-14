package android.tech.znotes.feature.notes

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AlertDialog
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
import android.widget.ArrayAdapter
import com.miguelcatalan.materialsearchview.MaterialSearchView
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_list_notes.*
import javax.inject.Inject

class NotesListActivity : AppCompatActivity(), RVAdapterItemClickListener {

    private lateinit var notesList: ArrayList<Note>
    @Inject lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by lazy { ViewModelProviders.of(this@NotesListActivity, viewModelFactory).get(NoteViewModel::class.java) }
    private lateinit var sort: String

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this@NotesListActivity)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_notes)
        notesList = ArrayList()
        adapter = NotesRVAdapter(notesList)
        adapter.setOnItemClickListener(this@NotesListActivity)
        initViews()
        sort = ""
        fetchData(sort)
    }

    private fun fetchData(sort: String) {
        viewModel.getNotes(sort = sort).observe(
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

    private fun showSortDialog() {
        val sortDialogBuilder = AlertDialog.Builder(this@NotesListActivity)
        sortDialogBuilder.setTitle("Sort")

        val arrayAdapter = ArrayAdapter<String>(this@NotesListActivity, android.R.layout.select_dialog_singlechoice)
        arrayAdapter.add("Title ascending")
        arrayAdapter.add("Title descending")
        arrayAdapter.add("Created at ascending")
        arrayAdapter.add("Created at descending")
        arrayAdapter.add("Has Photo attached")
        sortDialogBuilder.setNegativeButton("cancel", { dialog, _ -> dialog.dismiss() })

        sortDialogBuilder.setAdapter(arrayAdapter, { dialogInterface, which ->
            sort = when (which) {
                0 -> "Title ascending"
                1 -> "Title descending"
                2 -> "Created at ascending"
                3 -> "Created at descending"
                4 -> "Has Photo attached"
                else -> {
                    "Title ascending"
                }
            }
            fetchData(sort)
        })
        sortDialogBuilder.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_notes_list, menu)
        val item = menu?.findItem(R.id.actionSearch)
        searchView.setMenuItem(item)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.actionSort) {
            showSortDialog()
            true
        } else
            super.onOptionsItemSelected(item)
    }
}
