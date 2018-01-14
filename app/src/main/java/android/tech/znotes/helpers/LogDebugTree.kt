package android.tech.znotes.helpers

import com.orhanobut.logger.Logger
import timber.log.Timber
import java.util.regex.Pattern

class LogDebugTree : Timber.Tree() {

    private val CALL_STACK_INDEX = 5
    private val ANONYMOUS_CLASS = Pattern.compile("(\\$\\d+)+$")

    override fun log(priority: Int, tag: String?, message: String?, t: Throwable?) {
        Logger.log(priority, tag, prependCallLocation(message!!), t)
    }

    /**
     * Prepares our log message with Class hyperlink
     */
    private fun prependCallLocation(message: String): String {
        // DO NOT switch this to Thread.getCurrentThread().getStackTrace(). The test will pass
        // because Robolectric runs them on the JVM but on Android the elements are different.
        val stackTrace = Throwable().stackTrace
        if (stackTrace.size <= CALL_STACK_INDEX) {
            throw IllegalStateException(
                "Synthetic stacktrace didn't have enough elements: are you using proguard?")
        }
        val clazz = extractClassName(stackTrace[CALL_STACK_INDEX])
        val lineNumber = stackTrace[CALL_STACK_INDEX].lineNumber
        return "($clazz.java:$lineNumber) - $message"
    }

    /**
     * Extract the class name without any anonymous class suffixes (e.g., `Foo$1`
     * becomes `Foo`).
     */
    private fun extractClassName(element: StackTraceElement): String {
        var tag = element.className
        val m = ANONYMOUS_CLASS.matcher(tag)
        if (m.find()) {
            tag = m.replaceAll("")
        }
        return tag.substring(tag.lastIndexOf('.') + 1)
    }
}
