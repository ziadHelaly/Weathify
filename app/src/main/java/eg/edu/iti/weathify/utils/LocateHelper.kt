package eg.edu.iti.weathify.utils

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

class LocaleHelper {
    companion object {
        fun wrapContext(context: Context, languageCode: String): Context {
            val locale = Locale(languageCode)
            Locale.setDefault(locale)

            val config = Configuration(context.resources.configuration)
            config.setLocale(locale)

            return context.createConfigurationContext(config)
        }

        fun getSavedLanguage(context: Context): String {
            val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
            return prefs.getString("app_language", "en") ?: "en"
        }
        fun saveLanguagePreference(context: Context, languageCode: String) {
            val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
            prefs.edit().putString("app_language", languageCode).apply()
        }
    }
}
