package be.business.newsapp.domain.model

import be.business.newsapp.domain.model.newsresponse.NewsResponseDto

data class NewsResponse (
    val status: String,
    val totalResults: Int,
    val article: List<Article>
){
    fun fromDto(newsResponseDto: NewsResponseDto) = NewsResponse(
        status = newsResponseDto.status,
        totalResults = newsResponseDto.totalResults ?: 0,
        article = newsResponseDto.articles.map { it.toDomain() }
    )
}