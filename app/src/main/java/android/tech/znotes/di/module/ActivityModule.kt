package wellthy.care.di.module

import android.tech.znotes.feature.detail.NotesDetailsActivity
import android.tech.znotes.feature.notes.NotesListActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector
    internal abstract fun contributeNotesListActivity(): NotesListActivity

    @ContributesAndroidInjector
    internal abstract fun contributeNotesDetailsActivity(): NotesDetailsActivity
}
