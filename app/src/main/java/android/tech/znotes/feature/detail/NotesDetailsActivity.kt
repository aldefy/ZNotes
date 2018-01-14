package android.tech.znotes.feature.detail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.tech.znotes.R
import android.tech.znotes.ViewModelFactory
import android.tech.znotes.data.Note
import android.tech.znotes.feature.NoteViewModel
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.vansuita.pickimage.bean.PickResult
import com.vansuita.pickimage.bundle.PickSetup
import com.vansuita.pickimage.dialog.PickImageDialog
import com.vansuita.pickimage.listeners.IPickResult
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_detail_note.*
import org.joda.time.DateTime
import javax.inject.Inject

class NotesDetailsActivity : AppCompatActivity(), IPickResult {
    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, NotesDetailsActivity::class.java)
        }
    }

    @Inject lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by lazy { ViewModelProviders.of(this@NotesDetailsActivity, viewModelFactory).get(NoteViewModel::class.java) }
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this@NotesDetailsActivity)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_note)
        initViews()
    }

    private fun initViews() {
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }
        btnSave.setOnClickListener {
            validateAndSaveNote()
        }
    }

    private fun validateAndSaveNote() {
        if (etNoteTitle.text.isEmpty()) {
            etNoteTitle.error = "This field can't be empty"
            return
        }
        if (etNoteContent.text.isEmpty()) {
            etNoteContent.error = "This field can't be empty"
            return
        }
        val note = Note(
            title = etNoteTitle.text.toString(),
            note = etNoteContent.text.toString(),
            photoPath = "",
            recordedAt = DateTime().millis
        )
        viewModel.addNote(note).observe(
            this,
            Observer { finish() }
        )
    }

    override fun onPickResult(r: PickResult?) {
        r?.error?.let {
            Toast.makeText(this, it.message, Toast.LENGTH_LONG).show();
        }
        r?.bitmap?.let {
            ivNotePhoto.setImageBitmap(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_note_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.home) {
            onBackPressed()
            true
        } else if (item.itemId == R.id.actionAttachImage) {
            PickImageDialog.build(PickSetup()).show(this@NotesDetailsActivity)
            true
        } else
            super.onOptionsItemSelected(item)
    }
}
