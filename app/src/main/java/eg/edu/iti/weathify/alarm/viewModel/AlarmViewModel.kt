package eg.edu.iti.weathify.alarm.viewModel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eg.edu.iti.weathify.R
import eg.edu.iti.weathify.core.model.models.Alarm
import eg.edu.iti.weathify.core.model.models.FavouritePlace
import eg.edu.iti.weathify.core.model.repo.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class AlarmViewModel(val repository: WeatherRepository) : ViewModel() {
    private val _selectedDateTime = MutableStateFlow<LocalDateTime?>(null)
    val selectedDateTime: StateFlow<LocalDateTime?> = _selectedDateTime

    private val _inputData = MutableStateFlow("")
    val inputData: StateFlow<String> = _inputData

    private val _timeDifferenceMinutes = MutableStateFlow<Long?>(null)
    val timeDifferenceMinutes: StateFlow<Long?> = _timeDifferenceMinutes

    private val _isTimeValid = MutableStateFlow(true)
    val isTimeValid: StateFlow<Boolean> = _isTimeValid

    private val _currentCity = MutableStateFlow("current location")
    val currentCity: StateFlow<String> = _currentCity

    private val _selectedCity = MutableStateFlow(FavouritePlace("","",""))
    val selectedCity: StateFlow<FavouritePlace> = _selectedCity


    private val _alarmType = MutableStateFlow(R.string.notification)
    val alarmType: StateFlow<Int> = _alarmType

    private val _alarms = MutableStateFlow<List<Alarm>>(arrayListOf())
    val alarms: MutableStateFlow<List<Alarm>> = _alarms

    val alarmTypeOptions = listOf(
        R.string.notification,
        R.string.alarm
    )
    private val _cities = MutableStateFlow<List<FavouritePlace>>(arrayListOf())
    val cities: StateFlow<List<FavouritePlace>> = _cities

    init {
        getAlarms()
        getCities()
    }

    private fun getAlarms() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAlarms().collect {
                alarms.value = it
            }
        }
    }

    private fun getCities() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getFavourites().collect {
                _cities.value = it
            }
        }
    }

    fun saveAlarm(alarm: Alarm) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addAlarm(alarm)
        }
    }

    fun deleteAlarm(alarm: Alarm) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAlarm(alarm)
        }
    }

    fun updateAlarmType(type: Int) {
        _alarmType.value = type
    }

    fun updateCurrentCity(city: FavouritePlace) {
        _currentCity.value = city.name
        _selectedCity.value =city
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateDate(year: Int, month: Int, day: Int) {
        val currentDateTime = _selectedDateTime.value ?: LocalDateTime.now()
        _selectedDateTime.value =
            LocalDateTime.of(year, month + 1, day, currentDateTime.hour, currentDateTime.minute)
        validateTime()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateTime(hour: Int, minute: Int) {
        val currentDateTime = _selectedDateTime.value ?: LocalDateTime.now()
        _selectedDateTime.value = LocalDateTime.of(
            currentDateTime.year,
            currentDateTime.month,
            currentDateTime.dayOfMonth,
            hour,
            minute
        )
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
    fun formatTime(time: Long): String {
        return LocalDateTime.ofInstant(
            Instant.ofEpochMilli(time),
            ZoneId.systemDefault()
        ).format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy"))
    }

}