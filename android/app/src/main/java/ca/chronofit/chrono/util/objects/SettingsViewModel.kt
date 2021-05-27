package ca.chronofit.chrono.util.objects

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.Serializable

class SettingsViewModel : ViewModel(), Serializable {
    val darkMode: LiveData<String> get() = _darkMode
    private val _darkMode = MutableLiveData<String>()

    val notifications: LiveData<Boolean> get() = _notifications
    private val _notifications = MutableLiveData<Boolean>()

    val getReadyTime: LiveData<String> get() = _getReadyTime
    private val _getReadyTime = MutableLiveData<String>()

    val lastRest: LiveData<Boolean> get() = _lastRest
    private val _lastRest = MutableLiveData<Boolean>()

    val audioPrompts: LiveData<Boolean> get() = _audioPrompts
    private val _audioPrompts = MutableLiveData<Boolean>()

    val soundEffect: LiveData<String> get() = _soundEffect
    private val _soundEffect = MutableLiveData<String>()

    fun onDarkModeChanged(setting: String) {
        _darkMode.value = setting
    }

    fun onNotificationChanged(setting: Boolean) {
        _notifications.value = setting
    }

    fun onReadyTimeChanged(setting: String) {
        _getReadyTime.value = setting
    }

    fun onLastRestChanged(setting: Boolean) {
        _lastRest.value = setting
    }

    fun onAudioPromptChanged(setting: Boolean) {
        _audioPrompts.value = setting
    }

    fun onSoundEffectChanged(setting: String) {
        _soundEffect.value = setting
    }
}