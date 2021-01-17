package ca.chronofit.chrono.util.objects

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SettingsViewModel : ViewModel() {
    val darkMode: LiveData<Boolean> get() = _darkMode
    private val _darkMode = MutableLiveData<Boolean>()

    val notifications: LiveData<Boolean> get() = _notifications
    private val _notifications = MutableLiveData<Boolean>()

    val audioPrompts: LiveData<Boolean> get() = _audioPrompts
    private val _audioPrompts = MutableLiveData<Boolean>()

    val getReadyTime: LiveData<String> get() = _getReadyTime
    private val _getReadyTime = MutableLiveData<String>()

    fun onDarkModeChanged(setting: Boolean) {
        _darkMode.value = setting
    }

    fun onNotificationChanged(setting: Boolean) {
        _notifications.value = setting
    }

    fun onAudioPromptChanged(setting: Boolean) {
        _audioPrompts.value = setting
    }

    fun onReadyTimeChanged(setting: String) {
        _getReadyTime.value = setting
    }
}