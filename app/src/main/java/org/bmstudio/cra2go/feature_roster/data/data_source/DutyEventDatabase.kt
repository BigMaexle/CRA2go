package org.bmstudio.cra2go.feature_roster.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.bmstudio.cra2go.feature_roster.domain.model.DataBaseConverters
import org.bmstudio.cra2go.feature_roster.domain.model.DutyEvent

@Database(
    entities = [DutyEvent::class],
    version = 1
)
@TypeConverters(DataBaseConverters::class)
abstract class DutyEventDatabase : RoomDatabase() {

    abstract val dutyeventDao: DutyEventDao

    companion object {
        const val DATABASE_NAME = "DutyEventDB"
    }
}