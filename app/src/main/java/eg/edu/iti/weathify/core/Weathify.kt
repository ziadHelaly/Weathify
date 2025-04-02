package eg.edu.iti.weathify.core

import android.app.Application
import android.content.Context
import eg.edu.iti.weathify.utils.Constants.Companion.LANGUAGE_KEY
import eg.edu.iti.weathify.utils.updateLocale

class Weathify : Application() {
    override fun onCreate() {
        super.onCreate()
//        val language=getSharedPreferences("settings", Context.MODE_PRIVATE).getString(LANGUAGE_KEY, "100000")
//        updateLocale(this,language?.toInt()?:0)
    }
}