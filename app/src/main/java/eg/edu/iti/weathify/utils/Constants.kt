package eg.edu.iti.weathify.utils

class Constants {
    companion object {
        const val BASE_URL = "https://api.openweathermap.org/"
        const val SEARCH_URL = "https://nominatim.openstreetmap.org/"
        const val permissionRequestCode = 5005
        fun imageLink(icon: String) = "https://openweathermap.org/img/wn/$icon@2x.png"
        const val TEMP_KEY = "TEMP_KEY"
        const val WIND_KEY = "WIND_KEY"
        const val LOCATION_KEY = "LOCATION_KEY"
        const val LANGUAGE_KEY = "LANGUAGE_KEY"
        const val LATITUDE_KEY = "LATITUDE_KEY"
        const val LONGITUDE_KEY = "LONGITUDE_KEY"

    }
}