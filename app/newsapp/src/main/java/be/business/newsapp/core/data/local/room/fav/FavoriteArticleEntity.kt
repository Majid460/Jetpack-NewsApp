package be.business.newsapp.core.data.local.room.fav

import androidx.room.Entity
import androidx.room.PrimaryKey
import be.business.newsapp.domain.model.Article
import be.business.newsapp.domain.model.Source
import be.business.newsapp.domain.model.newsresponse.ArticleDto
import be.business.newsapp.domain.model.newsresponse.SourceDto

@Entity(tableName = "favorites")
data class FavoriteArticleEntity(
    @PrimaryKey val url: String,        // unique ID
    val title: String,
    val author: String?,
    val description: String?,
    val urlToImage: String?,
    val publishedAt: String?,
    val content: String?,
    val isFavorite: Boolean = true
){
    fun toDto() = ArticleDto(
        source = SourceDto(id = null, name = ""),
        author = author,
        title = title,
        description = description,
        url = url,
        urlToImage = urlToImage,
        publishedAt = publishedAt,
        content = content,
    )
    fun toDomain() = Article(
        source = Source(id = null, name = ""),
        author = author,
        title = title,
        description = description,
        url = url,
        urlToImage = urlToImage,
        publishedAt = publishedAt,
        content = content,
        isFavorite = isFavorite
    )

}

