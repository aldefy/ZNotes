package android.tech.znotes.feature.detail

import android.app.Dialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.tech.znotes.R
import android.tech.znotes.ViewModelFactory
import android.tech.znotes.data.Note
import android.tech.znotes.feature.NoteViewModel
import android.tech.znotes.helpers.loadUrl
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
        fun newIntent(context: Context, note: Note? = null): Intent {
            return Intent(context, NotesDetailsActivity::class.java).apply {
                note?.let {
                    putExtra("note", note)
                }
            }
        }
    }

    @Inject lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by lazy { ViewModelProviders.of(this@NotesDetailsActivity, viewModelFactory).get(NoteViewModel::class.java) }
    private lateinit var note: Note
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this@NotesDetailsActivity)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_note)
        initData()
        initViews()
    }

    private fun initData() {
        note = if (intent.hasExtra("note")) {
            intent.extras.getParcelable("note")
        } else
            Note()
    }

    lateinit var progressDialog: Dialog
    private lateinit var photoUploadedUrl: String

    private fun initViews() {
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }
        progressDialog = Dialog(this@NotesDetailsActivity, R.style.ProgressDialog)
        progressDialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progressDialog.setContentView(layoutInflater.inflate(R.layout.layout_progress_fullscreen, null))
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.setCancelable(false)

        photoUploadedUrl = ""
        etNoteTitle.setText(note.title)
        etNoteContent.setText(note.note)
        etNoteContent.setSelection(note.note.length)
        etNoteTitle.setSelection(note.title.length)
        if (note.photoPath.isNotBlank())
            ivNotePhoto.loadUrl(note.photoPath)
        ivNotePhoto.isDrawingCacheEnabled = true
        btnSave.setOnClickListener {
            if (!note.photoPath.contains("http")) {
                if (ivNotePhoto.drawable != null) {
                    ivNotePhoto.buildDrawingCache()
                    val bitmap = ivNotePhoto.drawingCache
                    progressDialog.show()
                    viewModel.uploadNotePhoto(bitmap = bitmap, title = note.title).observe(this,
                        Observer { uploadResult ->
                            run {
                                uploadResult?.error?.let {
                                    progressDialog.dismiss()
                                    Toast.makeText(this@NotesDetailsActivity, it.message, Toast.LENGTH_SHORT).show()
                                }
                                uploadResult?.resultUrl?.let {
                                    photoUploadedUrl = it.toString()
                                    validateAndSaveNote()
                                }
                            }
                        }
                    )
                } else {
                    validateAndSaveNote()
                }
            } else
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
        note.apply {
            title = etNoteTitle.text.toString()
            note = etNoteContent.text.toString()
            photoPath = photoUploadedUrl
            recordedAt = DateTime().millis
        }
        viewModel.addNote(note).observe(
            this,
            Observer {
                progressDialog.dismiss()
                finish()
            }
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
