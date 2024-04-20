package com.example.cra2go.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.android.volley.toolbox.Volley
import com.example.cra2go.CRA2go
import com.example.cra2go.feature_roster.data.data_source.DutyEventDatabase
import com.example.cra2go.feature_roster.data.repository.CRARepositoryImp
import com.example.cra2go.feature_roster.data.repository.DutyEventRepositoryImp
import com.example.cra2go.feature_roster.domain.repository.CRARepository
import com.example.cra2go.feature_roster.domain.repository.DutyEventRepository
import com.example.cra2go.feature_roster.domain.use_cases.DeleteDutyEventUseCase
import com.example.cra2go.feature_roster.domain.use_cases.DutyEventUseCases
import com.example.cra2go.feature_roster.domain.use_cases.GetDutyEventsUseCase
import com.example.cra2go.feature_roster.domain.use_cases.UpdateDutyEventsFromCRAUseCase
import com.example.cra2go.feature_roster.presentation.show_roster.MainActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDutyEventDatabase(app:Application):DutyEventDatabase{
        return Room.databaseBuilder(
            app,
            DutyEventDatabase::class.java,
            DutyEventDatabase.DATABASE_NAME
        ).allowMainThreadQueries().build()
    }

    @Provides
    @Singleton
    fun provideDutyEventRepository(db:DutyEventDatabase):DutyEventRepository{
        return DutyEventRepositoryImp(db.dutyeventDao)
    }

    @Provides
    @Singleton
    fun provideDutyEventUseCases(repository: DutyEventRepository,craRepository: CRARepository):DutyEventUseCases{
        return DutyEventUseCases(
            getDutyEvents = GetDutyEventsUseCase(repository),
            deleteDutyEvent = DeleteDutyEventUseCase(repository),
            updateDutyEventsFromCRA = UpdateDutyEventsFromCRAUseCase(repository,craRepository)
        )
    }

    @Provides
    @Singleton
    fun provideCRARepository(@ApplicationContext appContext: Context,db: DutyEventDatabase):CRARepository{
        return CRARepositoryImp(
            dao = db.dutyeventDao,
            queue = Volley.newRequestQueue(appContext)
        )
    }

}