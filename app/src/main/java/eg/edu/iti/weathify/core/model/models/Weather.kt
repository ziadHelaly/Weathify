package eg.edu.iti.weathify.core.model.models

data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)