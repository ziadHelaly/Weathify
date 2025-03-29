package eg.edu.iti.weathify.utils

class Constants {
    companion object{
        const val BASE_URL="https://api.openweathermap.org/data/3.0/"
        const val permissionRequestCode = 5005
        fun imageLink(icon:String)="https://openweathermap.org/img/wn/$icon@2x.png"

    }
}