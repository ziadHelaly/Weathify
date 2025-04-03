package eg.edu.iti.weathify.alarm.viewModel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import eg.edu.iti.weathify.core.model.repo.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class AlarmViewModel(val repository: WeatherRepository):ViewModel() {
    private val _selectedDateTime = MutableStateFlow<LocalDateTime?>(null)
    val selectedDateTime: StateFlow<LocalDateTime?> = _selectedDateTime

    private val _inputData = MutableStateFlow("")
    val inputData: StateFlow<String> = _inputData

    private val _timeDifferenceMinutes = MutableStateFlow<Long?>(null)
    val timeDifferenceMinutes: StateFlow<Long?> = _timeDifferenceMinutes

    private val _isTimeValid = MutableStateFlow(true)
    val isTimeValid: StateFlow<Boolean> = _isTimeValid

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateDate(year: Int, month: Int, day: Int) {
        val currentDateTime = _selectedDateTime.value ?: LocalDateTime.now()
        _selectedDateTime.value = LocalDateTime.of(year, month + 1, day, currentDateTime.hour, currentDateTime.minute)
        validateTime()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateTime(hour: Int, minute: Int) {
        val currentDateTime = _selectedDateTime.value ?: LocalDateTime.now()
        _selectedDateTime.value = LocalDateTime.of(currentDateTime.year, currentDateTime.month, currentDateTime.dayOfMonth, hour, minute)
        validateTime()
    }

    fun updateInputData(data: String) {
        _inputData.value = data
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun validateTime() {
        val now = LocalDateTime.now()
        val selectedTime = _selectedDateTime.value
        if (selectedTime != null) {
            val difference = ChronoUnit.MINUTES.between(now, selectedTime)
            _timeDifferenceMinutes.value = difference
            _isTimeValid.value = difference > 0
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getResult(): String {
        return _selectedDateTime.value?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) ?: "N/A"
    }
}