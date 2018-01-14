package android.tech.znotes

import android.app.Activity
import android.content.Context
import android.support.multidex.MultiDexApplication
import android.tech.znotes.di.component.AppComponent
import android.tech.znotes.di.component.DaggerAppComponent
import android.tech.znotes.helpers.LogDebugTree
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import net.danlew.android.joda.JodaTimeAndroid
import timber.log.Timber
import javax.inject.Inject

class NotesApp : MultiDexApplication(), HasActivityInjector {

    companion object {
        lateinit var component: AppComponent
        fun applicationContext(): Context {
            return this.applicationContext()
        }

        fun getAppComponent(): AppComponent {
            return component
        }
    }

    @Inject lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>
    override fun activityInjector(): DispatchingAndroidInjector<Activity> = dispatchingAndroidInjector

    override fun onCreate() {
        super.onCreate()
        JodaTimeAndroid.init(this)
        component = DaggerAppComponent
            .builder()
            .application(this@NotesApp)
            .build()
        component.inject(this)
        initLogging()
    }

    private fun initLogging() {
        if (BuildConfig.DEBUG) {
            val formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(true)
                .methodOffset(5)
                .tag("Notes")
                .build()
            Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))
            Timber.plant(LogDebugTree())
        }
    }
}
