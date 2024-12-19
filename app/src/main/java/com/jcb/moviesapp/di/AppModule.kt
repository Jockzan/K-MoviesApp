package com.jcb.moviesapp.di

import com.jcb.moviesapp.data.api.MoviesService
import com.jcb.moviesapp.data.api.createMoviesService
import com.jcb.moviesapp.data.api.network.NetworkInteractor
import com.jcb.moviesapp.data.api.network.NetworkInteractorImpl
import com.jcb.moviesapp.data.repositories.DetailsRepositoryImpl
import com.jcb.moviesapp.data.repositories.GenreRepositoryImpl
import com.jcb.moviesapp.data.repositories.MoviesRepositoryImpl
import com.jcb.moviesapp.domain.repositories.DetailsRepository
import com.jcb.moviesapp.domain.repositories.GenreRepository
import com.jcb.moviesapp.domain.repositories.MoviesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNetworkInteractor(): NetworkInteractor = NetworkInteractorImpl()

    @Provides
    @Singleton
    fun provideMoviesService(): MoviesService = createMoviesService()

    @Provides
    @Singleton
    fun provideGenreRepository(
        networkInteractor: NetworkInteractor,
        moviesService: MoviesService
    ): GenreRepository = GenreRepositoryImpl(networkInteractor, moviesService)

    @Provides
    @Singleton
    fun provideMoviesRepository(
        networkInteractor: NetworkInteractor,
        moviesService: MoviesService
    ): MoviesRepository = MoviesRepositoryImpl(networkInteractor, moviesService)

    @Provides
    @Singleton
    fun provideDetailsRepository(
        networkInteractor: NetworkInteractor,
        moviesService: MoviesService
    ): DetailsRepository = DetailsRepositoryImpl(networkInteractor, moviesService)
}