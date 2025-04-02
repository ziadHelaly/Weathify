package eg.edu.iti.weathify.utils


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import java.util.Locale

fun updateLocale(activity: Activity, languageCode: String) {
    val locale = Locale(languageCode)
    Locale.setDefault(locale)

    val config = Configuration(activity.resources.configuration)
    config.setLocale(locale)

    activity.resources.updateConfiguration(config, activity.resources.displayMetrics)

    activity.recreate()
}
