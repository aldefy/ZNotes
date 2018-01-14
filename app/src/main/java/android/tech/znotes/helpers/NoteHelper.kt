package android.tech.znotes.helpers

import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import java.util.TimeZone

fun DateTime.getFormatted(): String {
    return withZone(DateTimeZone.forTimeZone(TimeZone.getDefault())).toString("dd MMM yyyy hh:mm a")
}
