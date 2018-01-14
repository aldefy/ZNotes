package android.tech.znotes.feature.detail

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.tech.znotes.R
import android.tech.znotes.ViewModelFactory
import android.tech.znotes.feature.NoteViewModel
import android.view.MenuItem
import dagger.android.AndroidInjection
import javax.inject.Inject

class NotesDetailsActivity : AppCompatActivity() {
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
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.home) {
            onBackPressed()
            true
        } else
            super.onOptionsItemSelected(item)
    }
}
