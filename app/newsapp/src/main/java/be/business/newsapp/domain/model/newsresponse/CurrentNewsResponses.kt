package be.business.newsapp.domain.model.newsresponse

import be.business.newsapp.domain.model.Article
import be.business.newsapp.domain.model.NewsResponse
import be.business.newsapp.domain.model.Source
import kotlinx.serialization.Serializable

@Serializable
data class NewsResponseDto(
    val status: String,
    val totalResults: Int? = null,
    val articles: List<ArticleDto> = emptyList()
){
    fun toDomain(favorites: Set<String> = emptySet()): NewsResponse {
        return NewsResponse(
            status = status,
            totalResults = totalResults ?: 0,
            article = articles.map { it.toDomain(isFavorite = favorites.contains(it.url)) }
        )
    }

}

@Serializable
data class ArticleDto(
    val source: SourceDto,
    val author: String? = null,
    val title: String,
    val description: String? = null,
    val url: String? = null,
    val urlToImage: String? = null,
    val publishedAt: String? = null,
    val content: String? = null,
){
    fun toDomain(isFavorite: Boolean = false) = Article(
        url = url ?: "",
        title = title,
        author = author,
        description = description,
        urlToImage = urlToImage,
        publishedAt = publishedAt,
        content = content,
        source = source.toDomain(),
        isFavorite = isFavorite
    )

}

@Serializable
data class SourceDto(
    val id: String? = null,
    val name: String
){
    fun toDomain() = Source(
        id = id,
        name = name
    )
}
