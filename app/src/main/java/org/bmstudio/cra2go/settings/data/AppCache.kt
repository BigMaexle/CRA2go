package org.bmstudio.cra2go.settings.data

import android.util.Log
import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.SerializationException
import javax.inject.Inject


// constructor injection with hilt
class AppCache @Inject constructor(private val dataStore: DataStore<AppSettings>){

    private val _cacheState = MutableStateFlow<AppSettings>(AppSettings.default)
    var state = _cacheState.asStateFlow()

    // loading in the data
    suspend fun loadInCache() {
        try {
            dataStore.data.collect {
                _cacheState.value = it
            }
        } catch(error: SerializationException) {
            // handle the excetion
        }
    }

    // storing the new data
    suspend fun storeAirline(airline: String) {
        dataStore.updateData { actualSettings: AppSettings ->
            actualSettings.copy(airline = airline)
        }
    }

    // storing the new data
    suspend fun storeEventFilter(filter: List<Boolean>) {

        Log.i("SAVER","SPEICHERE NEUE SETTINGS:")
        Log.i("SAVER",filter.toString())

        dataStore.updateData { actualSettings: AppSettings ->
            actualSettings.copy(eventsdisplayfilter = filter)
        }
    }


}
