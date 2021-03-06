package android.tech.znotes.di.component

import android.tech.znotes.NotesApp
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import android.tech.znotes.di.module.ActivityModule
import android.tech.znotes.di.module.AppModule
import javax.inject.Singleton

@Singleton
@Component(modules = [(AppModule::class), (ActivityModule::class), (AndroidSupportInjectionModule::class)])
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: NotesApp): Builder

        fun build(): AppComponent
    }

    fun inject(app: NotesApp)
}
