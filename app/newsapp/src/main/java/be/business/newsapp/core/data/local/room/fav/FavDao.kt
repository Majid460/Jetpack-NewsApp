package be.business.newsapp.core.data.local.room.fav

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(article: FavoriteArticleEntity)

    @Query("DELETE FROM favorites WHERE url = :url")
    suspend fun removeFavorite(url: String)

    @Query("SELECT * FROM favorites")
    fun getFavorites(): Flow<List<FavoriteArticleEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE url = :url)")
    suspend fun isFavorite(url: String): Boolean

    @Query("SELECT * FROM favorites WHERE isFavorite = 1")
    suspend fun getFavoriteArticlesOnce(): List<FavoriteArticleEntity>
}