package ru.namerpro.playlistmaker.search.di

import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.namerpro.playlistmaker.search.data.network.ItunesServiceApi
import ru.namerpro.playlistmaker.search.data.network.NetworkClient
import ru.namerpro.playlistmaker.search.data.network.RetrofitNetworkClient
import ru.namerpro.playlistmaker.search.data.network.TracksRepositoryImpl
import ru.namerpro.playlistmaker.search.domain.api.TracksRepository

val searchNetworkModule = module {

    single<NetworkClient> {
        RetrofitNetworkClient(get())
    }

    single<ItunesServiceApi> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ItunesServiceApi::class.java)
    }

    single<TracksRepository> {
        TracksRepositoryImpl(get())
    }

}