package be.business.newsapp.core.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import be.business.newsapp.core.data.local.room.fav.FavoriteArticleEntity
import be.business.newsapp.core.data.local.room.fav.FavoriteDao


@Database(
    entities = [FavoriteArticleEntity::class],
    version = 1,
    exportSchema = false
)
//@TypeConverters(TypeConverter::class)
abstract class NewsDatabase : RoomDatabase() {
    // DAO
    abstract fun favNewDao(): FavoriteDao
}