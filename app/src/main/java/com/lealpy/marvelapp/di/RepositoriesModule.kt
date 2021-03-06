package com.lealpy.marvelapp.di

import com.lealpy.marvelapp.data.api.CharactersApi
import com.lealpy.marvelapp.data.database.CharactersDao
import com.lealpy.marvelapp.data.repositories.CharactersRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class RepositoriesModule {

    @Provides
    fun provideCharactersRepository(
        charactersApi: CharactersApi,
        charactersDao: CharactersDao,
    ): CharactersRepositoryImpl {
        return CharactersRepositoryImpl(
            charactersApi = charactersApi,
            charactersDao = charactersDao,
        )
    }

}
