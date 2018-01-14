package android.tech.znotes.helpers

import org.joda.time.DateTime
import org.joda.time.Period
import org.joda.time.format.PeriodFormatterBuilder

fun DateTime.getEllapsedTime(): String {
    val period = Period(this, DateTime())
    val formatter = PeriodFormatterBuilder()
        .appendSeconds().appendSuffix(" seconds ago\n")
        .appendMinutes().appendSuffix(" minutes ago\n")
        .appendHours().appendSuffix(" hours ago\n")
        .appendDays().appendSuffix(" days ago\n")
        .appendWeeks().appendSuffix(" weeks ago\n")
        .appendMonths().appendSuffix(" months ago\n")
        .appendYears().appendSuffix(" years ago\n")
        .printZeroNever()
        .toFormatter()
    return formatter.print(period)
}
