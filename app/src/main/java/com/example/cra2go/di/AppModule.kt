package com.example.cra2go.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.cra2go.feature_roster.data.data_source.DutyEventDatabase
import com.example.cra2go.feature_roster.data.repository.CRARepositoryImp
import com.example.cra2go.feature_roster.data.repository.DutyEventRepositoryImp
import com.example.cra2go.feature_roster.data.repository.RetrofitInstance
import com.example.cra2go.feature_roster.domain.repository.CRARepository
import com.example.cra2go.feature_roster.domain.repository.DutyEventRepository
import com.example.cra2go.feature_roster.domain.use_cases.DeleteDutyEventUseCase
import com.example.cra2go.feature_roster.domain.use_cases.DutyEventUseCases
import com.example.cra2go.feature_roster.domain.use_cases.GetDutyEventsUseCase
import com.example.cra2go.feature_roster.domain.use_cases.UpdateDutyEventsFromCRAUseCase
import com.example.cra2go.login.data.repository.AccessTokenRepositoryImp
import com.example.cra2go.login.domain.repository.AccessTokenRepository
import com.example.cra2go.login.domain.use_cases.LoginToCRAUseCase
import com.example.cra2go.login.domain.use_cases.LoginUseCases
import com.example.cra2go.login.domain.use_cases.SaveNewTokenUseCase
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
            updateRoster = UpdateDutyEventsFromCRAUseCase(repository,craRepository)
        )
    }

    @Provides
    @Singleton
    fun provideCRARepository(db: DutyEventDatabase):CRARepository{
        return CRARepositoryImp(
            dao = db.dutyeventDao
        )
    }

    @Provides
    @Singleton
    fun provideLoginUseCases(accessTokenRepository: AccessTokenRepository):LoginUseCases{
        return LoginUseCases(
            loginToCRAUseCase = LoginToCRAUseCase(accessTokenRepository),
            saveNewTokenUseCase = SaveNewTokenUseCase(accessTokenRepository)
        )
    }

    @Provides
    @Singleton
    fun provideAccessTokenRepository():AccessTokenRepository{
        return AccessTokenRepositoryImp(null)
    }


}