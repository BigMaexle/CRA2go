package org.bmstudio.cra2go.settings.data


import android.content.Context
import android.util.Log
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream
import java.util.Dictionary


@Serializable
data class AppSettings(
    val airline: String,
    val test_active: Boolean,
    val eventsdisplayfilter: List<Boolean>
) {


    /* DisplayArrayDisplayed
    0: FLIGHTS
    1: GROUNDEVENTS
    2: VAC
    3: OFF/FREE
     */

    companion object {
        val default = AppSettings(
            "LCAG",false,
            listOf(true,true,true,true)
        )
    }
}

// serializer with our defined data class AppSettings
object SettingsSerializer : Serializer<AppSettings> {

    // default value of the settings for initialization
    override val defaultValue: AppSettings = AppSettings.default

    // reading the inputstream of the stored file
    override suspend fun readFrom(input: InputStream): AppSettings {
        try {
            return Json.decodeFromString(
                deserializer = AppSettings.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (exception: SerializationException) {
            Log.e("SettingsSerializer", "Error reading settings", exception)
            return defaultValue
        }
    }

    // writting the the output stream with actual datatype
    override suspend fun writeTo(
        t: AppSettings,
        output: OutputStream
    ) = output.write(
        Json.encodeToString(serializer = AppSettings.serializer(), value = t).toByteArray()
    )

}

// context property delegate with the datastore
val Context.settingsDataStore: DataStore<AppSettings> by dataStore(
    fileName = "settings.json",
    serializer = SettingsSerializer
)
