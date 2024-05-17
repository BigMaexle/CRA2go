package org.bmstudio.cra2go.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.bmstudio.cra2go.feature_roster.data.data_source.DutyEventDatabase
import org.bmstudio.cra2go.feature_roster.data.repository.CRARepositoryImp
import org.bmstudio.cra2go.feature_roster.data.repository.DutyEventRepositoryImp
import org.bmstudio.cra2go.feature_roster.domain.repository.CRARepository
import org.bmstudio.cra2go.feature_roster.domain.repository.DutyEventRepository
import org.bmstudio.cra2go.feature_roster.domain.use_cases.DeleteDutyEventUseCase
import org.bmstudio.cra2go.feature_roster.domain.use_cases.DutyEventUseCases
import org.bmstudio.cra2go.feature_roster.domain.use_cases.GetDutyEventsUseCase
import org.bmstudio.cra2go.feature_roster.domain.use_cases.UpdateDutyEventsFromCRAUseCase
import org.bmstudio.cra2go.login.data.repository.AccessTokenRepositoryImp
import org.bmstudio.cra2go.login.domain.repository.AccessTokenRepository
import org.bmstudio.cra2go.login.domain.use_cases.LoginToCRAUseCase
import org.bmstudio.cra2go.login.domain.use_cases.LoginUseCases
import org.bmstudio.cra2go.login.domain.use_cases.SaveNewTokenUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDutyEventDatabase(app: Application): DutyEventDatabase {
        return Room.databaseBuilder(
            app,
            DutyEventDatabase::class.java,
            DutyEventDatabase.DATABASE_NAME
        ).allowMainThreadQueries().build()
    }

    @Provides
    @Singleton
    fun provideDutyEventRepository(db: DutyEventDatabase): DutyEventRepository {
        return DutyEventRepositoryImp(db.dutyeventDao)
    }

    @Provides
    @Singleton
    fun provideDutyEventUseCases(
        repository: DutyEventRepository,
        craRepository: CRARepository
    ): DutyEventUseCases {
        return DutyEventUseCases(
            getDutyEvents = GetDutyEventsUseCase(repository),
            deleteDutyEvent = DeleteDutyEventUseCase(repository),
            updateRoster = UpdateDutyEventsFromCRAUseCase(repository, craRepository)
        )
    }

    @Provides
    @Singleton
    fun provideCRARepository(db: DutyEventDatabase): CRARepository {
        return CRARepositoryImp(
            dao = db.dutyeventDao
        )
    }

    @Provides
    @Singleton
    fun provideLoginUseCases(accessTokenRepository: AccessTokenRepository): LoginUseCases {
        return LoginUseCases(
            loginToCRAUseCase = LoginToCRAUseCase(accessTokenRepository),
            saveNewTokenUseCase = SaveNewTokenUseCase(accessTokenRepository)
        )
    }

    @Provides
    @Singleton
    fun provideAccessTokenRepository(): AccessTokenRepository {
        return AccessTokenRepositoryImp(null)
    }


}