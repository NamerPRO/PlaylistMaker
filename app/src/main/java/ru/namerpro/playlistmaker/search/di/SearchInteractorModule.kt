package ru.namerpro.playlistmaker.search.di

import com.google.gson.Gson
import org.koin.dsl.module
import ru.namerpro.playlistmaker.search.domain.api.HistoryInteractor
import ru.namerpro.playlistmaker.search.domain.api.SharedPreferencesSearchInteractor
import ru.namerpro.playlistmaker.search.domain.api.TracksInteractor
import ru.namerpro.playlistmaker.search.domain.impl.HistoryInteractorImpl
import ru.namerpro.playlistmaker.search.domain.impl.SharedPreferencesSearchInteractorImpl
import ru.namerpro.playlistmaker.search.domain.impl.TracksInteractorImpl
import java.util.concurrent.Executors

val searchInteractorModule = module {

    single<SharedPreferencesSearchInteractor> {
        SharedPreferencesSearchInteractorImpl(get(), get())
    }

    single<HistoryInteractor> {
        HistoryInteractorImpl(ArrayList(10))
    }

    factory<TracksInteractor> {
        TracksInteractorImpl(get())
    }

    factory {
        Executors.newCachedThreadPool()
    }

    single {
        Gson()
    }

}