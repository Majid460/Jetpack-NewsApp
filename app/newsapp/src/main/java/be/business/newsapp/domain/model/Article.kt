package be.business.newsapp.domain.model

import be.business.newsapp.core.data.local.room.fav.FavoriteArticleEntity
import be.business.newsapp.domain.model.newsresponse.ArticleDto
import be.business.newsapp.domain.model.newsresponse.SourceDto

data class Article(
    val source: Source,
    val author: String?,
    val title: String,
    val description: String?,
    val url: String?,
    val urlToImage: String?,
    val publishedAt: String?,
    val content: String?,
    val isFavorite: Boolean
){
    fun toEntity() = FavoriteArticleEntity(
        url = url ?: "",
        title = title,
        author = author,
        description = description,
        urlToImage = urlToImage,
        publishedAt = publishedAt,
        content = content,
        isFavorite = isFavorite
    )
    fun toDto() = ArticleDto(
        source = source.toDto(),
        author = author,
        title = title,
        description = description,
        url = url,
        urlToImage = urlToImage,
        publishedAt = publishedAt,
        content = content
    )
}

data class Source(
    val id: String?,
    val name: String
){
    fun toDto() = SourceDto(
        id = id,
        name = name
    )
}
