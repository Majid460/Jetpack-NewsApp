package be.business.newsapp.core.data.local.datastore

import be.business.newsapp.core.data.local.room.NewsDatabase


class DataManager(
    val preferences: PreferenceRepositoryImpl,
    val database: NewsDatabase
)