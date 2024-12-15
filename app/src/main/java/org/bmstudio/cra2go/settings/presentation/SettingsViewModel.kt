package org.bmstudio.cra2go.settings.presentation

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.bmstudio.cra2go.settings.data.AppCache
import org.bmstudio.cra2go.settings.data.AppSettings
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val appCache: AppCache
) : ViewModel() {

    init {
        viewModelScope.launch {
            appCache.loadInCache()
        }
    }

    private val _appSettings: MutableStateFlow<AppSettings> = MutableStateFlow(AppSettings.default)
    var appSettings = _appSettings.asStateFlow()

    private val _isSwitchOn: MutableStateFlow<Boolean> = MutableStateFlow(false)
    var isSwitchOn = _isSwitchOn.asStateFlow()

    private val _textPreference: MutableStateFlow<String> = MutableStateFlow("")
    var textPreference = _textPreference.asStateFlow()

    private val _intPreference: MutableStateFlow<Int> = MutableStateFlow(0)
    var intPreference = _intPreference.asStateFlow()

    private val _listPreference: MutableStateFlow<List<Boolean>> = MutableStateFlow(listOf())
    var listPreference = _listPreference.asStateFlow()

    private val _eventsFilter: MutableStateFlow<List<Boolean>> = MutableStateFlow(listOf())
    var eventsFilter = _eventsFilter.asStateFlow()

    fun getAirline():String{
        return appCache.state.value.airline
    }

    fun getEventFilters(): List<Boolean>{

        return appCache.state.value.eventsdisplayfilter

    }

    fun readAirline(){

        //Log.d(TAG, "readAirline: ")
        viewModelScope.launch {
            appCache.state.collect{ appSettings ->
                _appSettings.value = appSettings
                Log.d(TAG, "ActiveAirline: ${appSettings.airline}")
            }
        }

    }


    fun toggleSwitch(){
        _isSwitchOn.value = _isSwitchOn.value.not()
        // here is place for permanent storage handling - switch
    }

    fun saveCalenderFilter(finalFilter:List<Boolean>){
        _listPreference.value = finalFilter

        viewModelScope.launch {
            appCache.storeEventFilter(_listPreference.value)
        }


    }

    fun saveAirline(finalText: String) {
        _textPreference.value = finalText

        viewModelScope.launch {
            appCache.storeAirline(_textPreference.value)
        }
    }

    // just checking, if it is not empty - but you can check anything
    fun checkTextInput(text: String) = text.isNotEmpty()

    companion object {
        const val TAG = "SettingsViewModel"
    }

}
